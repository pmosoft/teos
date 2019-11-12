package com.sccomz.scala.job.spark

import com.sccomz.scala.job.spark.eng.Los
import com.sccomz.scala.job.spark.eng.PathLoss
import com.sccomz.scala.job.spark.eng.RsrpPilot
import com.sccomz.scala.job.spark.eng.Rssi
import com.sccomz.scala.job.spark.eng.Rsrp
import com.sccomz.scala.job.spark.eng.Throughput
import com.sccomz.scala.job.spark.eng.BestServer
import com.sccomz.scala.job.spark.eng.Sinr

/*
 * 설    명 :
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.EngManager
EngManager.execute("8459967");

*/

object EngManager {

  def execute(scheduleId:String) = {
    Los.execute(scheduleId);
    //PathLoss.execute(scheduleId);
    //RsrpPilot.execute(scheduleId);
    //Rsrp.execute(scheduleId);
    //BestServer.execute(scheduleId);
    //Rssi.execute(scheduleId);
    //Sinr.execute(scheduleId);
    //Throughput.execute(scheduleId);
  }
  
  
}
