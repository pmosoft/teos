package com.sccomz.scala.etl.extract.oracle

import java.io.File
import java.io.PrintWriter
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraDuSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraMobileParameterSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraRuSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScenarioNrAntennaSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScenarioNrRuSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScenarioSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraScheduleSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraSiteSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraFabaseSql
import com.sccomz.scala.etl.extract.oracle.sql.ExtractOraNruetrafficSql

/*
import com.sccomz.scala.etl.extract.oracle.ExtractOraManager
ExtractOraManager.extractOracleToHadoopCsv("8463233")

8463233	5113566

ExtractOraManager.extractOracleToHadoopCsv("8460064")


ExtractOraManager.extractOracleToHadoopCsv("8463189")



ExtractOraManager.extractOracleToHadoopCsvBatch("20191107")


ExtractOraManager.extractOracleToHadoopCsv("8460178")
ExtractOraManager.extractOracleToHadoopCsv("8460179")
ExtractOraManager.extractOracleToHadoopCsv("8460062")
ExtractOraManager.extractOracleToHadoopCsv("8460063")



 * */
object ExtractOraManager {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    extractOracleToPostgreIns("8463233");
    //extractOracleToHadoopCsv("8460064");
    //extractOracleToHadoopCsv("8460178");

    //extractOracleToPostgreIns("8460178");
    //extractOracleToPostgreIns("8460179");
    //extractOracleToPostgreIns("8460062");
    //extractOracleToPostgreIns("8460063");
    
    //8463233	5113566
    
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

     //--------------------------------------
         tabNm = "DU"
     //--------------------------------------
     qry = ExtractOraDuSql.selectDuCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "RU"
     //--------------------------------------
     qry = ExtractOraRuSql.selectRuCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "SITE"
     //--------------------------------------
     qry = ExtractOraSiteSql.selectSiteCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "SCENARIO_NR_RU"
     //--------------------------------------
     qry = ExtractOraScenarioNrRuSql.selectScenarioNrRuCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "SCENARIO_NR_ANTENNA"
     //--------------------------------------
     qry = ExtractOraScenarioNrAntennaSql.selectScenarioNrAntennaCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "MOBILE_PARAMETER"
     //--------------------------------------
     qry = ExtractOraMobileParameterSql.selectMobileParameterCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

     //--------------------------------------
         tabNm = "NRUETRAFFIC"
     //--------------------------------------
     qry = ExtractOraNruetrafficSql.selectNruetrafficCsv(scheduleId); println(qry);
     rs = stat.executeQuery(qry);
     pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
     while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
     
  }

  def extractOracleToHadoopCsvBatch(workDt:String): Unit = {

    var tabNm = ""; var qry = "";

    //--------------------------------------
        tabNm = "MOBILE_PARAMETER";
    //--------------------------------------
    qry = ExtractOraFabaseSql.selectFabaseCsv(); println(qry);
    rs = stat.executeQuery(qry);
    var pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+workDt+".dat" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    
  }  
  
}