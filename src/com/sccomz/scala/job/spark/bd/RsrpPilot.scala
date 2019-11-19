package com.sccomz.scala.job.spark.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import com.sccomz.scala.comm.App
import java.sql.DriverManager
import java.sql.Statement


/*
 * 설    명 :
 * 입    력 : SCHEDULE
           SCENARIO_NR_RU
           NRSECTORPARAMETER (#)
           MOBILE_PARAMETER 
           NRSYSTEM          (#)
           RESULT_NR_2D_PILOT_RU
           RESULT_NR_2D_PATHLOSS_RU
 * 출    력 : RESULT_NR_2D_RSRPPILOT_RU 
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.RsrpPilot
RsrpPilot.execute("8463189");

 */

object RsrpPilot {

  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    execute(scheduleId);
  }

  def execute(scheduleId: String) = {
    executeSql(scheduleId);
  }

  //  * * * Hive Query * * *
  def executeSql(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    //var scheduleId = "8463189";

    var objNm = "RESULT_NR_BF_RSRPPILOT_RU"
    //------------------------------------------------------
    println(objNm + " 시작");
    //------------------------------------------------------
    var qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
    //---------------------------------------------------
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""), true)
    qry = s"""set hive.exec.dynamic.partition.mode=nontrict"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("insert partition table");
    //---------------------------------------------------
    qry = s"""
WITH PARAM AS
(
SELECT b.scenario_id, a.schedule_id, b.ru_id, b.enb_id, b.sector_ord as cell_id,
       nvl(d.mobilegain, 0) - nvl(d.feederloss,0) - nvl(d.carloss,0) - nvl(d.buildingloss,0) - nvl(d.bodyloss,0) as all_loss,
       b.fade_margin as ru_fade_margin, b.feeder_loss as ru_feeder_loss,
       c.beammismatchmargin, c.losbeamformingloss, c.nlosbeamformingloss,
       e.number_of_sc_per_rb,
       (c.txpwrdbm + c.powercombininggain + c.antennagain) - (10. * log10(e.number_of_cc * e.rb_per_cc)) - b.correction_value as EIRPPerRB
  FROM SCHEDULE a, SCENARIO_NR_RU b, NRSECTORPARAMETER c, MOBILE_PARAMETER d, NRSYSTEM e
 WHERE a.schedule_id = ${scheduleId}
   AND a.scenario_id = b.scenario_id
   and b.scenario_id = c.scenario_id
   and b.ru_id = c.ru_id
   and a.scenario_id = d.scenario_id
   and a.scenario_id = e.scenario_id
),
ANTGAIN AS
(
select schedule_id,
       ru_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, rz,
       0 as antgain
  from RESULT_NR_BF_PATHLOSS_RU
 where schedule_id = ${scheduleId}
),
PLPRIME_temp AS
(
SELECT PATHLOSS.ru_id, PARAM.enb_id, PARAM.cell_id, PATHLOSS.tbd_key,
       PATHLOSS.rx_tm_xpos, PATHLOSS.rx_tm_ypos, PATHLOSS.rx_floorz, PATHLOSS.rz,
       PATHLOSS.los, PATHLOSS.pathloss,
--       0 as antenna_gain,
       PATHLOSS.pathloss -
       (
       0 --@@@ antenna gain
       + PARAM.all_loss
       - PARAM.ru_fade_margin
       - PARAM.ru_feeder_loss
       - PARAM.beammismatchmargin
       )
       + IF (PATHLOSS.los = 1, PARAM.losbeamformingloss, PARAM.nlosbeamformingloss) as pathlossprime,
       PARAM.EIRPPerRB,
       PARAM.number_of_sc_per_rb,
       PATHLOSS.schedule_id
  FROM RESULT_NR_BF_PATHLOSS_RU PATHLOSS, PARAM
 WHERE PATHLOSS.schedule_id = ${scheduleId}
   and PATHLOSS.schedule_id = PARAM.schedule_id
   AND PATHLOSS.ru_id = PARAM.ru_id
)
insert into ${objNm} partition (schedule_id)
select PLPRIME_temp.ru_id, PLPRIME_temp.enb_id, PLPRIME_temp.cell_id, PLPRIME_temp.tbd_key,
       PLPRIME_temp.rx_tm_xpos, PLPRIME_temp.rx_tm_ypos, PLPRIME_temp.rx_floorz, PLPRIME_temp.rz,
       PLPRIME_temp.los, PLPRIME_temp.pathloss,
       ANTGAIN.antgain, 
       PLPRIME_temp.pathloss - nvl(ANTGAIN.antgain,0) as pathlossprime,
       PLPRIME_temp.EIRPPerRB - 10. * log10(PLPRIME_temp.number_of_sc_per_rb) - (PLPRIME_temp.pathloss - nvl(ANTGAIN.antgain,0)) as RSRPPilot,
       PLPRIME_temp.schedule_id
  from PLPRIME_temp left outer join ANTGAIN
    on (PLPRIME_temp.rx_tm_xpos = ANTGAIN.rx_tm_xpos and PLPRIME_temp.rx_tm_ypos = ANTGAIN.rx_tm_ypos and PLPRIME_temp.rx_floorz = ANTGAIN.rx_floorz and PLPRIME_temp.ru_id = ANTGAIN.ru_id and PLPRIME_temp.tbd_key = ANTGAIN.tbd_key)
"""
    println(qry); stat.execute(qry);

  }

}
