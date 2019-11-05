package com.sccomz.scala.schedule.real

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import com.sccomz.scala.comm.App
import java.lang.Runtime
import scala.sys.process._
import java.io.ByteArrayInputStream
import com.sccomz.scala.etl.extract.oracle.ExtractOraManager
import com.sccomz.scala.etl.load.LoadPostManager
import com.sccomz.scala.etl.load.LoadHdfsManager
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch

/*

import com.sccomz.scala.schedule.real.ExecuteJob
ExecuteJob.execute("8460178");

 */

object ExecuteJob {

  //val logger = LoggerFactory.getLogger("StatDailyBatch")
  var scheduleId = "";

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";
  
  def main(args: Array[String]): Unit = {

    scheduleId = if (args.length < 1) "" else args(0);
    //---------------------------------------------------------------------------------------------
    println("ExecuteJob : " + scheduleId);
    //---------------------------------------------------------------------------------------------
    //execute();
  }

  def delStepLog(scheduleId:String) = { qry = ExecuteJobSql.deleteScheduleStepAll(scheduleId); println(qry); stat.execute(qry); }
  def insStepLog(scheduleId:String,typeStepCd:String) = { 
    var prevTypeStepCd = if(typeStepCd=="01") "00" else if(typeStepCd=="02") "01" else if(typeStepCd=="03") "02" else if(typeStepCd=="04") "03" else if(typeStepCd=="05") "04" else if(typeStepCd=="06") "05" else if(typeStepCd=="07") "06" else if(typeStepCd=="08") "07" else if(typeStepCd=="09") "08" else if(typeStepCd=="10") "09" else "00"
    qry = ExecuteJobSql.insertScheduleStep(scheduleId, typeStepCd); println(qry); stat.execute(qry);
    qry = ExecuteJobSql.updateScheduleStep(scheduleId, prevTypeStepCd, "정상"); println(qry); stat.execute(qry);
  }

  def updStepErrLog(scheduleId:String,typeStepCd:String) = { 
    qry = ExecuteJobSql.updateScheduleStep(scheduleId, typeStepCd, "오류"); println(qry); stat.execute(qry);
  }
  
  
  def selMaxStep(scheduleId:String) = { 
    qry = ExecuteJobSql.selectScheduleStep(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next(); rs.getString("MAX_TYPE_STEP_CD");
  }
  
  
  def execute(scheduleId:String): Unit = {
    var typeStepCd = "01";
    delStepLog(scheduleId);

    typeStepCd="01"; 
    executeEtlOracleToPostgre(scheduleId,typeStepCd); 
    executeEtlOracleToHdfs(scheduleId,typeStepCd);
    typeStepCd="02";   
    var isLoof = true; 
    while(isLoof) {
      typeStepCd=selMaxStep(scheduleId);
      if(typeStepCd=="02") {executePostgreShell();}
      if(typeStepCd=="02") {executeEtlPostgreToHdfs();}
      if(typeStepCd=="03") {executeSparkEngJob();}
      if(typeStepCd=="05") {executeSparkMakeBinFile();}
      if(typeStepCd=="06") {isLoof=false;}
      Thread.sleep(1000*3);
    }
  }
  
  def executeEtlOracleToPostgre(scheduleId:String, typeStepCd:String): Unit = {
    insStepLog(scheduleId,typeStepCd);
    
    try {
      ExtractOraManager.extractOracleToPostgreIns(scheduleId);
      LoadPostManager.oracleToPostgreAll(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }
  }
  def executeEtlOracleToHdfs(scheduleId:String, typeStepCd:String): Unit = {
    insStepLog(scheduleId,typeStepCd);

    try {
      ExtractOraManager.extractOracleToHadoopCsv(scheduleId);
      LoadHdfsManager.oracleToHdfs(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }    
  }

  def executePostgreShell(): Unit = {
    
  }

  def executeEtlPostgreToHdfs(): Unit = {
  }
  def executeSparkEngJob(): Unit = {
  }
  def executeSparkMakeBinFile(): Unit = {
  }  
  
  //def executePassLoss(scheduleId : String) = { PassLoss.executeReal(scheduleId) }

}