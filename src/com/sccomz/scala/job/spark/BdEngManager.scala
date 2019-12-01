package com.sccomz.scala.job.spark

import com.sccomz.scala.job.spark.bd.Los
import com.sccomz.scala.job.spark.bd.PathLoss
import com.sccomz.scala.job.spark.bd.RsrpPilot
import com.sccomz.scala.job.spark.bd.Rssi
import com.sccomz.scala.job.spark.bd.Rsrp
import com.sccomz.scala.job.spark.bd.Throughput
import com.sccomz.scala.job.spark.bd.BestServer
import com.sccomz.scala.job.spark.bd.Sinr
import java.sql.DriverManager
import java.sql.ResultSet
import com.sccomz.scala.comm.App
import java.sql.Connection
import java.sql.Statement

/*
 * 설    명 :
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.EngManager
EngManager.execute("8459967");

*/

object BdEngManager {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";

  def execute(scheduleId:String) = {

    var jobWeight = 1;
    var queueNm = "lowQueue";

    qry = "SELECT JOB_WEIGHT FROM SCHEDULE_EXT WHERE SCHEDULE_ID=${scheduleId}"; println(qry);
    rs = stat.executeQuery(qry); rs.next(); jobWeight = rs.getInt("JOB_WEIGHT");
    if(jobWeight==1) queueNm = "lQueue";
    if(jobWeight==2) queueNm = "mQueue";
    if(jobWeight==3) queueNm = "hQueue";

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
