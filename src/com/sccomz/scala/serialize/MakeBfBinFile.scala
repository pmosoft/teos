package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Statement
import java.sql.ResultSet
import java.sql.Connection

import scala.collection._

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission

import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil
import com.sccomz.scala.comm.App

/*

import com.sccomz.scala.serialize.MakeBfBinFile
MakeBfBinFile.executeResult("8463233");
MakeBfBinFile.makeSectorResult("8463233", "LOS","SYS/5113566");

 * */
object MakeBfBinFile extends Logging {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;

  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    executeResult(scheduleId);
    //executeResult("8460062");
  }

  def executeResult(scheduleId: String) = {

    val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).getOrCreate();

    // separate this function from the job
    val ruInfo = MakeFolder.makeResultPath(scheduleId);
    makeResult(spark, ruInfo, scheduleId, "LOS");
    //makeResult(scheduleId, "PATHLOSS");
    //makeResult(scheduleId, "BEST_SERVER");
    //makeResult(scheduleId, "PILOT_EC");     // RSRP
    //makeResult(scheduleId, "RSSI");
    //makeResult(scheduleId, "C2I");      // SINR

    spark.stop();
  }

  // Bin 생성
  def makeResult(spark: SparkSession, ruInfo: Map[String, String], scheduleId: String, cdNm: String) = {
    makeSectorResult(spark, scheduleId, cdNm, ruInfo.getOrElse("SECTOR_PATH", ""));
    //makeRuResult(scheduleId, cdNm, ruInfo);

    if (cdNm == "LOS" || cdNm == "PATHLOSS") {
//      makeRuResult(scheduleId, cdNm, ruInfo);
    }
  }

  def initialArray(sumBinCnt: Int, initialValue: Array[Byte]) = {
    val iv = new Byte4(initialValue)
    val bin = Array.ofDim[Byte4](sumBinCnt)
    for (i <- 0 until sumBinCnt by 1) { bin(i) = iv }
    bin
  }

  // 섹터 Bin
  def makeSectorResult(spark: SparkSession, scheduleId: String, cdNm: String, sectorPath: String) = {

    logInfo(s"""makeSectorResult start ${scheduleId}""");

    var qry = "";
    // int    buildingIndex; // Building Index
    // char   cTBDKey[20];	 // TBD Key
    // UCha   xyz[4];			   // 0 : X Bin Count
    // 				               // 1 : Y Bin count
    //                       // 2 : Z floor
    //                       // 3 : Padding
    // float  startX, startY;	// Building Border letf bottom
    // ULLong	startPointBin;	// start point of BIN data

	  //int    bldCount;
	  //int    resolution;
	  //char*  bldHeader;	// BuildingHeader
	  //ULLong binCount;	// binData Count(do not save in file)
	  //char*  binData;
    //---------------------------------------------------
       logInfo(s"""[SEL] Header ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectResultNrBfScenHeader(scheduleId); logInfo(qry);
    val headerDF = spark.sql(qry).repartition(1); logInfo(qry); headerDF.cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER");

    //---------------------------------------------------
       logInfo(s"""[SEL] bldCount ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectBldCount(); logInfo(qry); var sqlDf = spark.sql(qry); var row = sqlDf.collect.last
    val bldCount : Int = row(0).asInstanceOf[Int]

    //---------------------------------------------------
       logInfo(s"""[SEL] resolution ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectResolution(scheduleId); logInfo(qry); rs = stat.executeQuery(qry);
    val resolution : Int = rs.getInt("RESOLUTION");

    //---------------------------------------------------
       logInfo(s"""[SEL] sumBinCnt ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectSumBinCnt(scheduleId); logInfo(qry); sqlDf = spark.sql(qry); row = sqlDf.collect.last
    val sumBinCnt : Long = row(0).asInstanceOf[Long]
    val sumBinCnt2 : Int = row(0).asInstanceOf[Int]

    //---------------------------------------------------
       logInfo(s"""[SEL] value ${scheduleId}""");
    //---------------------------------------------------
    val names = cdNm match {
      case "LOS"         => ("RESULT_NR_BF_LOS"        , "LOS"     )
      case "PATHLOSS"    => ("RESULT_NR_BF_PATHLOSS"   , "PATHLOSS")
      case "BEST_SERVER" => ("RESULT_NR_BF_BESTSERVER" , "RU_SEQ"  )
      case "PILOT_EC"    => ("RESULT_NR_BF_RSRP"       , "RSRP"    )
      case "RSSI"        => ("RESULT_NR_BF_RSSI"       , "RSSI"    )
      case "SINR"        => ("RESULT_NR_BF_SINR"       , "SINR"    )
    };
    val tabNm = names._1; val colNm = names._2;
    qry = MakeBfBinFileSql.selectSectorResult(scheduleId, tabNm, colNm);logInfo(qry); 
    val vDf = spark.sql(qry).repartition(1);
    
    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일 Write start ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    import org.apache.hadoop.fs.{ FileSystem, Path }
    val fs = FileSystem.get(new Configuration())
    var sectorPathFileNm = s"/user/icpap/result/bd/${DateUtil.getDate("yyyyMMdd")}/$sectorPath/${cdNm}.bin"
    val dos = fs.create(new Path(sectorPathFileNm))

    //val dos = new DataOutputStream(new FileOutputStream(sectorPathFileNm ));
    dos.writeInt(ByteUtil.swap(bldCount));                                   // bldCount       int        4
    dos.writeInt(ByteUtil.swap(resolution));                                 // resolution     int        4

    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일 Write Header ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------

    headerDF.foreachPartition { p =>
      p.foreach { row =>
        dos.writeInt(ByteUtil.swap(row(0).asInstanceOf[Int]));                // BUILDING_INDEX int        4
        dos.write(ByteUtil.toByte20(row(1).asInstanceOf[String]));            // TBD_KEY        char[20]  20
        dos.write(row(2).asInstanceOf[Int]);                                  // BinXCnt        uchar      1
        dos.write(row(3).asInstanceOf[Int]);                                  // BinYCnt        uchar      1
        dos.write(row(4).asInstanceOf[Int]);                                  // FloorZ         uchar      1
        dos.write(row(4).asInstanceOf[Int]);                                  // Padding        uchar      1
        dos.writeFloat(ByteUtil.swap(row(5).asInstanceOf[Float]));            // startX         float      4
        dos.writeFloat(ByteUtil.swap(row(6).asInstanceOf[Float]));            // startY         float      4
        dos.writeLong(row(7).asInstanceOf[Long]);                             // start point    ulong      8
        }
    }

    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일 Write binCount ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    dos.writeLong(sumBinCnt);                                                 // binCount        ulong     8

    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일 Write binData ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    val initialValue = cdNm match {
        case "PATHLOSS" => ByteUtil.floatMax()
        case _          => ByteUtil.intZero()
    }

    val bin = initialArray(sumBinCnt2 , initialValue)

    vDf.foreachPartition { p =>
      p.foreach { row =>
        bin(row(5).asInstanceOf[Int]).value = cdNm match {
          case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
          case _     => ByteUtil.floatToByteArray(row(5).asInstanceOf[Float])
        }
      }
    }

    bin.foreach { e =>
       dos.write(e.value)
    }
    dos.close()

    logInfo(s"""makeSectorResult end ${scheduleId}""");

  }

  // RU별 Bin
  def makeRuResult(spark: SparkSession,scheduleId: String, cdNm: String, ruInfo: mutable.Map[String, String]) = {

    logInfo("makeRuResult 01")

    val names = cdNm match {
      case "LOS" => ("RESULT_NR_BF_LOS_RU", "VALUE")
      case "PATHLOSS" => ("RESULT_NR_BF_PATHLOSS_RU", "PATHLOSS")
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

    logInfo("makeRuResult 02")

    //    val iCnt = ruInfo.size;

    // todo use jdbc
    def getRUBinCounts(scheduleId: String, ruId: (String, String)): (Int, Int) = {
      logInfo("makeRuResult 04 " + ruId._1)

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
        logInfo("makeRuResult 05 " + ruId._1)

        //var qry2 = MakeBinFileSql4.selectRuResult(scheduleId, tabNm, colNm, ruId._1);
        // todo add a column called RU_ID
        val qry = MakeBinFileSql.selectRuResult2(ruId._1)
        logInfo(qry)

        import spark.implicits._

        // ru_id 에 의한 파티션
        // 처음부터 파티션 돼 있는 것이 좋음
        val sqlDf2 = spark.sql(qry).repartition($"RU_ID")

        logInfo("makeRuResult 06 " + ruId._1)

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

        logInfo("makeRuResult 07 " + ruId._1)

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

        logInfo("makeRuResult 08 " + ruId._1)

        logInfo("=========================== RU별 Bin 생성 완료 ===========================")
        if (fos != null) fos.close()
      }
    }
  }


}