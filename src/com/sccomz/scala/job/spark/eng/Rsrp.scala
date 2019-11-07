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

object Rsrp {

var spark: SparkSession = null
var objNm  = "RESULT_NR_2D_RSRP_RU";
var objNm2 = "RESULT_NR_2D_RSRP";

def execute(scheduleId:String) = {
  //------------------------------------------------------
  println(objNm + " 시작");
  //------------------------------------------------------
  spark = SparkSession.builder().appName("Los").getOrCreate();
  excuteSql(scheduleId);
  excuteSql2(scheduleId);
}

def excuteSql(scheduleId:String) = {

var scheduleId = "8463189"; var objNm = "RESULT_NR_2D_RSRP_RU"
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
WITH OVERLAB AS
(
select enb_id, cell_id, rx_tm_xpos, rx_tm_ypos,
       case when sum(power(10., rsrppilot / 10.)) = 0. then -9999
            else 10. * log10 (sum(power(10., rsrppilot / 10.)))
        end as rsrppilot
  from RESULT_NR_2D_RSRPPILOT_RU
 where schedule_id = 8463189
 group by enb_id, cell_id, rx_tm_xpos, rx_tm_ypos
 having count(*) > 1
)
insert into I_RESULT_NR_2D_RSRP_RU partition (schedule_id=${scheduleId})
select a.scenario_id, a.ru_id, a.enb_id, a.cell_id, a.rx_tm_xpos, a.rx_tm_ypos,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       case when b.rsrppilot is not null then b.rsrppilot else a.rsrppilot end rsrp
  from (select * from RESULT_NR_2D_RSRPPILOT_RU where schedule_id = 8463189) a left outer join OVERLAB b
    on (a.enb_id = b.enb_id and a.cell_id = b.cell_id and a.rx_tm_xpos = b.rx_tm_xpos and a.rx_tm_ypos = b.rx_tm_ypos)
"""
//--------------------------------------
println(qry);
//--------------------------------------
spark.sql(qry).take(100).foreach(println);


}


def excuteSql2(scheduleId:String) = {
  
var scheduleId = "8463189"; var objNm2 = "RESULT_NR_2D_RSRP"
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
insert into I_RESULT_NR_2D_RSRP partition (schedule_id=${scheduleId})
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,       
       max(rsrp) as rsrp
  from AREA, RESULT_NR_2D_RSRP_RU RSLT
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
