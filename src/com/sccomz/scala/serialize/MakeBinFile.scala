package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.util.logging.Logger

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission

import org.apache.spark.sql.SparkSession
import org.apache.spark.internal.Logging
import scala.collection._
import scala.sys.process._
import scala.concurrent._
import ExecutionContext.Implicits.global

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.scala.comm.App
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil


/*

import com.sccomz.scala.serialize.MakeBinFile
MakeBinFile.executeEngResult(spark,"8463233");
MakeBinFile.makeEngSectorResult("8463233", "LOS","SYS/5113566");

spark2-submit --master yarn --jars /home/icpap/lib/ojdbc7.jar,/home/icpap/lib/hiveJdbc11.jar --class com.sccomz.scala.serialize.MakeBinFile /home/icpap/bin/teos1.jar 8463233


*/


object MakeBinFile extends Logging {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;

  // todo add app name
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName(this.getClass.getName).getOrCreate()

    val scheduleId = if (args.length < 1) "" else args(0);
    executeEngResult(spark, scheduleId);
    //executeEngResult("8460062");

    spark.stop();
    con.close();
  }

  // 2D Bin
  def executeEngResult(spark: SparkSession, scheduleId: String) = {
    // separate this function from the job
    val ruInfo = MakeFolder.makeResultPath(scheduleId);
    makeEngResult(spark, ruInfo, scheduleId, "LOS");
    makeEngResult(spark, ruInfo, scheduleId, "PATHLOSS");
    makeEngResult(spark, ruInfo, scheduleId, "BEST_SERVER");
    makeEngResult(spark, ruInfo, scheduleId, "PILOT_EC");       // RSRP
    makeEngResult(spark, ruInfo, scheduleId, "RSSI");
    makeEngResult(spark, ruInfo, scheduleId, "SINR");           // SINR
  }

  // 2D Bin 생성
  def makeEngResult(spark: SparkSession, ruInfo: Map[String, String], scheduleId: String, cdNm: String) = {
    var sectorPath = ruInfo.getOrElse("SECTOR_PATH", "");
    makeEngSectorResult(spark, scheduleId, cdNm, ruInfo.getOrElse("SECTOR_PATH", sectorPath));

    if (cdNm == "LOS" || cdNm == "PATHLOSS") {
      makeEngRuResult(spark, scheduleId, cdNm, ruInfo);
    }
  }
  def initialArray(x_bin_count: Int, y_bin_count: Int, initialValue: Array[Byte]) = {
    val iv = new Byte4(initialValue)
    val bin = Array.ofDim[Byte4](x_bin_count * y_bin_count)
    for (i <- 0 until x_bin_count * y_bin_count by 1) { bin(i) = iv }
    bin
  }

  def writeToHdfs(bin: Array[Byte4], path: String) {
      import org.apache.hadoop.fs.{ FileSystem, Path }
      val fs = FileSystem.get(new Configuration())
      val out = fs.create(new Path(path))

      bin.foreach { e =>
        out.write(e.value)

      }
      out.close()
  }

  // 2D 섹터 결과
  def makeEngSectorResult(spark: SparkSession, scheduleId: String, cdNm: String, sectorPath: String) = {

    var qry = "";
    //---------------------------------------------------
       logInfo(s"""[SEL] binCount ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBinFileSql.selectBinCnt(scheduleId); logInfo(qry); rs = stat.executeQuery(qry); rs.next();
    val binXCnt = rs.getInt("BIN_X_CNT"); val binYCnt = rs.getInt("BIN_Y_CNT");

    //---------------------------------------------------
       logInfo(s"""[SEL] value ${scheduleId}""");
    //---------------------------------------------------
    val names = cdNm match {
      case "LOS"         => ("RESULT_NR_2D_LOS_8463233" , "LOS"     )
      case "PATHLOSS"    => ("RESULT_NR_2D_PATHLOSS"    , "PATHLOSS")
      case "BEST_SERVER" => ("RESULT_NR_2D_BESTSERVER"  , "RU_SEQ"  )
      case "PILOT_EC"    => ("RESULT_NR_2D_RSRP"        , "RSRP"    )
      case "RSSI"        => ("RESULT_NR_2D_RSSI"        , "RSSI"    )
      case "SINR"        => ("RESULT_NR_2D_SINR"        , "SINR"    )
    }

    val tabNm = names._1;  val colNm = names._2
    
    qry = MakeBinFileSql.selectSectorResult(scheduleId, tabNm, colNm); logInfo(qry)
    val vDf = spark.sql(qry).repartition(1); //sqlDf.show();

    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일  write start ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    val initialValue = cdNm match {
        case "PATHLOSS" => ByteUtil.floatMax()
        case _          => ByteUtil.intZero()
    }

    vDf.foreachPartition { p =>
      println("partition start")

      var bin = initialArray(binXCnt, binYCnt, initialValue)

      p.foreach { row =>
        //val i = row(0).asInstanceOf[Int] * binYCnt + row(1).asInstanceOf[Int]
        //val i = row(1).asInstanceOf[Int] * binYCnt + row(0).asInstanceOf[Int]
        val i = row(1).asInstanceOf[Int] * binXCnt + row(0).asInstanceOf[Int] // y * xc + x
        //val i = (row(1).asInstanceOf[Int] * row(0).asInstanceOf[Int]) + row(0).asInstanceOf[Int]

        bin(i).value = colNm match {
          case "LOS" => ByteUtil.intToByteArray(row(2).asInstanceOf[Int])
          case _     => ByteUtil.floatToByteArray(row(2).asInstanceOf[Float])
        }
      }

      println("writing to file")

      var sectorPathFileNm = s"/user/icpap/result/${DateUtil.getDate("yyyyMMdd")}/$sectorPath/${cdNm}.bin"
      writeToHdfs(bin, sectorPathFileNm)

      println("partition end")
    }
  }

  // 2D RU별 결과
  def makeEngRuResult(spark: SparkSession, scheduleId: String, cdNm: String, ruInfo: Map[String, String]) = {

    var qry = "";
    //---------------------------------------------------
       logInfo(s"""[SEL] RU_VALUE ${scheduleId}""");
    //---------------------------------------------------
    val names = cdNm match {
      case "LOS"         => ("RESULT_NR_2D_LOS_RU"         , "VALUE"     )
      case "PATHLOSS"    => ("RESULT_NR_2D_PATHLOSS_RU"    , "PATHLOSS")
      case "BEST_SERVER" => ("RESULT_NR_2D_BESTSERVER_RU"  , "RU_SEQ"  )
      case "PILOT_EC"    => ("RESULT_NR_2D_RSRP_RU"        , "RSRP"    )
      case "RSSI"        => ("RESULT_NR_2D_RSSI_RU"        , "RSSI"    )
      case "SINR"        => ("RESULT_NR_2D_SINR_RU"        , "SINR"    )
    };

    val tabNm = names._1; val colNm = names._2;
    //spark.sql("DROP TABLE IF EXISTS ENG_RU");
    import spark.implicits._
    qry = MakeBinFileSql.selectRuResultAll(scheduleId, tabNm, colNm); logInfo(qry); val vDf = spark.sql(qry).repartition($"RU_ID");
    //qry = MakeBinFileSql.selectRuResultAll(scheduleId, tabNm, colNm); logInfo(qry); val vDf = spark.sql(qry).repartition(1);
    //vDF.cache.createOrReplaceTempView("RU_VALUE");

    ////---------------------------------------------------
    //   logInfo(s"""[SEL] RU_BIN_CNT ${scheduleId}""");
    ////---------------------------------------------------
    //def getRuBinCounts(scheduleId: String, ruId: (String, String)): (Int, Int) = {
    //  val qry = MakeBinFileSql.select2dRuBinCnt(scheduleId, ruId._1); logInfo(qry); val cntDf = spark.sql(qry)
    //  val row = cntDf.collect.last
    //  val x_bin_cnt = row(0).asInstanceOf[Int]
    //  val y_bin_cnt = row(1).asInstanceOf[Int]
    //  (x_bin_cnt, y_bin_cnt)
    //}

    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일  write start ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    //qry = MakeBinFileSql.selectAllRuResult2(); logInfo(qry);
    //import spark.implicits._
    //val sqlDf2 = spark.sql(qry).repartition($"RU_ID")

    val initialValue = cdNm match {
        case "PATHLOSS" => ByteUtil.floatMax()
        case _          => ByteUtil.intZero()
    }

    println("vDf.foreachPartition");

    vDf.foreachPartition { p =>
      println("partition start")

      // bin 초기화
      var ruId   : Option[String] = None
      var ruSize : Option[(Int, Int)] = None
      var bin    : Option[Array[Byte4]] = None
      var ruPath : Option[String] = None
      println("p.foreach");

      // bin 설정
      p.foreach { row =>
        // RU_ID, X_POINT, Y_POINT, VALUE
        val ru_id   = row(0).asInstanceOf[String] // RU_ID
        val x_point = row(3).asInstanceOf[Int]    // X_POINT
        val y_point = row(4).asInstanceOf[Int]    // Y_POINT
        val ru_path = row(6).asInstanceOf[String] // RU_PATH

        if (ruId.isEmpty) {
          ruId   = Some(ru_id)
          ruSize = Some((row(1).asInstanceOf[Int], row(2).asInstanceOf[Int])) // X_BIN_CNT, Y_BIN_CNT
          bin    = Some(initialArray(ruSize.get._1, ruSize.get._2, initialValue))
          ruPath = Some(ru_path)
        }

        println("bin set ru_id="+ru_id);

        try {
          bin.get(y_point * ruSize.get._2 + x_point).value = cdNm match {
            case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
            case _     => ByteUtil.floatToByteArray(row(5).asInstanceOf[Float])
          }
        } catch {
          case e: Exception => e.printStackTrace;println("ru_id="+ru_id+" : y="+y_point+" : x="+x_point+" : ruSize="+ruSize.get._2);
        }

        //bin.get(y_point * ruSize.get._2 + x_point).value = cdNm match {
        //  case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
        //  case _     => ByteUtil.floatToByteArray(row(5).asInstanceOf[Float])
        //}

      }

      // ruId.get
      var path = s"/user/icpap/result/${DateUtil.getDate("yyyyMMdd")}/${ruPath.get}/${cdNm}.bin"; println(path);
      writeToHdfs(bin.get, path)
      println("partition end")
    }
    
    var res1 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn1 /workspace/dn1_sshpass.sh").lineStream;
    var res2 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn2 /workspace/dn2_sshpass.sh").lineStream;
    var res3 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn3 /workspace/dn3_sshpass.sh").lineStream;
    var res4 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn4 /workspace/dn4_sshpass.sh").lineStream;
    var result1 = res1.last; var result2 = res2.last; var result3 = res3.last; var result4 = res4.last;
    println(result1); println(result2); println(result3); println(result4);
    
    var hd = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn4 /workspace/sh_dir/test_script.sh").lineStream;
    println(hd);

  }

  def extToLocal(): Unit = {

    //df -h | grep run | sed 's/%//' | awk '{ result += $5 } END { print result }'
    val df01 = "df -h | grep run | sed 's/%//' | awk '{ result += $5 } END { print result }'"
    val p01 = "'df -h'".run(); val f01 = Future(blocking(p01.exitValue()))
    val r01 = try {
      Await.result(f01, duration.Duration(60, "sec"))
    } catch { case _: TimeoutException => println("TIMEOUT!"); p01.destroy(); p01.exitValue() }

    //val contents = Process("df -h | grep run").lineStream
    val contents = Process("/root/cal_disk_use.sh").lineStream

  }





}
