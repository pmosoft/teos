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
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.BestServer
BestServer.execute("8463189");

 */

object BestServer {

  
def main(args: Array[String]): Unit = {  
  var scheduleId = if (args.length < 1) "" else args(0);
  execute(scheduleId);
}

def execute(scheduleId:String) = {
  executeSql(scheduleId);
}
 

def executeSql(scheduleId:String) = {
  Class.forName(App.dbDriverHive);
  var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
  var stat:Statement = con.createStatement();
  
var objNm = "RESULT_NR_BF_BESTSERVER"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); stat.execute(qry);
//var scheduleId = "8460062"; 
//var scheduleId = "8460970"; 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
qry = s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry); stat.execute(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------

qry = s"""
with RSLT as
(
SELECT a.schedule_id, a.ru_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, b.ru_seq
  FROM
    (
    select schedule_id, ru_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
      from
        (
        select schedule_id, ru_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz,
               rsrppilot,
               row_number() over(partition by rx_tm_xpos, rx_tm_ypos, rx_floorz order by rsrppilot desc) rsrppilot_ord
          from RESULT_NR_BF_RSRPPILOT_RU
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
insert into ${objNm} partition (schedule_id)
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, 
       max(RSLT.ru_seq) as ru_seq,
       max(schedule_id) as schedule_id
  from RSLT
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
println(qry); stat.execute(qry);

}

}
