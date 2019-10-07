package com.sccomz.scala.schedule.control

import org.apache.spark.sql.SparkSession
import com.sccomz.scala.sql.PassLoss

/*
 
/user/teos/parquet/entity/SCHEDULE/SCHEDULE_8443705

 
 */

object RealJob {

  //val logger = LoggerFactory.getLogger("StatDailyBatch")
  val spark = SparkSession.builder().appName("RealJob").getOrCreate()
  var scheduleId = ""


  def main(args: Array[String]): Unit = {

    scheduleId = if (args.length < 1) "" else args(0)

    //---------------------------------------------------------------------------------------------
    println("RealJob : " + scheduleId);
    //---------------------------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    println("1. PassLoss");
    //-------------------------------------------------------------------------
    executePassLoss(scheduleId)

    //---------------------------------------------------------------------------------------------
    println("잡 종료");
    //---------------------------------------------------------------------------------------------
    spark.stop()
  }

  def executePassLoss(scheduleId : String) = { PassLoss.executeReal(scheduleId) }

}