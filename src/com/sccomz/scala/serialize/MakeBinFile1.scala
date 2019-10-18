package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;

import com.sccomz.scala.comm.App
import shapeless.LowPriority.For
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.java.comm.util.DateUtil
import java.text.SimpleDateFormat


object MakeBinFile1 extends FileUtil{
  

  def main(args: Array[String]): Unit = {
    makeResultDir("");
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
      // 파일 생성
//      var dir2 = new File(dir, rs.getString(3));
//      var fw = new FileWriter(dir2);
//      var bw = new BufferedWriter(fw);
//      if(!dir2.exists()) bw.flush();
      println(dir);
    };
  }

  def makeResultFile(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=MakeBinFileSql2.selectScenarioNrRu("");
    var rs = stat.executeQuery(qry);
    while(rs.next()) {
    	var dir = new File("C:/Pony/Excel/result", rs.getString(2));
      if(!dir.exists()) dir.mkdirs();
      println(dir);
    };

  }

}