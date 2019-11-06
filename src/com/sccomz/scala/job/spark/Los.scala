package com.sccomz.scala.job.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.sql.{ DataFrame, Row, SparkSession }
import com.sccomz.scala.load.LoadTable

/*
 * 설    명 :
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute("8459967");

 */

object Los {

var spark: SparkSession = null
var objNm = "LOS"

def execute(scheduleId:String) = {
  //------------------------------------------------------
  println(objNm + " 시작");
  //------------------------------------------------------
  spark = SparkSession.builder().appName("Los").getOrCreate();
  excuteSql(scheduleId);
}

def excuteSql(scheduleId:String) = {

//var scheduleId = "8460062"; 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/teos/warehouse/RESULT_NR_2D_LOS/schedule_id=${scheduleId}"""),true)
spark.sql(s"""ALTER TABLE I_RESULT_NR_2D_LOS DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""")    


//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------

var qry = ""; qry = s"""
insert into table I_RESULT_NR_2D_LOS partition (schedule_id=${scheduleId})
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los
  from (
        select a.scenario_id, b.schedule_id,
               a.tm_startx div a.resolution * a.resolution as tm_startx,
               a.tm_starty div a.resolution * a.resolution as tm_starty,
               a.tm_endx div a.resolution * a.resolution as tm_endx,
               a.tm_endy div a.resolution * a.resolution as tm_endy,
               a.resolution
          from SCENARIO a, SCHEDULE b
         where b.schedule_id = ${scheduleId}  
           and a.scenario_id = b.scenario_id
        ) AREA, 
       (select 5104573 as scenario_id, 8460062 as schedule_id, '' as ru_id,
               rx_tm_xpos, rx_tm_ypos, --value
               1 value
   from RESULT_NR_2D_LOS_RU_temp_8460062
         where (is_bld = 't' or is_bld = 'T')
       ) RSLT
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
