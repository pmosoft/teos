package com.sccomz.scala.job.spark.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement

object Los {

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
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();

    //var scheduleId = "8463233";

    var objNm = "RESULT_NR_2D_LOS"
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
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los,
--       case when sum(case when upper(RSLT.is_bld) = 'T' THEN 1 else 0 end) > 0 then 1 else 0 end as los, -- PLB Check Only
       max(AREA.schedule_id) as schedule_id
  from AREA, RESULT_NR_2D_LOS_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
-- and RSLT.ru_id = 1012253245
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
    println(qry); stat.execute(qry);
    
  }
  
}