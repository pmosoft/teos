package com.sccomz.scala.etl.extract.post

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._

import com.sccomz.scala.schedule.control.sql.ScheduleDaemonSql
import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosAntGridDisSql

/*
import com.sccomz.scala.etl.extract.ExtractManager
ExtractManager.extractPostcleToHadoopCsv("8459967")

 * */
object ExtractPostManager {

  var con:Connection = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    extractPostToHadoopCsv("test");
  }

  def extractPostToHadoopCsv(scheduleId:String): Unit = {

    Class.forName(App.dbDriverPost);
    var tabNm = ""; var qry = "";

    //--------------------------------------
        tabNm = "LOS_ANT_GRID_DIS"
    //--------------------------------------
    qry = ExtractPostLosAntGridDisSql.selectLosAntGridDisCsv(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    var pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

  }
  def extractPostcleIns(): Unit = {}

  def extractPostgreCsv(): Unit = {}
  def extractPostgreIns(): Unit = {}

}