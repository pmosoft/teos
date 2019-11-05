package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.util.logging.Logger

import org.apache.spark.sql.SparkSession
import scala.collection._

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.scala.comm.App
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil
/*

import com.sccomz.scala.serialize.MakeBinFile4
MakeBinFile4.executeEngResult("8460062");
  
 * */
object MakeBinFile4 {

  val logger: Logger = Logger.getLogger(this.getClass.getName());
  var spark: SparkSession = null;
  spark = SparkSession.builder().appName("MakeBinFile4").getOrCreate();
  var ruInfo = mutable.Map[String,String]();
  
  def main(args: Array[String]): Unit = {
    executeEngResult("8460062");
  }

  // 2D Bin
  def executeEngResult(scheduleId: String) = {
    makeResultPath(scheduleId);
    makeEngResult(scheduleId, "LOS");
    makeEngResult(scheduleId, "PATHLOSS");
  }

  // 3D Bin
  def executeBdResult(scheduleId: String) = {
  }

  // 2D 폴더 생성
  def makeEngResult(scheduleId: String, cdNm: String) = {
    makeEngSectorResult(scheduleId, cdNm, ruInfo.getOrElse("SECTOR_PATH",""));
//    makeEngRuResult(scheduleId, cdNm, ruInfo);
  }

  // 폴더 생성 메소드
  def makeResultPath(scheduleId : String) : mutable.Map[String,String] = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var qry = MakeBinFileSql4.selectBinFilePath(scheduleId);
    var rs = stat.executeQuery(qry);
    var rowCnt = 1;
    var count = 1;

    var ruIdList = mutable.MutableList[String]();
    var ruPathList = mutable.MutableList[String]();

    while (rs.next()) {
      if (rowCnt == 1) {
        ruInfo += ("SECTOR_PATH" -> rs.getString("SECTOR_PATH"));

        // 폴더 삭제
        FileUtil.delFiles2(App.resultPath + "/" + DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("SECTOR_PATH"));
        logger.info("Directory Drop Complete!!");
      }

      // RU 정보 생성
      ruInfo += (rs.getString("RU_ID") -> rs.getString("RU_PATH"));

      if (rowCnt <= 150) {
        // 폴더 생성
        var dir = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("RU_PATH"));
        if (!dir.exists()) dir.mkdirs();
        println(dir);
      }
      rowCnt = rowCnt + 1;
    };
    ruInfo;
  }


  // 2D 섹터 결과
  def makeEngSectorResult(scheduleId: String, cdNm: String, sectorPath: String) = {

    logger.info("========================== 초기화 ===========================");
    var qry2= MakeBinFileSql4.selectBinCnt(scheduleId);
    var sqlDf = spark.sql("SELECT BIN_X_CNT, BIN_Y_CNT FROM SCHEDULE A WHERE A.SCHEDULE_ID = 8460062");
    var x_bin_cnt = 307;  var y_bin_cnt = 301;
    sqlDf.foreach { row =>
       x_bin_cnt = row.mkString(",").split(",")(0).toInt;
       y_bin_cnt = row.mkString(",").split(",")(1).toInt;
    }

    val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt);
    
    if(cdNm == "LOS") {      
    	for (y <- 0 until y_bin_cnt by 1) {
    		for (x <- 0 until x_bin_cnt by 1) {
    			bin(x)(y) = new Byte4(ByteUtil.intZero());
    		}
    	}
    } else {
      for (y <- 0 until y_bin_cnt by 1) {
    		for (x <- 0 until x_bin_cnt by 1) {
    			bin(x)(y) = new Byte4(ByteUtil.floatMax());
    		}
    	}
    }


    logger.info("========================= Value 세팅 =========================");
    //---------------------------------------------------------------------------------------------------------
    // Value 세팅
    //---------------------------------------------------------------------------------------------------------
    var tabNm = "";  var colNm = "";
         if(cdNm=="LOS"     ) { tabNm = "RESULT_NR_2D_LOS"      ; colNm = "LOS"     ;}
    else if(cdNm=="PATHLOSS") { tabNm = "RESULT_NR_2D_PATHLOSS" ; colNm = "PATHLOSS";}
    qry2= MakeBinFileSql4.selectSectorResult(scheduleId, tabNm, colNm);
    sqlDf = spark.sql(qry2);
    
    if(colNm == "LOS") {
    	sqlDf.foreach { row =>
    	var x_point = row.mkString(",").split(",")(0).toInt;
    	var y_point = row.mkString(",").split(",")(1).toInt;
    	var los = row.mkString(",").split(",")(2).toInt;
    	bin(x_point)(y_point).value = ByteUtil.intToByteArray(los);
    	}      
    } else {
      sqlDf.foreach { row =>
    	var x_point = row.mkString(",").split(",")(0).toInt;
    	var y_point = row.mkString(",").split(",")(1).toInt;
    	var pathloss = row.mkString(",").split(",")(2).toFloat;
    	bin(x_point)(y_point).value = ByteUtil.floatToByteArray(pathloss);
    	}
    }

    logger.info("========================= 파일 Write =========================");
    //---------------------------------------------------------------------------------------------------------
    // 파일 Write
    //---------------------------------------------------------------------------------------------------------
    var file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" +sectorPath+ "/" + colNm+".bin");
    var fos = new FileOutputStream(file);
    for (y <- 0 until y_bin_cnt by 1) {
      for (x <- 0 until x_bin_cnt by 1) {
        fos.write(bin(x)(y).value);
      }
    }
    logger.info("======================== Bin 생성 완료 ========================");
    if (fos != null) fos.close();
  }


  // 2D RU별 결과
  def makeEngRuResult(scheduleId: String, cdNm: String, ruInfo : mutable.Map[String,String] ) = {

    for(ruId <- ruInfo) {
      //println(ruId.get(0));
      if(ruId._1 != "SECTOR_PATH") {
        logger.info("========================= Value 세팅 =========================");
        //---------------------------------------------------------------------------------------------------------
        // Value 세팅
        //---------------------------------------------------------------------------------------------------------
        var tabNm = "";
             if(cdNm=="LOS"     ) { tabNm = "RESULT_NR_2D_LOS_RU"      ; }
        else if(cdNm=="PATHLOSS") { tabNm = "RESULT_NR_2D_PATHLOSS_RU" ; }
        var qry= MakeBinFileSql3.selectRuResult(scheduleId, tabNm, ruId._1);
        val sqlDf = spark.sql(qry);

        sqlDf.foreach { row =>
           var x_point = row.mkString(",").split(",")(0).toInt;
           var y_point = row.mkString(",").split(",")(1).toInt;
           var value = row.mkString(",").split(",")(2).toInt;
           //bin(x_point)(y_point).value = ByteUtil.intToByteArray(los);
        }

        logger.info("========================= 파일 Write =========================");
        //---------------------------------------------------------------------------------------------------------
        // 파일 Write
        //---------------------------------------------------------------------------------------------------------
        var file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" +ruId._2+ "/" + ruId._1+".bin");
        var fos = new FileOutputStream(file);
        //for (y <- 0 until y_bin_cnt by 1) {
        //  for (x <- 0 until x_bin_cnt by 1) {
        //    fos.write(bin(x)(y).value);
        //  }
        //}
        logger.info("======================== Bin 생성 완료 ========================");
        if (fos != null) fos.close();
      }
    }
  }

  // 3D 섹터별 결과
  def makeBdSectorResult(scheduleId: String, cdNm: String) = {
  }

  // 3D Ru별 결과
  def makeBdRuResult(scheduleId: String) = {
  }

}