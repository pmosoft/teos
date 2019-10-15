package com.sccomz.scala.etl.extract

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
import com.sccomz.scala.etl.extract.sql.ExtractOraScheduleSql
import com.sccomz.scala.etl.extract.sql.ExtractOraScenarioSql

/*
import com.sccomz.scala.etl.extract.ExtractManager
ExtractManager.extractOracleToHadoopCsv("8459967")

 * */
object ExtractOraManager {

  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    extractOracleToHadoopCsv("8459967");
  }

  def extractOracleToHadoopCsv(scheduleId:String): Unit = {

    Class.forName(App.dbDriverOra);
    var tabNm = ""; var qry = "";

    //--------------------------------------
        tabNm = "SCHEDULE"
    //--------------------------------------
    qry = ExtractOraScheduleSql.selectScheduleCsv(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    var pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    //--------------------------------------
        tabNm = "SCENARIO"
    //--------------------------------------
    qry = ExtractOraScenarioSql.selectScenarioCsv(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

  }
  def extractOracleIns(): Unit = {}

  def extractPostgreCsv(): Unit = {}
  def extractPostgreIns(): Unit = {}

}