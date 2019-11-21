package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.util.logging.Logger

import org.apache.spark.sql.SparkSession
import org.apache.spark.internal.Logging
import scala.collection._

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.scala.comm.App
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil
/*

import com.sccomz.scala.serialize.MakeBinFile
MakeBinFile.executeEngResult("8463233");
MakeBinFile.makeEngSectorResult("8463233", "LOS","SYS/5113566");

 * */
object MakeBinFile extends Logging {

//  val logger: Logger = Logger.getLogger(this.getClass.getName());
  val spark: SparkSession = SparkSession.builder().master("local[*]").appName(this.getClass.getName).getOrCreate();

//  var ruInfo = mutable.Map[String, String]();

  // todo add app name
  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    executeEngResult(scheduleId);
    //executeEngResult("8460062");
  }

  // 2D Bin
  def executeEngResult(scheduleId: String) = {

    // separate this function from the job
    val ruInfo = makeResultPath(scheduleId);
    makeEngResult(ruInfo, scheduleId, "LOS");
    //makeEngResult(scheduleId, "PATHLOSS");
    //makeEngResult(scheduleId, "BEST_SERVER");
    //makeEngResult(scheduleId, "PILOT_EC");     // RSRP
    //makeEngResult(scheduleId, "RSSI");
    //makeEngResult(scheduleId, "C2I");      // SINR

    spark.stop();
  }

  // 3D Bin
  def executeBdResult(scheduleId: String) = {
  }

  // 2D Bin 생성
  def makeEngResult(ruInfo: Map[String, String], scheduleId: String, cdNm: String) = {
    makeEngSectorResult(scheduleId, cdNm, ruInfo.getOrElse("SECTOR_PATH", ""));
    //makeEngRuResult(scheduleId, cdNm, ruInfo);

    if (cdNm == "LOS" || cdNm == "PATHLOSS") {
//      makeEngRuResult(scheduleId, cdNm, ruInfo);
    }
  }

  // 폴더 생성 메소드
  def makeResultPath(scheduleId: String): mutable.Map[String, String] = {
    var ruInfo = mutable.Map[String, String]()
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var qry = MakeBinFileSql.selectBinFilePath(scheduleId); ; println(qry);
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
        logInfo("Directory Drop Complete!!");
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
    }
    ruInfo;
  }

  // todo use jdbc
  def getBinCounts(scheduleId: String): (Int, Int) = {
    val qry = MakeBinFileSql.selectBinCnt(scheduleId)
    logInfo(qry)
    val sqlDf = spark.sql(qry)

    val row = sqlDf.collect.last
    val x_bin_cnt = row(0).asInstanceOf[Int]
    val y_bin_cnt = row(1).asInstanceOf[Int]
    
    println(s"x_bin_cnt=$x_bin_cnt, y_bin_cnt=$y_bin_cnt")

    (x_bin_cnt, y_bin_cnt)
  }
  
  def initialArray(x_bin_count: Int, y_bin_count: Int, initialValue: Array[Byte]) = {
      val iv = new Byte4(initialValue)

      val bin = Array.ofDim[Byte4](x_bin_count * y_bin_count)

      for (i <- 0 until x_bin_count * y_bin_count by 1) {
          bin(i) = iv
      }

      bin
  }

  // 2D 섹터 결과
  def makeEngSectorResult(scheduleId: String, cdNm: String, sectorPath: String) = {
    val binCounts = getBinCounts(scheduleId)

    val initialValue = 
      cdNm match {
      case "PATHLOSS" =>
          ByteUtil.floatMax()
      case _ =>
          ByteUtil.intZero()
      }
    
    val bin = initialArray(binCounts._1, binCounts._2, initialValue)


    logInfo("============================ Value 세팅 ============================")
    //---------------------------------------------------------------------------------------------------------
    // Value 세팅
    //---------------------------------------------------------------------------------------------------------

    val names = cdNm match {
      case "LOS" => ("RESULT_NR_2D_LOS", "LOS")
      case "PATHLOSS" => ("RESULT_NR_2D_PATHLOSS", "PATHLOSS")
      case "BEST_SERVER" => ("RESULT_NR_2D_BESTSERVER", "RU_SEQ")
      case "PILOT_EC" => ("RESULT_NR_2D_RSRP", "RSRP")
      case "RSSI" => ("RESULT_NR_2D_RSSI", "RSSI")
      case "C2I" => ("RESULT_NR_2D_SINR", "SINR")
    }

    val tabNm = names._1
    val colNm = names._2

    // SELECT DISTINCT X_POINT, Y_POINT, ${colNm} FROM   ${tabNm} WHERE  SCHEDULE_ID = ${scheduleId} ORDER BY X_POINT, Y_POINT
    val qry = MakeBinFileSql.selectSectorResult(scheduleId, tabNm, colNm)
    logInfo(qry)
    val sqlDf = spark.sql(qry).repartition(1)
    
    sqlDf.foreachPartition { p =>
      println("partition start")

      val bin = initialArray(binCounts._1, binCounts._2, initialValue)

      p.foreach { row =>
       val i = row(0).asInstanceOf[Int] * binCounts._1 + row(1).asInstanceOf[Int]

       bin(i).value = colNm match {
          case "LOS" =>
            ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
          case "PATHLOSS" =>
            ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
          case "RU_SEQ" =>
            ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
          case "RSRP" =>
            ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
          case "RSSI" =>
            ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
          case _ =>
            ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
        }
        
      }
      
      println("writing to file")
//      println(bin.mkString(","))
      
      import org.apache.hadoop.fs.{FileSystem, Path}
        val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
       val out = fs.create(new Path("/user/icpap/sector"))
       bin.foreach { e =>
       out.write(e.value)
        }
//    out.write(image);
    out.close();
      
      println("partition end")
    }

/*    sqlDf.collect.foreach { row =>
      val i = row(0).asInstanceOf[Int] * binCounts._1 + row(1).asInstanceOf[Int]

      bin(i).value = colNm match {
        case "LOS" =>
          ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
        case "PATHLOSS" =>
          ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
        case "RU_SEQ" =>
          ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
        case "RSRP" =>
          ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
        case "RSSI" =>
          ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
        case _ =>
          ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
      }
    }
*/
    logInfo("============================ 파일 Write ============================")
    //---------------------------------------------------------------------------------------------------------
    // 파일 Write
    //---------------------------------------------------------------------------------------------------------
    /*    val file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" + sectorPath + "/" + cdNm + ".bin")
    //var file = new File(App.resultPath, "20191116" + "/" +sectorPath+ "/" + cdNm+".bin");
    val fos = new FileOutputStream(file)

    for (y <- 0 until binCounts._2 by 1) {
      for (x <- 0 until binCounts._1 by 1) {
        fos.write(bin(x)(y).value)
      }
    }

    logInfo("=========================== Bin 생성 완료 ===========================")
    if (fos != null) fos.close()
*/ }

  // 2D RU별 결과
  def makeEngRuResult(scheduleId: String, cdNm: String, ruInfo: mutable.Map[String, String]) = {

    logInfo("makeEngRuResult 01")

    val names = cdNm match {
      case "LOS" => ("RESULT_NR_2D_LOS_RU", "VALUE")
      case "PATHLOSS" => ("RESULT_NR_2D_PATHLOSS_RU", "PATHLOSS")
    }

    val tabNm = names._1
    val colNm = names._2

    // todo unnecessary statement?
    spark.sql("DROP TABLE IF EXISTS ENG_RU")

    val qry = MakeBinFileSql.selectRuResultAll(scheduleId, tabNm, colNm)
    logInfo(qry)

    val tDF = spark.sql(qry)
    tDF.cache.createOrReplaceTempView("ENG_RU")
    //    tDF.count();

    logInfo("makeEngRuResult 02")

    //    val iCnt = ruInfo.size;

    // todo use jdbc
    def getRUBinCounts(scheduleId: String, ruId: (String, String)): (Int, Int) = {
      logInfo("makeEngRuResult 04 " + ruId._1)

      val qry = MakeBinFileSql.select2dRuBinCnt(scheduleId, ruId._1)
      logInfo(qry)
      val sqlDf = spark.sql(qry)

      val row = sqlDf.collect.last
      val x_bin_cnt = row(0).asInstanceOf[Int]
      val y_bin_cnt = row(1).asInstanceOf[Int]

      (x_bin_cnt, y_bin_cnt)
    }

    ruInfo.foreach { ruId =>
      println(ruId._1 + " : " + ruId._2)

      if (ruId._1 != "SECTOR_PATH") {
        logInfo("============================== 초기화 ==============================")
        //---------------------------------------------------------------------------------------------------------
        // 초기화
        //---------------------------------------------------------------------------------------------------------
        val binCounts = getRUBinCounts(scheduleId, ruId)

        val x_bin_cnt = binCounts._1
        val y_bin_cnt = binCounts._2

        val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt)

        if (cdNm == "LOS") {
          for (y <- 0 until y_bin_cnt by 1) {
            for (x <- 0 until x_bin_cnt by 1) {
              bin(x)(y) = new Byte4(ByteUtil.intZero())
            }
          }
        } else {
          for (y <- 0 until y_bin_cnt by 1) {
            for (x <- 0 until x_bin_cnt by 1) {
              bin(x)(y) = new Byte4(ByteUtil.floatMax())
            }
          }
        }

        logInfo("============================ RU별 Value 세팅 ============================")
        //---------------------------------------------------------------------------------------------------------
        // Value 세팅
        //---------------------------------------------------------------------------------------------------------
        logInfo("makeEngRuResult 05 " + ruId._1)

        //var qry2 = MakeBinFileSql4.selectRuResult(scheduleId, tabNm, colNm, ruId._1);
        // todo add a column called RU_ID
        val qry = MakeBinFileSql.selectRuResult2(ruId._1)
        logInfo(qry)
        
        import spark.implicits._

        // ru_id 에 의한 파티션
        // 처음부터 파티션 돼 있는 것이 좋음
        val sqlDf2 = spark.sql(qry).repartition($"RU_ID")

        logInfo("makeEngRuResult 06 " + ruId._1)
        
        sqlDf2.foreachPartition { p =>
          println("partition start")
          
          // bin 초기화
//          val bin
          
          // bin 설정
          p.foreach { row =>
            
          }
          
          // bin 출력
          
          println("partition end")
        }

        sqlDf2.collect.foreach { row =>
          val x_point = row(0).asInstanceOf[Int]
          val y_point = row(1).asInstanceOf[Int]

          bin(x_point)(y_point).value = cdNm match {
            case "LOS" =>
              ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
            case _ =>
              ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])

          }
        }

        logInfo("makeEngRuResult 07 " + ruId._1)

        logInfo("============================ RU별 파일 Write ============================")
        //---------------------------------------------------------------------------------------------------------
        // 파일 Write
        //---------------------------------------------------------------------------------------------------------
        val file = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" + ruId._2 + "/" + cdNm + ".bin")
        val fos = new FileOutputStream(file)
        for (y <- 0 until y_bin_cnt by 1) {
          for (x <- 0 until x_bin_cnt by 1) {
            fos.write(bin(x)(y).value)
          }
        }

        logInfo("makeEngRuResult 08 " + ruId._1)

        logInfo("=========================== RU별 Bin 생성 완료 ===========================")
        if (fos != null) fos.close()
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