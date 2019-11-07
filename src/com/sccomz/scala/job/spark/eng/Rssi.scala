package com.sccomz.scala.job.spark.eng

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

/*
 * 설    명 :
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute("8459967");

 */

object Rssi {

var spark: SparkSession = null
var objNm  = "RESULT_NR_2D_RSSI_RU";
var objNm2 = "RESULT_NR_2D_RSSI";

def execute(scheduleId:String) = {
  //------------------------------------------------------
  println(objNm + " 시작");
  //------------------------------------------------------
  spark = SparkSession.builder().appName("Los").getOrCreate();
  excuteSql(scheduleId);
  excuteSql2(scheduleId);
}

def excuteSql(scheduleId:String) = {

var scheduleId = "8463189"; var objNm = "RESULT_NR_2D_RSSI_RU"
//var scheduleId = "8460062"; 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/teos/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
spark.sql(s"""ALTER TABLE I_${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""")    

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------

var qry = ""; qry = s"""
with NR_PARAMETER as -- 파라미터 정보
(
select a.scenario_id, b.schedule_id, c.ant_category,
       c.number_of_cc, c.number_of_sc_per_rb, c.rb_per_cc, c.bandwidth_per_cc, c.subcarrierspacing,
       d.noisefigure
  from SCENARIO a, SCHEDULE b, NRSYSTEM c, MOBILE_PARAMETER d
 where b.schedule_id = 8463189  
   and a.scenario_id = b.scenario_id
   and a.scenario_id = c.scenario_id
   and a.scenario_id = d.scenario_id
)
insert into I_${objNm} partition (schedule_id=${scheduleId})
select a.scenario_id, a.ru_id, a.enb_id, a.cell_id, a.rx_tm_xpos, a.rx_tm_ypos,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       if (RSSINoNoise = 0. , -9999, 10. * log10(RSSINoNoise)) as RSSINoNoise,  
       if ((RSSINoNoise + MobileNoiseFloor) = 0. , -9999, 10. * log10((RSSINoNoise + MobileNoiseFloor))) as RSSI
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
	 where a.schedule_id = 8463189
	   and a.schedule_id = b.schedule_id
	) a
"""
//--------------------------------------
println(qry);
//--------------------------------------
spark.sql(qry).take(100).foreach(println);


}


def excuteSql2(scheduleId:String) = {
  
var scheduleId = "8463189"; var objNm2 = "RESULT_NR_2D_RSSI"
//var scheduleId = "8460062"; 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/teos/warehouse/${objNm2}/schedule_id=${scheduleId}"""),true)
spark.sql(s"""ALTER TABLE I_${objNm2} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""")    

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------

var qry = ""; qry = s"""
with AREA as
(
select a.scenario_id, b.schedule_id,
       a.tm_startx div a.resolution * a.resolution as tm_startx,
       a.tm_starty div a.resolution * a.resolution as tm_starty,
       a.tm_endx div a.resolution * a.resolution as tm_endx,
       a.tm_endy div a.resolution * a.resolution as tm_endy,
       a.resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = 8463189
   and a.scenario_id = b.scenario_id
)
insert into I_${objNm2} partition (schedule_id=${scheduleId})
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       if (sum((power(10, (RSSINoNoise)/10.0))) = 0. , -9999, 10. * log10(sum((power(10, (RSSINoNoise)/10.0))))) as RSSI
  from AREA, RESULT_NR_2D_RSSI_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
//--------------------------------------
println(qry);
//--------------------------------------
spark.sql(qry).take(100).foreach(println);
  
  
}

}
