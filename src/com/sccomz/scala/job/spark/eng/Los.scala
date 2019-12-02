package com.sccomz.scala.job.spark.eng

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement



/*
 * 설    명 :
 * 입    력 : SCHEDULE
           SCENARIO
           RESULT_NR_2D_LOS_RU
 * 출    력 : RESULT_NR_2D_LOS
 * 수정내역 :
 * 2019-11-01 | 박준용 | 최초작성

import com.sccomz.scala.job.spark.eng.Los2
Los2.execute("8463233");

spark.sql(s"""
SELECT RU_ID 
FROM   RESULT_NR_2D_LOS_RU 
WHERE SCHEDULE_ID = 8463233 
GROUP BY RU_ID
ORDER BY RU_ID
""").take(100).foreach(println);


spark.sql(s"""
SELECT RU_ID 
FROM   RESULT_NR_2D_LOS_RU
WHERE SCHEDULE_ID = 8463233 
""").take(100).foreach(println);


 */

object Los{

  
def main(args: Array[String]): Unit = {  
  var scheduleId = if (args.length < 2) "" else args(0);
  var queueNm    = if (args.length < 2) "" else args(1);
  execute(scheduleId,"lQueue");
}   

def execute(scheduleId:String,queueNm:String) = {
  
  val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("queue",queueNm).config("spark.sql.warehouse.dir","/TEOS/warehouse").enableHiveSupport().getOrCreate();
  executeSql(spark, scheduleId);
  spark.close();
  //executeSql(scheduleId);
}
 




def executeSql(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463233";
  
var objNm = "RESULT_NR_2D_LOS"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = "";
 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
import spark.implicits._
import spark.sql
qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; sql(qry);

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
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los
  from AREA, RESULT_NR_2D_LOS_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   --and RSLT.ru_id = 1011760123
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
println(qry); spark.sql(qry).take(100).foreach(println);



}

}
