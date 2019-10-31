package com.sccomz.scala.etl.extract.oracle

import java.io.File
import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScenarioSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScheduleSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraDuSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraRuSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraSiteSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraMobileParameterSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScenarioNrRuSql

/*
import com.sccomz.scala.etl.extract.oracle.ExtractOraManager
ExtractOraManager.extractOracleToHadoopCsv("8459967")

 * */
object ExtractOraManager {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    extractOracleToHadoopCsv("8459967");
    extractOracleToPostgreIns("8459967");
  }

  def extractOracleToPostgreIns(scheduleId:String): Unit = {
    var tabNm = ""; var qry = "";

    //--------------------------------------
        tabNm = "SCHEDULE"
    //--------------------------------------
    qry = ExtractOraScheduleSql.selectScheduleIns(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    var pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".sql" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    //--------------------------------------
        tabNm = "SCENARIO";
    //--------------------------------------
    qry = ExtractOraScenarioSql.selectScenarioIns(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".sql" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    //--------------------------------------
        tabNm = "MOBILE_PARAMETER";
    //--------------------------------------
    qry = ExtractOraMobileParameterSql.selectMobileParameterIns(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".sql" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    //--------------------------------------
        tabNm = "SCENARIO_NR_RU";
    //--------------------------------------
    qry = ExtractOraScenarioNrRuSql.selectScenarioNrRuIns(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".sql" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    
    
  }
  
  
  def extractOracleToHadoopCsv(scheduleId:String): Unit = {

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
    
    // //--------------------------------------
    //     tabNm = "DU"
    // //--------------------------------------
    // qry = ExtractOraDUSql.selectDUCsv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    // 
    // //--------------------------------------
    //     tabNm = "RU"
    // //--------------------------------------
    // qry = ExtractOraRUSql.selectRUCsv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    // 
    // //--------------------------------------
    //     tabNm = "SITE"
    // //--------------------------------------
    // qry = ExtractOraSiteSql.selectSiteCsv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

  }
  def extractOracleIns(): Unit = {}

  def extractPostgreCsv(): Unit = {}

}