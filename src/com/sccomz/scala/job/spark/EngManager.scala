package com.sccomz.scala.job.spark

import com.sccomz.scala.job.spark.eng.Los
import com.sccomz.scala.job.spark.eng.PathLoss
/*
 * 설    명 :
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute("8459967");

 */

object EngManager {

  def execute(scheduleId:String) = {
    Los.execute(scheduleId);
    PathLoss.execute(scheduleId);
  }

}
