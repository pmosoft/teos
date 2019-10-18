package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;

import com.sccomz.scala.comm.App


object MakeBinFile {

  def main(args: Array[String]): Unit = {
    makeResultDir("");
  }

  def makeResultDir(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=MakeBinFileSql.selectScenarioNrRu("");
    var rs = stat.executeQuery(qry);
    while(rs.next()) {

      var dir = new File(rs.getString(1));
      if(!dir.exists()) dir.mkdirs();

      println(rs.getString(1));
    };
  }

  def makeResultFile(scheduleId:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=MakeBinFileSql.selectScenarioNrRu("");
    var rs = stat.executeQuery(qry);
    while(rs.next()) {

      var dir = new File(rs.getString(1));
      if(!dir.exists()) dir.mkdirs();

      println(rs.getString(1));
    };

  }

}