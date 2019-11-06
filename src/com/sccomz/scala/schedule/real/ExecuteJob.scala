package com.sccomz.scala.schedule.real

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.lang.Runtime
import java.io.ByteArrayInputStream

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import scala.sys.process._

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.oracle.ExtractOraManager
import com.sccomz.scala.etl.load.LoadPostManager
import com.sccomz.scala.etl.load.LoadHdfsManager
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch
import java.io.File
import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
import com.sccomz.scala.job.spark.Los

/*

import com.sccomz.scala.schedule.real.ExecuteJob
ExecuteJob.execute("8460178");

ExecuteJob.delStepLog("8460178");

ExecuteJob.executePostgreShell("8460178")

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

    typeStepCd="01"; insStepLog(scheduleId,typeStepCd);
    executeEtlOracleToPostgre(scheduleId,typeStepCd); 
    executeEtlOracleToHdfs(scheduleId,typeStepCd);
    typeStepCd="02"; insStepLog(scheduleId,typeStepCd);   
    var isLoof = true; 
    while(isLoof) {
      typeStepCd=selMaxStep(scheduleId); println("typeStepCd="+typeStepCd);
      if(typeStepCd=="02") {executePostgreShell(scheduleId);}
      if(typeStepCd=="02") {executeEtlPostgreToHdfs(scheduleId);insStepLog(scheduleId,"03");}
      if(typeStepCd=="03") {executeSparkEngJob(scheduleId)     ;insStepLog(scheduleId,"04");}
      if(typeStepCd=="04") {executeSparkMakeBinFile(scheduleId);insStepLog(scheduleId,"05");}
      if(typeStepCd=="05") {isLoof=false;}
      Thread.sleep(1000*3);
    }
  }
  
  def executeEtlOracleToPostgre(scheduleId:String, typeStepCd:String): Unit = {
    try {
      ExtractOraManager.extractOracleToPostgreIns(scheduleId);
      LoadPostManager.oracleToPostgreAll(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }
  }
  def executeEtlOracleToHdfs(scheduleId:String, typeStepCd:String): Unit = {

    try {
      ExtractOraManager.extractOracleToHadoopCsv(scheduleId);
      LoadHdfsManager.oracleToHdfs(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }    
  }

  def executePostgreShell(scheduleId:String): Unit = {
    println("executePostgreShell start");
    //var scheduleId = "8460062";
    var res = Process(s"sh /home/icpap/sh/execPostgre.sh ${scheduleId}").lineStream;
    
    //var logFile = new File(s"sh /home/icpap/sh/${scheduleId}_end_log.txt");
    var logFile = new File(s"/home/icpap/sh/${scheduleId}_end_log.txt");
    
    while(!logFile.exists()){
      Thread.sleep(1000*1);
      println("ing...");
    }

    println("executePostgreShell end");
    
  }

  def executeEtlPostgreToHdfs(scheduleId:String): Unit = {
    //ExtractLoadPostManager.monitorJobDis(scheduleId);  
  }
  
  def executeSparkEngJob(scheduleId:String): Unit = {
    Los.execute(scheduleId);
  }
  
  def executeSparkMakeBinFile(scheduleId:String): Unit = {
  }  
  
  
  //def executePassLoss(scheduleId : String) = { PassLoss.executeReal(scheduleId) }

}