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

    //val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).getOrCreate();
    val spark: SparkSession = SparkSession.builder().appName(this.getClass.getName).getOrCreate();

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

    println(s"""makeSectorResult start ${scheduleId}""");

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
       println(s"""[SEL] Header ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectResultNrBfScenHeader(scheduleId); println(qry);
    val headerDF = spark.sql(qry); println(qry); headerDF.cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER");

    //---------------------------------------------------
       println(s"""[SEL] bldCount ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectBldCount(); println(qry); var sqlDf = spark.sql(qry); var row = sqlDf.collect.last
    val bldCount : Int = row(0).asInstanceOf[Int]

    //---------------------------------------------------
       println(s"""[SEL] resolution ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectResolution(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next();
    val resolution : Int = rs.getInt("RESOLUTION");

    //---------------------------------------------------
       println(s"""[SEL] sumBinCnt ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectSumBinCnt(scheduleId); println(qry); sqlDf = spark.sql(qry); row = sqlDf.collect.last
    val sumBinCnt : Long = row(0).asInstanceOf[Long]
    val sumBinCnt2 : Int = row(1).asInstanceOf[Int]

    //---------------------------------------------------
       println(s"""[SEL] value ${scheduleId}""");
    //---------------------------------------------------
    val names = cdNm match {
      case "LOS"         => ("RESULT_NR_BF_LOS"        , "LOS"       )
      case "PATHLOSS"    => ("RESULT_NR_BF_PATHLOSS"   , "PATHLOSS"  )
      case "BESTSERVER"  => ("RESULT_NR_BF_BESTSERVER" , "BESTSERVER")
      case "RSRP"        => ("RESULT_NR_BF_RSRP"       , "RSRP"      )
      case "RSSI"        => ("RESULT_NR_BF_RSSI"       , "RSSI"      )
      case "SINR"        => ("RESULT_NR_BF_SINR"       , "SINR"      )
    };
    val tabNm = names._1; val colNm = names._2;
    qry = MakeBfBinFileSql.selectSectorResult(scheduleId, tabNm, colNm);println(qry); 
    val vDf = spark.sql(qry).repartition(1);
    
    //---------------------------------------------------------------------------------------------------------
       println(s"""파일 Write start ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    import org.apache.hadoop.fs.{ FileSystem, Path }
    val fs = FileSystem.get(new Configuration())
    var sectorPathFileNm = s"/user/icpap/result/bd/${DateUtil.getDate("yyyyMMdd")}/$sectorPath/${cdNm}.bin"
    val dos = fs.create(new Path(sectorPathFileNm))

    //val dos = new DataOutputStream(new FileOutputStream(sectorPathFileNm ));
    dos.writeInt(ByteUtil.swap(bldCount));                                   // bldCount       int        4
    dos.writeInt(ByteUtil.swap(resolution));                                 // resolution     int        4

    //---------------------------------------------------------------------------------------------------------
       println(s"""파일 Write Header ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------

    headerDF.collect.foreach { row =>
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
    
    //---------------------------------------------------------------------------------------------------------
       println(s"""파일 Write binCount ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    dos.writeLong(sumBinCnt);                                                 // binCount        ulong     8
    
    //---------------------------------------------------------------------------------------------------------
       println(s"""파일 Write binData ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    val initialValue = cdNm match {
        case "PATHLOSS" => ByteUtil.floatMax()
        case _          => ByteUtil.intZero()
    }
    
    val bin = initialArray(sumBinCnt2 , initialValue)
    
    vDf.foreachPartition { p =>
      p.foreach { row =>
        bin(row(7).asInstanceOf[Int]).value = cdNm match {
          case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
          case _     => ByteUtil.floatToByteArray(row(6).asInstanceOf[Float])
        }
      }
    }
    
    bin.foreach { e =>
       dos.write(e.value)
    }
    dos.close()

    println(s"""makeSectorResult end ${scheduleId}""");

  }

  // RU별 Bin
  def makeRuResult(spark: SparkSession,scheduleId: String, cdNm: String, ruInfo: mutable.Map[String, String]) = {

    println("makeRuResult 01")

    var qry = "";
    //---------------------------------------------------
       logInfo(s"""[SEL] RU_VALUE ${scheduleId}""");
    //---------------------------------------------------
    val names = cdNm match {
      case "LOS"         => ("RESULT_NR_BF_LOS_RU"         , "VALUE"   )
      case "PATHLOSS"    => ("RESULT_NR_BF_PATHLOSS_RU"    , "PATHLOSS")
      case "BEST_SERVER" => ("RESULT_NR_BF_BESTSERVER_RU"  , "RU_SEQ"  )
      case "PILOT_EC"    => ("RESULT_NR_BF_RSRP_RU"        , "RSRP"    )
      case "RSSI"        => ("RESULT_NR_BF_RSSI_RU"        , "RSSI"    )
      case "SINR"        => ("RESULT_NR_BF_SINR_RU"        , "SINR"    )
    };    

    val tabNm = names._1; val colNm = names._2;
    //spark.sql("DROP TABLE IF EXISTS ENG_RU");
    import spark.implicits._
    qry = MakeBfBinFileSql.selectResultNrBfRuHeader(scheduleId); logInfo(qry); val headerDF = spark.sql(qry).repartition($"RU_ID");
    headerDF.cache.createOrReplaceTempView("M_RESULT_NR_BF_RU_HEADER");
    
    //---------------------------------------------------
       println(s"""[SEL] resolution ${scheduleId}""");
    //---------------------------------------------------
    qry = MakeBfBinFileSql.selectBfResolution(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next();
    val resolution : Int = rs.getInt("RESOLUTION");
    
    //---------------------------------------------------------------------------------------------------------
       logInfo(s"""파일  write start ${scheduleId}""");
    //---------------------------------------------------------------------------------------------------------
    val initialValue = cdNm match {
      case "PATHLOSS" => ByteUtil.floatMax()
      case _          => ByteUtil.intZero()
    }

    headerDF.foreachPartition { p =>

      // bin 초기화
      var ruId   : Option[String] = None
      var bin    : Option[Array[Byte4]] = None
      var ruPath : Option[String] = None

      var bldCount : Int = 0
      var sumBinCnt : Long = 0 
      var sumBinCnt2 : Int = 0
      
      // bin 설정
      p.foreach { row =>
        val ru_id   = row(0).asInstanceOf[String] // RU_ID
        val ru_path = row(11).asInstanceOf[String] // RU_PATH

        if (ruId.isEmpty) {
          ruId   = Some(ru_id)
          //bin    = Some(initialArray(ruSize.get._1, ruSize.get._2, initialValue))
          ruPath = Some(ru_path)
          
          //---------------------------------------------------
             println(s"""[SEL] bldCount ${scheduleId}""");
          //---------------------------------------------------
          qry = MakeBfBinFileSql.selectBfBldCount(ru_id); println(qry); var sqlDf = spark.sql(qry); var row = sqlDf.collect.last
          bldCount = row(0).asInstanceOf[Int]
          
          //---------------------------------------------------
             println(s"""[SEL] sumBinCnt ${scheduleId}""");
          //---------------------------------------------------
          qry = MakeBfBinFileSql.selectBfSumBinCnt(ru_id); println(qry); sqlDf = spark.sql(qry); row = sqlDf.collect.last
          sumBinCnt  = row(0).asInstanceOf[Long]
          sumBinCnt2 = row(1).asInstanceOf[Int]
          
        }        

        //---------------------------------------------------------------------------------------------------------
           println(s"""파일 Write start ${scheduleId}""");
        //---------------------------------------------------------------------------------------------------------
        import org.apache.hadoop.fs.{ FileSystem, Path }
        val fs = FileSystem.get(new Configuration())
        var ruPathFileNm = s"/user/icpap/result/bd/${DateUtil.getDate("yyyyMMdd")}/${ru_path}/${cdNm}.bin"
        val dos = fs.create(new Path(ruPathFileNm))
            
        
        
      }      
    }   
  
        
//      // bin 초기화
//      var ruId   : Option[String] = None
//      var ruSize : Option[(Int, Int)] = None 
//      var bin    : Option[Array[Byte4]] = None
//      var ruPath : Option[String] = None
//      println("p.foreach");
//
//      // bin 설정
//      p.foreach { row =>
//        // RU_ID, X_POINT, Y_POINT, VALUE
//        val ru_id   = row(0).asInstanceOf[String] // RU_ID
//        val x_point = row(3).asInstanceOf[Int]    // X_POINT
//        val y_point = row(4).asInstanceOf[Int]    // Y_POINT
//        val ru_path = row(6).asInstanceOf[String] // RU_PATH
//
//        if (ruId.isEmpty) {
//          ruId   = Some(ru_id)
//          ruSize = Some((row(1).asInstanceOf[Int], row(2).asInstanceOf[Int])) // X_BIN_CNT, Y_BIN_CNT
//          //bin    = Some(initialArray(ruSize.get._1, ruSize.get._2, initialValue))
//          ruPath = Some(ru_path)
//        }
//
//        println("bin set ru_id="+ru_id);
//
//        try {
//          bin.get(y_point * ruSize.get._2 + x_point).value = cdNm match {
//            case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
//            case _     => ByteUtil.floatToByteArray(row(5).asInstanceOf[Float])
//          }
//        } catch {        
//          case e: Exception => e.printStackTrace;println("ru_id="+ru_id+" : y="+y_point+" : x="+x_point+" : ruSize="+ruSize.get._2);
//        }        
//        
//        //bin.get(y_point * ruSize.get._2 + x_point).value = cdNm match {
//        //  case "LOS" => ByteUtil.intToByteArray(row(5).asInstanceOf[Int])
//        //  case _     => ByteUtil.floatToByteArray(row(5).asInstanceOf[Float])
//        //}
//        
//      }
//
//      // ruId.get
//      var path = s"/user/icpap/result/${DateUtil.getDate("yyyyMMdd")}/${ruPath.get}/${cdNm}.bin"; println(path);
//      //writeToHdfs(bin.get, path)
//      println("partition end")
//    }

  }

}