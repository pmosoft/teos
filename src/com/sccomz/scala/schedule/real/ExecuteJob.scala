package com.sccomz.scala.schedule.real

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.lang.Runtime
import java.io.File
import java.io.ByteArrayInputStream

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import scala.sys.process._

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.oracle.ExtractOraManager
import com.sccomz.scala.etl.load.LoadPostManager
import com.sccomz.scala.etl.load.LoadHdfsManager
import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
import com.sccomz.scala.job.spark.EngManager

import com.amazonaws.services.simpleworkflow.flow.core.TryCatch
import com.sccomz.scala.serialize.MakeBinFile
import com.sccomz.scala.etl.cache.LosResultCache

/*

import com.sccomz.scala.schedule.real.ExecuteJob
ExecuteJob.executePostgreShell("8460064");
ExecuteJob.executeEtlOracleToHdfs("8460064");
ExecuteJob.execute("8460064");
ExecuteJob.execute("8460178");

ExecuteJob.executeEtlOracleToHdfs("8463235","SC051");

ExecuteJob.executeEtlOracleToHdfs("8460966","SC001");
ExecuteJob.executeEtlOracleToHdfs("8460968","SC001");
ExecuteJob.delStepLog("8460178");
ExecuteJob.executePostgreShell("8460178")

sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh 5113766

*/

object ExecuteJob {

  //val logger = LoggerFactory.getLogger("StatDailyBatch")
  var scheduleId = "";
  var scenarioId = "";

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";

  def main(args: Array[String]): Unit = {
    scheduleId = if (args.length < 2) "" else args(0);
    scenarioId = if (args.length < 2) "" else args(1);
    //---------------------------------------------------------------------------------------------
    println("ExecuteJob : scheduleId = " + scheduleId + " : scenarioId : " + scenarioId);
    //---------------------------------------------------------------------------------------------
    executePostgreShell(scenarioId);
    execute(scheduleId,scenarioId);
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

  def updScheduleProcessCd(scheduleId:String, processCd:String, processMsg:String) = {
    qry = ExecuteJobSql.updateScheduleProcessCd(scheduleId, processCd, processMsg); println(qry); stat.execute(qry);
  }

  def selMaxStep(scheduleId:String) = {
    qry = ExecuteJobSql.selectScheduleStep(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next(); rs.getString("MAX_TYPE_STEP_CD");
  }

  def execute(scheduleId:String, scenarioId:String): Unit = {

    updScheduleProcessCd(scheduleId,"20003","분석중");

    var typeStepCd = "01";
    delStepLog(scheduleId);

    typeStepCd="01"; insStepLog(scheduleId,typeStepCd);
    executeEtlOracleToHdfs(scheduleId,typeStepCd);
    executeEtlOracleToPostgre(scheduleId,typeStepCd);
    executeEtlHdfsToPostgre(scheduleId,typeStepCd);
    typeStepCd="02"; insStepLog(scheduleId,typeStepCd);
    var isLoof = true;
    while(isLoof) {
      typeStepCd=selMaxStep(scheduleId); println("typeStepCd="+typeStepCd);

      if(typeStepCd=="02") {executePostgreShell(scenarioId);}
      if(typeStepCd=="02") {executeEtlPostgreToHdfs(scheduleId,scenarioId);insStepLog(scheduleId,"03");}
      if(typeStepCd=="03") {executeSparkEngJob(scheduleId)     ;insStepLog(scheduleId,"04");}
      if(typeStepCd=="04") {executeSparkMakeBinFile(scheduleId);insStepLog(scheduleId,"05");}
      if(typeStepCd=="05") {isLoof=false;}
      updScheduleProcessCd(scheduleId,"20004","분석완료");
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
      LoadHdfsManager.oracleToHdfs(scheduleId, scenarioId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }
  }
  def executeEtlHdfsToPostgre(scheduleId:String, typeStepCd:String): Unit = {
    try {
      LosResultCache.execute(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId,typeStepCd);
    }
  }


  def executePostgreShell(scenarioId:String): Unit = {
    println("executePostgreShell start");
    //var res = Process(s"sh /home/icpap/sh/execPostgre.sh ${scheduleId}").lineStream;
    //var scheduleId = "8460064";
    //var scenarioId = "5104574";

    //var res = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh ${scenarioId}").lineStream;
    var res1 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn1 /workspace/dn1_sshpass.sh").lineStream;
    var res2 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn2 /workspace/dn2_sshpass.sh").lineStream;
    var res3 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn3 /workspace/dn3_sshpass.sh").lineStream;
    var res4 = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn4 /workspace/sh_dir/dn4_sshpass.sh").lineStream;
    var result1 = res1.last; var result2 = res2.last; var result3 = res3.last; var result4 = res4.last;
    println(result1); println(result2); println(result3); println(result4);
    
    var test = Process(s"sshpass -f /home/sshpasswd ssh -o StrictHostKeyChecking=no icpap@teos-cluster-dn4 /workspace/sh_dir/test_script.sh").lineStream;
    
    //var res = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 hadoop fs -df -h").lineStream;

    //var res = Process(s"ssh postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh ${scenarioId}").lineStream;
    //var res = Process(s"ssh icpap@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh ${scheduleId}").lineStream;
    //var res = Process(s"ssh icpap@teos-cluster-dn1 /home/icpap/sh//execPostgre.sh 111").lineStream;
    // sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 hadoop fs -df -h
    // res.waitFor()
    //ssh postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh 5113566
    //var logFile = new File(s"sh /home/icpap/sh/${scheduleId}_end_log.txt");
    var logFile = new File(s"/home/icpap/log/${scheduleId}_end_log.txt");

    //8463233	5113566

    //chmod 777 /gis01/bin/anal_los_job_dis.sh
    //var res = Process(s"sshpass postgres@teos-cluster-dn1 /gis01/bin/alos_job_dis.sh 11").lineStream;
    //sshpass postgres@teos-cluster-dn1 /gis01/bin/alos_job_dis.sh 11
    //sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 ls -al
    //sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 sh /gis01/bin/alos_job_dis.sh 11
    //sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 cat /gis01/bin/alos_job_dis.sh
    //var res = Process(s"sshpass postgres@teos-cluster-dn1 /gis01/bin/alos_job_dis.sh 11").lineStream;
    //sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh 5113766

    //while(!logFile.exists()){
    //  Thread.sleep(1000*1);
    //  println("ing...");
    //}

    println("executePostgreShell end");
  }

  def executeEtlPostgreToHdfs(scheduleId:String,scenarioId:String): Unit = {
    ExtractLoadPostManager.monitorJobDis(scheduleId,scenarioId);
  }



  def executeSparkEngJob(scheduleId:String): Unit = {
    EngManager.execute(scheduleId);
  }

  def executeSparkMakeBinFile(scheduleId:String): Unit = {
//     MakeBinFile.executeEngResult(scheduleId);
  }

}