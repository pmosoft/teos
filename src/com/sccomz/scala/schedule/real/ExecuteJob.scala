package com.sccomz.scala.schedule.real

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.lang.Runtime
import java.io.File
import java.io.ByteArrayInputStream
import java.util.logging.Logger

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

# ru 6
ExecuteJob.execute("8463246");
ps -ef | grep '5113972'


ExecuteJob.execute("8460178");
ps -ef | grep 5105173

ExecuteJob.execute("8463290");
ps -ef | grep '5115566'




ExecuteJob.executePostgreShell("8460064");
ExecuteJob.executeEtlOracleToHdfs("8460064");
ExecuteJob.execute("8460064");

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
  var typeCd = "";
  var typeStepCd = "01";

  
  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";

  def main(args: Array[String]): Unit = {
    scheduleId = if (args.length < 1) "" else args(0);
    //---------------------------------------------------------------------------------------------
    println("ExecuteJob : scheduleId = " + scheduleId + " : scenarioId : " + scenarioId);
    //---------------------------------------------------------------------------------------------
    execute(scheduleId);
  }

  def delStepLog(scheduleId:String) = { qry = ExecuteJobSql.deleteScheduleStepAll(scheduleId); println(qry); stat.execute(qry); }
  def insStepLog(scheduleId:String) = {
    var prevTypeStepCd = if(typeStepCd=="01") "00" else if(typeStepCd=="02") "01" else if(typeStepCd=="03") "02" else if(typeStepCd=="04") "03" else if(typeStepCd=="05") "04" else if(typeStepCd=="06") "05" else if(typeStepCd=="07") "06" else if(typeStepCd=="08") "07" else if(typeStepCd=="09") "08" else if(typeStepCd=="10") "09" else "00"
    qry = ExecuteJobSql.insertScheduleStep(scheduleId, typeStepCd); println(qry); stat.execute(qry);
    qry = ExecuteJobSql.updateScheduleStep(scheduleId, prevTypeStepCd, "정상"); println(qry); stat.execute(qry);
  }

  def updStepErrLog(scheduleId:String) = {
    qry = ExecuteJobSql.updateScheduleStep(scheduleId, typeStepCd, "오류"); println(qry); stat.execute(qry);
  }

  def updScheduleProcessCd(scheduleId:String, processCd:String, processMsg:String) = {
    qry = ExecuteJobSql.updateScheduleProcessCd(scheduleId, processCd, processMsg); println(qry); stat.execute(qry);
  }

  def selMaxStep(scheduleId:String) = {
    qry = ExecuteJobSql.selectScheduleStep(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next(); rs.getString("MAX_TYPE_STEP_CD");
  }

  def selectTypeCd(scheduleId:String) = {
    qry = ExecuteJobSql.selectScheduleInfo(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next(); rs.getString("TYPE_CD")
  }
  
  def selectScenarioId(scheduleId:String) = {
    qry = ExecuteJobSql.selectScheduleInfo(scheduleId); println(qry); rs = stat.executeQuery(qry); rs.next(); rs.getString("SCENARIO_ID")
  }
  
  def execute(scheduleId:String): Unit = {
    try {    
      scenarioId = selectScenarioId(scheduleId)
      typeCd = selectTypeCd(scheduleId)
      
      updScheduleProcessCd(scheduleId,"20003","분석중");
      
      delStepLog(scheduleId);
      
      println("01 : ETL OracleToHdfs 실행")
      
      typeStepCd = "01"; insStepLog(scheduleId); executeEtlOracleToHdfs(scheduleId);
      
      println("02 : ETL OracleToPostgre 실행")
      typeStepCd = "02"; insStepLog(scheduleId); executeEtlOracleToPostgre(scheduleId);
      //executeEtlHdfsToPostgre(scheduleId,typeStepCd);
      var isLoof = true;
      println("03 : Postgre shell 실행")
      typeStepCd="03"; insStepLog(scheduleId);
      while(isLoof) {
        typeStepCd=selMaxStep(scheduleId); println("typeStepCd="+typeStepCd)
        if(typeStepCd=="03") { 
          executePostgreShell(scenarioId)
          typeStepCd="04";println("04 : ETL Postgre to HDFS 실행");insStepLog(scheduleId)
        } else if(typeStepCd=="04") { 
          executeEtlPostgreToHdfs(scheduleId)
          typeStepCd = "05";println("05 : Spark Eng 실행");insStepLog(scheduleId) 
        } else if(typeStepCd=="05") {
          executeSparkEngJob(scheduleId);insStepLog(scheduleId)
          typeStepCd = "06";println("06 : MakeBin 실행");insStepLog(scheduleId) 
        } else if(typeStepCd=="06") {
          executeSparkMakeBinFile(scheduleId)
      
          updScheduleProcessCd(scheduleId,"20004","분석완료");
          isLoof=false
        }
        Thread.sleep(1000*3);
      }
    } catch {
      case ex: Exception => {updStepErrLog(scheduleId);println(ex);sys.exit()}
    }
  }

  def executeEtlOracleToPostgre(scheduleId:String): Unit = {
    try {
      ExtractOraManager.extractOracleToPostgreIns(scheduleId);
      LoadPostManager.oracleToPostgreAll(scheduleId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId);
    }
  }
  def executeEtlOracleToHdfs(scheduleId:String): Unit = {

    try {
      ExtractOraManager.extractOracleToHadoopCsv(scheduleId);
      LoadHdfsManager.oracleToHdfs(scheduleId, scenarioId);
    } catch {
      case _:Throwable=>updStepErrLog(scheduleId);
    }
  }
  def executeEtlHdfsToPostgre(scheduleId:String, typeStepCd:String): Unit = {
    try {
      LosResultCache.execute(scheduleId);
    } catch {
      case ex: Throwable => throw ex
    }
  }

  def executePostgreShell(scenarioId:String): Unit = {
    println("executePostgreShell start");
    var str = "";    
    //var res0 = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 Seq('pkill', '-9', '-ef', 'anal_los_job_dis.sh 5105173')").lineStream;
    
    //var res0 = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 pkill -9 -ef 'anal_los_job_dis.sh 5105173'").lineStream;
    //var res0 = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 pkill -9 -ef 5105173").lineStream;

    
    try {
      str = s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 pkill -9 -ef 'anal_los_job_dis.sh ${scenarioId}'";
      println(str); var res0 = Process(str).lineStream;
    } catch {
      case ex: Exception => println("")
    } 
    
    str = s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh ${scenarioId}";
    println(str); var res = Process(str).lineStream;
    
    // proc = Process(Seq("java", "-jar", appJar.toString), baseDir)
    
    //var res = Process(s"sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh 5105173").lineStream;
    //pkill -9 -ef 'anal_los_job_dis.sh 5105173'
    //gis01/bin/anal_los_job_dis.sh 5105173 
    // ps -ef | grep 'anal_los_job_dis.sh 5105173'
    // kill -9 `ps -ef | grep 'anal_los_job_dis.sh 5105173' | awk '{print $2}'`
    //sshpass -pteos ssh -o StrictHostKeyChecking=no postgres@teos-cluster-dn1 /gis01/bin/anal_los_job_dis.sh 5105173
    
    //var res = Process(s"sh /home/icpap/sh/execPostgre.sh ${scheduleId}").lineStream;
    //var scheduleId = "8460064";
    //var scenarioId = "5104574";

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

  def executeEtlPostgreToHdfs(scheduleId:String): Unit = {
    ExtractLoadPostManager.monitorJobDis(scheduleId,true);
  }


  def executeSparkEngJob(scheduleId:String): Unit = {
    EngManager.execute(scheduleId);
  }

  def executeSparkMakeBinFile(scheduleId:String): Unit = {
//     MakeBinFile.executeEngResult(scheduleId);
  }

}