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

/*
 
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

  def execute(): Unit = {
    executeEtlOracleToPostgre();
    executeEtlOracleToHdfs();
    
    var isLoof = true;
    var typeStepCd = "01";
    while(isLoof) {
      qry = ScheduleDaemonSql.selectSchedule10001(); println(qry);
      rs = stat.executeQuery(qry);rs.next();
      typeStepCd=rs.getString("TYPE_STEP_CD");
      if(typeStepCd=="02") {executePostgreShell();}
      if(typeStepCd=="02") {executeEtlPostgreToHdfs();}
      if(typeStepCd=="03") {executeSparkEngJob();}
      if(typeStepCd=="05") {executeSparkMakeBinFile();}
      if(typeStepCd=="06") {isLoof=false;}
      Thread.sleep(1000*3);
    }    
  }
  
  def executeEtlOracleToPostgre(): Unit = {
  }
  def executeEtlOracleToHdfs(): Unit = {
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