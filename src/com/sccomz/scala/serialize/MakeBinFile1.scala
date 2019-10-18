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
    while(rs.next()) {
      // 폴더
      var dir = new File("C:/Pony/Excel/result", rs.getString(2));
      if(!dir.exists()) dir.mkdirs();
      // 파일
      var dir2 = new File(dir, rs.getString(3));
      var fw = new FileWriter(dir2);
      var bw = new BufferedWriter(fw);
      if(!dir2.exists()) bw.flush();
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