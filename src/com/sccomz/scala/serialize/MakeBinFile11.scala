package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Statement

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.scala.comm.App
import java.util.logging.Logger
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil
import org.apache.spark.sql.SparkSession

object MakeBinFile11 {
  val logger: Logger = Logger.getLogger(this.getClass.getName());
  var spark: SparkSession = null;

  def main(args: Array[String]): Unit = {
    sparkTest("");
  }
  
  def sparkTest(scheduleId: String) = {
    spark = SparkSession.builder().appName("MakeBinFile11").getOrCreate();
    var query = "SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT";
    val df = spark.sql(query);
    df.show();
  }
  
  // 폴더 생성 메소드
  def makeResultDir(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var qry = MakeBinFileSql2.selectScenarioNrRu("");
    var rs = stat.executeQuery(qry);
    var count = 0;
    count = count + 1;

    // 폴더 삭제
    if (count == 1) {
      FileUtil.delFiles2(App.resultPath + "/20191028/SYS/");
      logger.info("Directory Drop Complete!!");
    }

    while (rs.next()) {
      // 폴더 생성
      var dir = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/SYS/" + rs.getString(1) + "/ENB_" + rs.getString(2) + "/PCI_" + rs.getString(3) + "_PORT_" + rs.getString(4) + "_" + rs.getString(5));
      if (!dir.exists()) dir.mkdirs();
      println(dir);
    };
  }

  // Bin 파일 생성 메소드
  def makeResultFile(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var fu = new FileUtil;

    logger.info("========================== 초기화 ===========================");
    //---------------------------------------------------------------------------------------------------------
    // 초기화
    //---------------------------------------------------------------------------------------------------------
    var x_bin_cnt = 307; var y_bin_cnt = 301;
    val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt);

    for (y <- 0 until y_bin_cnt by 1) {
      for (x <- 0 until x_bin_cnt by 1) {
        bin(x)(y) = new Byte4(ByteUtil.intZero());
      }
    }

    logger.info("======================== Value 세팅 ========================");
    //---------------------------------------------------------------------------------------------------------
    // Value 세팅
    //---------------------------------------------------------------------------------------------------------
    var qry = MakeBinFileSql2.test1("");
    var rs2 = stat.executeQuery(qry);
    var x_point = 0; var y_point = 0; var los = 0;
    while (rs2.next()) {
      x_point = rs2.getInt("x_point");
      y_point = rs2.getInt("y_point");
      los = rs2.getInt("los");
      bin(x_point)(y_point).value = ByteUtil.intToByteArray(los);
    }

    logger.info("======================== 파일 Write ========================");
    //---------------------------------------------------------------------------------------------------------
    // 파일 Write
    //---------------------------------------------------------------------------------------------------------
    var file = new File("C:/Pony/Excel/result/LOS", "losTest.bin");
    var fos = new FileOutputStream(file);
    for (y <- 0 until y_bin_cnt by 1) {
      for (x <- 0 until x_bin_cnt by 1) {
        fos.write(bin(x)(y).value);
      }
    }
    logger.info("======================= Bin 생성 완료 =======================");
    rs2.close();
    if (fos != null) fos.close();
  }

}