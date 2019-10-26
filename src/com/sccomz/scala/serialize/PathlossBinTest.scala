package com.sccomz.scala.serialize

import java.sql.DriverManager
import java.sql.Statement
import java.io.File

import com.sccomz.scala.comm.App
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.java.comm.util.DateUtil
import java.util.logging.Logger
import java.io.FileOutputStream
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil

object PathlossBinTest {

  def main(args: Array[String]): Unit = {
//    makeResultDir("");
    makeResultFile("");
  }

  // 폴더 생성 메소드
  def makeResultDir(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=MakeBinFileSql2.selectScenarioNrRu("");
    var rs = stat.executeQuery(qry);
    var count = 0;

    while(rs.next()) {
      count = count + 1;
      // 파일 삭제
      if(count==1) {
        FileUtil.delFiles("C:/Pony/Excel/result", ".*");
      }
      // 폴더 생성
      var dir = new File("C:/Pony/Excel/result", DateUtil.getDate("yyyyMMdd") + "/SYS/" + rs.getString(1) + "/ENB_" + rs.getString(2) + "/PCI_" + rs.getString(3) + "_PORT_" + rs.getString(4) + "_" + rs.getString(5));
      if(!dir.exists()) dir.mkdirs();
      println(dir);
    };
  }

  // Bin 파일 생성 메소드
  def makeResultFile(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();

    val logger : Logger = Logger.getLogger(this.getClass.getName());

    logger.info("================================ 초기화 ================================");
    //----------------------------------------------------------------------------------------------------------------
    // 초기화
    //----------------------------------------------------------------------------------------------------------------
    var x_bin_cnt = 307; var y_bin_cnt = 301;
    val bin = Array.ofDim[Byte4](x_bin_cnt, y_bin_cnt);

    for (y <- 0 until y_bin_cnt by 1) {
      for (x <- 0 until x_bin_cnt by 1) {
        bin(x)(y) = new Byte4(ByteUtil.floatMax());
      }
    }

    logger.info("============================== Value 세팅 ==============================");
    //----------------------------------------------------------------------------------------------------------------
    // Value 세팅
    //----------------------------------------------------------------------------------------------------------------
    var query = MakeBinFileSql2.test2("");
    var rs2 = stat.executeQuery(query);
    var x_point = 0; var y_point = 0; var pathloss = 0.0f;

    while(rs2.next()) {
      x_point = rs2.getInt("x_point");
      y_point = rs2.getInt("y_point");
      pathloss = rs2.getFloat("pathloss");
      bin(x_point)(y_point).value = ByteUtil.floatToByteArray(pathloss);
    }

    logger.info("============================== 파일 Write ==============================");
    //----------------------------------------------------------------------------------------------------------------
    // 파일 Write
    //----------------------------------------------------------------------------------------------------------------
    var file = new File("C:/Pony/Excel/result", "pathlossTest.bin");
    var fos = new FileOutputStream(file);

    for(i <- 0 until x_bin_cnt by 1) {
      for(j <- 0 until y_bin_cnt by 1) {
        fos.write(bin(i)(j).value);
      }
    }

    logger.info("============================== Bin 파일 생성 완료 ==============================");
    rs2.close();
    if(fos != null) fos.close();
  }

}