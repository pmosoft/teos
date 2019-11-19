package com.sccomz.scala.job.spark.eng

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import com.sccomz.scala.comm.App
import java.sql.DriverManager
import java.sql.Statement

/*
 * 설    명 :
 * 입    력 : SCENARIO
           SCHEDULE
           NRSYSTEM
           MOBILE_PARAMETER
           RESULT_NR_2D_RSRPPILOT_RU
 * 출    력 : RESULT_NR_2D_RSSI_RU
           RESULT_NR_2D_RSSI
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.Rssi
Rssi.execute("8463189");

 */

object Rssi2 {

  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    execute(scheduleId);
  }

  def execute(scheduleId: String) = {
    executeSql(scheduleId);
    executeSql2(scheduleId);
  }

  //  * * * Hive Query(RU) * * *
  def executeSql(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement = con.createStatement();
    
    //var scheduleId = "8463189";

    var objNm = "RESULT_NR_2D_RSSI_RU"
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
    qry = s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("insert partition table");
    //---------------------------------------------------
    qry = s"""
    with NR_PARAMETER as -- 파라미터 정보
(
select a.scenario_id, b.schedule_id, c.ant_category,
       c.number_of_cc, c.number_of_sc_per_rb, c.rb_per_cc, c.bandwidth_per_cc, c.subcarrierspacing,
       d.noisefigure
  from SCENARIO a, SCHEDULE b, NRSYSTEM c, MOBILE_PARAMETER d
 where b.schedule_id = ${scheduleId}  
   and a.scenario_id = b.scenario_id
   and a.scenario_id = c.scenario_id
   and a.scenario_id = d.scenario_id
)
insert into ${objNm} partition (schedule_id)
select a.scenario_id, a.ru_id, a.enb_id, a.cell_id, a.rx_tm_xpos, a.rx_tm_ypos,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       if (RSSINoNoise = 0. , -9999, 10. * log10(RSSINoNoise)) as RSSINoNoise,  
       if ((RSSINoNoise + MobileNoiseFloor) = 0. , -9999, 10. * log10((RSSINoNoise + MobileNoiseFloor))) as RSSI,  
       a.schedule_id
  from
	(
	select a.scenario_id, a.ru_id, a.enb_id, a.cell_id, a.rx_tm_xpos, a.rx_tm_ypos,
	       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
	       power(10 ,
	       case when upper(b.ant_category) = 'COMMON' then
	                    a.rsrppilot
	            else    a.rsrppilot + 10. * log10(b.number_of_cc * b.number_of_sc_per_rb * b.rb_per_cc)
	        end / 10.) as RSSINoNoise, -- dRssidBm
	       power(10, 
	       case when upper(b.ant_category) = 'COMMON' then
	                    -174. + b.noisefigure + 10. * log10 ((b.subcarrierspacing / 1000.) * 1000000.)
	            else    -174. + b.noisefigure + 10. * log10 ((b.bandwidth_per_cc * b.number_of_cc) * 1000000.)
	        end / 10.)  as MobileNoiseFloor, -- m_dNowMilliWatt
	       a.schedule_id
	  from RESULT_NR_2D_RSRPPILOT_RU a, NR_PARAMETER b
	 where a.schedule_id = ${scheduleId}
	   and a.schedule_id = b.schedule_id
	) a
"""
    println(qry); stat.execute(qry);
  }

  //  * * * Hive Query * * *
  def executeSql2(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat:Statement = con.createStatement();
    //var scheduleId = "8463189";

    var objNm = "RESULT_NR_2D_RSSI"
    //------------------------------------------------------
    println(objNm + " 시작");
    //------------------------------------------------------
    var qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION(schedule_id=${scheduleId})"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
    //---------------------------------------------------
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""), true)
    qry = s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("insert partition table");
    //---------------------------------------------------
    qry = s"""
with AREA as
(
select a.scenario_id, b.schedule_id,
       a.tm_startx div a.resolution * a.resolution as tm_startx,
       a.tm_starty div a.resolution * a.resolution as tm_starty,
       a.tm_endx div a.resolution * a.resolution as tm_endx,
       a.tm_endy div a.resolution * a.resolution as tm_endy,
       a.resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
)
insert into ${objNm} partition (schedule_id)
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       if (sum((power(10, (RSSINoNoise)/10.0))) = 0. , -9999, 10. * log10(sum((power(10, (RSSINoNoise)/10.0))))) as RSSI,
       max(AREA.schedule_id) as schedule_id
  from AREA, RESULT_NR_2D_RSSI_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
    println(qry); stat.execute(qry);

  }


}
  
  
  