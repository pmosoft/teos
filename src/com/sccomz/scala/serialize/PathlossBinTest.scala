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
  
  val logger : Logger = Logger.getLogger(this.getClass.getName());

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
    count = count + 1;
    // 파일 삭제
    if (count == 1) {
      FileUtil.delFiles2(App.resultPath + "/20191028/SYS/");
      logger.info("Directory Drop Complete!!");
    }

    while(rs.next()) {
      // 폴더 생성
      var dir = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/SYS/" + rs.getString(1) + "/ENB_" + rs.getString(2) + "/PCI_" + rs.getString(3) + "_PORT_" + rs.getString(4) + "_" + rs.getString(5));
      if(!dir.exists()) dir.mkdirs();
      println(dir);
    };
  }

  // Bin 파일 생성 메소드
  def makeResultFile(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();

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
    
    logger.info("============================= 1차원 배열 변환 =============================");
    //----------------------------------------------------------------------------------------------------------------
    // 1차원 배열 변환
    //----------------------------------------------------------------------------------------------------------------
    var newBin = new Array[Byte4](bin.length * bin(0).length);
    
    for(x <- 0 until bin.length by 1) {
      for(y <- 0 until bin(x).length by 1) {
        // 2차원 배열의 원소를 1차원 배열의 원소로 이동
        newBin((x * bin(x).length) + y) = bin(x)(y);
      }
    }

    logger.info("============================== 파일 Write ==============================");
    //----------------------------------------------------------------------------------------------------------------
    // 파일 Write
    //----------------------------------------------------------------------------------------------------------------
    var file = new File("C:/Pony/Excel/result/Pathloss", "pathlossTest2.bin");
    var fos = new FileOutputStream(file);

//    for (y <- 0 until y_bin_cnt by 1) {
//      for (x <- 0 until x_bin_cnt by 1) {
//        fos.write(bin(x)(y).value);
//      }
//    }

    for (i <- 0 until newBin.length by 1) {
      fos.write(newBin(i).value);
    }

    logger.info("============================== Bin 파일 생성 완료 ==============================");
    rs2.close();
    if(fos != null) fos.close();
  }

}