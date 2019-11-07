package com.sccomz.scala.schedule.batch

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
import com.sccomz.scala.job.spark.eng.Los

/*

import com.sccomz.scala.schedule.batch.BatchJob
BatchJob.execute("20191107");

 */

object BatchJob {

  //val logger = LoggerFactory.getLogger("StatDailyBatch")
  var workDt = "";

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";
  
  def main(args: Array[String]): Unit = {

    workDt = if (args.length < 1) "" else args(0);
    //---------------------------------------------------------------------------------------------
    println("BatchJob : " + workDt);
    //---------------------------------------------------------------------------------------------
    execute(workDt);
  }

  def execute(workDt:String): Unit = {
      executeFabaseEtlOracleToHdfs(workDt);
  }
  
  def executeFabaseEtlOracleToHdfs(workDt:String): Unit = {
      ExtractOraManager.extractOracleToHadoopCsvBatch(workDt);
      LoadHdfsManager.oracleToHdfsBatch(workDt);
  }

}