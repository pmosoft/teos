package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Statement

import com.sccomz.java.comm.util.BinValue
import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.scala.comm.App

object MakeBinFile1{

  def main(args: Array[String]): Unit = {    
//    makeResultDir("");
    makeResultFile("");
  }

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
  
  def makeResultFile(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry2=MakeBinFileSql2.test1("");
    var rs2 = stat.executeQuery(qry2);
    
    while(rs2.next()) {
      var x_bin_cnt = rs2.getInt("x_bin_cnt");
      var y_bin_cnt = rs2.getInt("y_bin_cnt");
      
      val binVal = new Array[Array[BinValue]](x_bin_cnt)(y_bin_cnt);
      
      for(i <- 0 until x_bin_cnt by 1) {
        for(j <- 0 until y_bin_cnt by 1) {
          binVal(i) = new BinValue(FileUtil.intMax());
        }
      }
    }
    
    var qry3 = MakeBinFileSql2.selectResultNr2dLos("");
    var rs3 = stat.executeQuery(qry3);

//    var bin = new ArrayList[Integer];
    var count = 0;
    var i = 0; var j = 0;
    var fu = new FileUtil;
    
    while(rs3.next()) {
      var x = rs3.getInt(1);
      var y = rs3.getInt(2);
      var value = rs3.getInt(3);
    }
   }

//  def makeResultFile(scheduleId:String) = {
//    Class.forName(App.dbDriverHive);
//    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
//    var stat:Statement=con.createStatement();
//    var qry=MakeBinFileSql2.test1("");
//    var rs2 = stat.executeQuery(qry);
//    var file = new File("C:/Pony/Excel/result", "los.bin");
//    var fos = new FileOutputStream(file);
//    var bin = new ArrayList[Integer];
//    var count = 0;
//    var i = 0; var j = 0;
//    var fu = new FileUtil;
//    while(rs2.next()) {
//      var x_bin_cnt = rs2.getInt("x_bin_cnt");
//      var y_bin_cnt = rs2.getInt("y_bin_cnt");
//      val binVal = new Array[BinValue](x_bin_cnt)(y_bin_cnt);
//      for(i <- 0 until x_bin_cnt by 1) {
//        for(j <- 0 until y_bin_cnt by 1) {
//          binVal[i][j] = new BinValue(FileUtil.intMax());
//        }
//      }
//      
//      
//    	var resultValue = rs2.getInt("LOS");
//    	bin.add(resultValue);
//    	count = count + 1;
//    	if(count == 4) {
//    		for(i <- 0 until bin.size() by 1) {
//    			fos.write(fu.intToByteArray(bin.get(i)));
//    		}
//    		count = 0;
//    		bin.clear();
//    	}
//    }
//    fos.close();
//  }

}