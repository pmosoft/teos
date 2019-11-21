package com.sccomz.scala.job.spark.eng

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

/*
 * 설    명 :
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.BestServer
BestServer.execute("8463189");

 */

object BestServer2 {

  
def main(args: Array[String]): Unit = {
  var scheduleId = if (args.length < 1) "" else args(0);
  execute(scheduleId);
}   

def execute(scheduleId:String) = {
  val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/TEOS/warehouse").enableHiveSupport().getOrCreate();
  executeSql(spark, scheduleId);
  spark.close();
}
 

def executeSql(spark: SparkSession, scheduleId:String) = {
  
var objNm = "RESULT_NR_2D_BESTSERVER"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = "";
//var scheduleId = "8460062"; 
//var scheduleId = "8460970"; 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
import spark.implicits._
import spark.sql
qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); sql(qry);

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
),
RSLT as
(
SELECT a.scenario_id, a.schedule_id, a.rx_tm_xpos, a.rx_tm_ypos, b.ru_seq
  FROM
	(
	select scenario_id, schedule_id, rx_tm_xpos, rx_tm_ypos, ru_id
	  from
		(
		select scenario_id, schedule_id, rx_tm_xpos, rx_tm_ypos,
		       ru_id, rsrppilot,
		       row_number() over(partition by rx_tm_xpos, rx_tm_ypos order by rsrppilot desc) rsrppilot_ord
		  from RESULT_NR_2D_RSRPPILOT_RU
		 where schedule_id = ${scheduleId}
		) a
	 where a.rsrppilot_ord = 1
	) a,
	(
	SELECT a.schedule_id, b.ru_id, b.ru_seq
	  from SCHEDULE a, SCENARIO_NR_RU b
	 WHERE a.schedule_id = ${scheduleId}
	   AND a.scenario_id = b.scenario_id
	) b
where a.schedule_id = b.schedule_id
  and a.ru_id = b.ru_id
)
insert into ${objNm} partition (schedule_id=${scheduleId})
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,       
       max(RSLT.ru_seq) as ru_seq
  from AREA, RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

}