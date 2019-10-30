package com.sccomz.scala.schedule.real

/*
 
 */

object ExecuteJob {

  //val logger = LoggerFactory.getLogger("StatDailyBatch")
  var scheduleId = "";

  def main(args: Array[String]): Unit = {

    scheduleId = if (args.length < 1) "" else args(0);

    //---------------------------------------------------------------------------------------------
    println("ExecuteJob : " + scheduleId);
    
    //---------------------------------------------------------------------------------------------
    //execute();

  }

  def execute(): Unit = {
    executeEtlOracleToPostgre();
    executePostgreShell();
    executeEtlPostgreToHdfs();
    executeEtlOracleToHdfs();
    executeEngJob();
    executeMakeBinFile();
  }
  
  def executeEtlOracleToPostgre(): Unit = {
  }

  def executePostgreShell(): Unit = {
  }

  def executeEtlPostgreToHdfs(): Unit = {
  }
  
  def executeEtlOracleToHdfs(): Unit = {
  }

  def executeEngJob(): Unit = {
  }

  def executeMakeBinFile(): Unit = {
  }  
  
  //def executePassLoss(scheduleId : String) = { PassLoss.executeReal(scheduleId) }

}