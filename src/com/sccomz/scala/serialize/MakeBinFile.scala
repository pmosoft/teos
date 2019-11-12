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

import com.sccomz.scala.serialize.MakeBinFile
MakeBinFile.executeEngResult("8460062");
  
 * */
object MakeBinFile {

  val logger: Logger = Logger.getLogger(this.getClass.getName());
  val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).getOrCreate();
  
  var ruInfo = mutable.Map[String,String]();
  
  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    executeEngResult(scheduleId);
    //executeEngResult("8460062");
  }

  // 2D Bin
  def executeEngResult(scheduleId: String) = {
    
    makeResultPath(scheduleId);
    makeEngResult(scheduleId, "LOS");
    makeEngResult(scheduleId, "PATHLOSS");
    makeEngResult(scheduleId, "BEST_SERVER");
    //makeEngResult(scheduleId, "PILOT_EC");     // RSRP
    //makeEngResult(scheduleId, "RSSI");
    //makeEngResult(scheduleId, "C2I");      // SINR
    
    spark.stop();
  }

  // 3D Bin
  def executeBdResult(scheduleId: String) = {
  }
  
  // 2D 폴더 생성
  def makeEngResult(scheduleId: String, cdNm: String) = {
    makeEngSectorResult(scheduleId, cdNm, ruInfo.getOrElse("SECTOR_PATH",""));
    //makeEngRuResult(scheduleId, cdNm, ruInfo);

    if(cdNm == "LOS" || cdNm == "PATHLOSS") {
    	makeEngRuResult(scheduleId, cdNm, ruInfo);
    }
  }
  
  // 폴더 생성 메소드
  def makeResultPath(scheduleId : String) : mutable.Map[String,String] = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var qry = MakeBinFileSql4.selectBinFilePath(scheduleId);
    var rs = stat.executeQuery(qry);
    var rowCnt = 1;

    var ruIdList = mutable.MutableList[String]();
    var ruPathList = mutable.MutableList[String]();

    while (rs.next()) {
      // 처음 시작하자마자 폴더 유무 확인 후 있으면 삭제하고 다시 생성.
      if (rowCnt == 1) {
        ruInfo += ("SECTOR_PATH" -> rs.getString("SECTOR_PATH"));

        // 폴더 삭제
        FileUtil.delFiles2(App.resultPath + "/" + DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("SECTOR_PATH"));
        logger.info("Directory Drop Complete!!");
      }

      // RU 정보 생성
      ruInfo += (rs.getString("RU_ID") -> rs.getString("RU_PATH"));

//      if (rowCnt <= 150) {
        // 폴더 생성
        var dir = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("RU_PATH"));
        if (!dir.exists()) dir.mkdirs();
        println(dir);
//      }
      rowCnt = rowCnt + 1;
    };
    ruInfo;
  }

  // 2D 섹터 결과
  def makeEngSectorResult(scheduleId: String, cdNm: String, sectorPath: String) = {
    var x_bin_cnt = 0; var y_bin_cnt = 0;

    logger.info("============================= 초기화 ==============================");
    var qry2= MakeBinFileSql4.selectBinCnt(scheduleId);
    var sqlDf = spark.sql(qry2);
    
    for (row <- sqlDf.collect) {
       x_bin_cnt = row.mkString(",").split(",")(0).toInt;
       y_bin_cnt = row.mkString(",").split(",")(1).toInt;
    }

    val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt);
    
    if(cdNm == "PATHLOSS") {
    	for (y <- 0 until y_bin_cnt by 1) {
    		for (x <- 0 until x_bin_cnt by 1) {
    			bin(x)(y) = new Byte4(ByteUtil.floatMax());
    		}
    	}
    } else {
      for (y <- 0 until y_bin_cnt by 1) {
    		for (x <- 0 until x_bin_cnt by 1) {
    		  bin(x)(y) = new Byte4(ByteUtil.intZero());
    		}
    	}
    }

    logger.info("============================ Value 세팅 ============================");
    //---------------------------------------------------------------------------------------------------------
    // Value 세팅
    //---------------------------------------------------------------------------------------------------------
    var tabNm = "";  var colNm = "";
         if(cdNm=="LOS"     ) { tabNm = "RESULT_NR_2D_LOS"      ; colNm = "LOS"     ;}
    else if(cdNm=="PATHLOSS") { tabNm = "RESULT_NR_2D_PATHLOSS" ; colNm = "PATHLOSS";}
    else if(cdNm=="BEST_SERVER") { tabNm = "RESULT_NR_2D_BESTSERVER" ; colNm = "RU_SEQ";}
    else if(cdNm=="PILOT_EC") { tabNm = "RESULT_NR_2D_RSRP" ; colNm = "RSRP";}
    else if(cdNm=="RSSI") { tabNm = "RESULT_NR_2D_RSSI" ; colNm = "RSSI";}
    else if(cdNm=="C2I") { tabNm = "RESULT_NR_2D_SINR" ; colNm = "SINR";}
    qry2= MakeBinFileSql4.selectSectorResult(scheduleId, tabNm, colNm);
    println(qry2);
    sqlDf = spark.sql(qry2);
    
    if(colNm == "LOS") {
    	for (row <- sqlDf.collect) {
    	var x_point = row.mkString(",").split(",")(0).toInt;
    	var y_point = row.mkString(",").split(",")(1).toInt;
    	var los = row.mkString(",").split(",")(2).toInt;
    	bin(x_point)(y_point).value = ByteUtil.intToByteArray(los);
    	}      
    } else if(colNm == "PATHLOSS") {
      for (row <- sqlDf.collect) {
        var x_point = row.mkString(",").split(",")(0).toInt;
        var y_point = row.mkString(",").split(",")(1).toInt;
        var pathloss = row.mkString(",").split(",")(2).toFloat;
        bin(x_point)(y_point).value = ByteUtil.floatToByteArray(pathloss);
      }
    } else if(colNm == "RU_SEQ") {
      for (row <- sqlDf.collect) {
        var x_point = row.mkString(",").split(",")(0).toInt;
        var y_point = row.mkString(",").split(",")(1).toInt;
        var bestServer = row.mkString(",").split(",")(2).toInt;
        bin(x_point)(y_point).value = ByteUtil.intToByteArray(bestServer);
      }
    } else if(colNm == "RSRP") {
      for (row <- sqlDf.collect) {
        var x_point = row.mkString(",").split(",")(0).toInt;
        var y_point = row.mkString(",").split(",")(1).toInt;
        var rsrp = row.mkString(",").split(",")(2).toFloat;
        bin(x_point)(y_point).value = ByteUtil.floatToByteArray(rsrp);
      }
    } else if(colNm == "RSSI") {
      for (row <- sqlDf.collect) {
        var x_point = row.mkString(",").split(",")(0).toInt;
        var y_point = row.mkString(",").split(",")(1).toInt;
        var rssi = row.mkString(",").split(",")(2).toFloat;
        bin(x_point)(y_point).value = ByteUtil.floatToByteArray(rssi);
      }
    } else {
      for (row <- sqlDf.collect) {
        var x_point = row.mkString(",").split(",")(0).toInt;
        var y_point = row.mkString(",").split(",")(1).toInt;
        var sinr = row.mkString(",").split(",")(2).toFloat;
        bin(x_point)(y_point).value = ByteUtil.floatToByteArray(sinr);
      }
    }

    logger.info("============================ 파일 Write ============================");
    //---------------------------------------------------------------------------------------------------------
    // 파일 Write
    //---------------------------------------------------------------------------------------------------------
    var file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" +sectorPath+ "/" + cdNm+".bin");
    var fos = new FileOutputStream(file);
    for (y <- 0 until y_bin_cnt by 1) {
      for (x <- 0 until x_bin_cnt by 1) {
        fos.write(bin(x)(y).value);
      }
    }
    
    logger.info("=========================== Bin 생성 완료 ===========================");
    if (fos != null) fos.close();
  }

  // 2D RU별 결과
  def makeEngRuResult(scheduleId: String, cdNm: String, ruInfo : mutable.Map[String,String]) = {

    var tabNm = ""; var colNm = "";
         if(cdNm=="LOS"     ) { tabNm = "RESULT_NR_2D_LOS_RU"      ; colNm = "VALUE";}
    else if(cdNm=="PATHLOSS") { tabNm = "RESULT_NR_2D_PATHLOSS_RU" ; colNm = "PATHLOSS";}
    
    var qry2 = MakeBinFileSql4.selectRuResultAll(scheduleId, tabNm, colNm);
    spark.sql("DROP TABLE IF EXISTS ENG_RU");
    println(qry2); var tDF = spark.sql(qry2); tDF.cache.createOrReplaceTempView("ENG_RU"); tDF.count();
    
    for(ruId <- ruInfo) {
      if(ruId._1 != "SECTOR_PATH") {
        logger.info("============================== 초기화 ==============================");
        //---------------------------------------------------------------------------------------------------------
        // 초기화
        //---------------------------------------------------------------------------------------------------------
        var x_bin_cnt = 0; var y_bin_cnt = 0;
        
        var qry = MakeBinFileSql4.select2dRuBinCnt(scheduleId,ruId._1);

        println(qry); var sqlDf = spark.sql(qry);

        for (row <- sqlDf.collect) {
          x_bin_cnt = row.mkString(",").split(",")(0).toInt;
          y_bin_cnt = row.mkString(",").split(",")(1).toInt;
        }

        val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt);

        if (cdNm == "LOS") {
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
        
        logger.info("============================ RU별 Value 세팅 ============================");
        //---------------------------------------------------------------------------------------------------------
        // Value 세팅
        //---------------------------------------------------------------------------------------------------------
        
        //var qry2 = MakeBinFileSql4.selectRuResult(scheduleId, tabNm, colNm, ruId._1);
        var qry2 = MakeBinFileSql4.selectRuResult2(ruId._1);
        println(qry2);
        var sqlDf2 = spark.sql(qry2);
        
        if(cdNm == "LOS") {
        	for (row <- sqlDf2.collect) {
        	  var x_point = row.mkString(",").split(",")(0).toInt;
        	  var y_point = row.mkString(",").split(",")(1).toInt;
        	  var value = row.mkString(",").split(",")(2).toInt;
        	  bin(x_point)(y_point).value = ByteUtil.intToByteArray(value);
        	}
        } else {
          for (row <- sqlDf2.collect) {
        	  var x_point = row.mkString(",").split(",")(0).toInt;
        	  var y_point = row.mkString(",").split(",")(1).toInt;
        	  var pathloss = row.mkString(",").split(",")(2).toFloat;
        	  bin(x_point)(y_point).value = ByteUtil.floatToByteArray(pathloss);
        	}
        }

        logger.info("============================ RU별 파일 Write ============================");
        //---------------------------------------------------------------------------------------------------------
        // 파일 Write
        //---------------------------------------------------------------------------------------------------------
        var file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" +ruId._2+ "/" + cdNm +".bin");
        var fos = new FileOutputStream(file);
        for (y <- 0 until y_bin_cnt by 1) {
          for (x <- 0 until x_bin_cnt by 1) {
            fos.write(bin(x)(y).value);
          }
        }
        logger.info("=========================== RU별 Bin 생성 완료 ===========================");
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

  // Bin Test
  def test01(scheduleId: String) = {
    val bin = Array.ofDim[Byte4](2, 3);

    bin(0)(0) = new Byte4(ByteUtil.intZero());
    bin(1)(0) = new Byte4(ByteUtil.intZero());
    bin(2)(0) = new Byte4(ByteUtil.intZero());
  }
  
}