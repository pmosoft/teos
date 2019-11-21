package com.sccomz.scala.job.spark.bak.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement

/*
 * 설    명 :
 * 입    력 : RESULT_NR_2D_RSRPPILOT_RU
           SCENARIO
           SCHEDULE
 * 출    력 : RESULT_NR_2D_RSRP_RU
           RESULT_NR_2D_RSRP
 * 수정내역 :
 * 2019-11-01 | 박준용 | 최초작성

import com.sccomz.scala.job.spark.eng.Rsrp
Rsrp.execute("8463189");

 */

object Rsrp {

def main(args: Array[String]): Unit = {
  var scheduleId = if (args.length < 1) "" else args(0);
  execute(scheduleId);
}

def execute(scheduleId:String) = {
  executeSql(scheduleId);
  executeSql2(scheduleId);
}

def executeSql(scheduleId:String) = {
Class.forName(App.dbDriverHive);
var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
var stat:Statement=con.createStatement();
  
//var scheduleId = "8463233";
  
var objNm = "RESULT_NR_BF_RSRP_RU"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------

var qry=s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); stat.execute(qry);

//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
qry=s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry); stat.execute(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
WITH OVERLAB AS
(
select enb_id, cell_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, rz,
       case when sum(power(10., rsrppilot / 10.)) = 0. then -9999
            else 10. * log10 (sum(power(10., rsrppilot / 10.)))
        end as rsrppilot
  from RESULT_NR_BF_RSRPPILOT_RU
 where schedule_id = ${scheduleId}
 group by enb_id, cell_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, rz
 having count(*) > 1
)
insert into ${objNm} partition (schedule_id)
select a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       case when b.rsrppilot is not null then b.rsrppilot else a.rsrppilot end rsrp,
       a.schedule_id
  from (select * from RESULT_NR_BF_RSRPPILOT_RU where schedule_id = ${scheduleId}) a left outer join OVERLAB b
    on (a.enb_id = b.enb_id and a.cell_id = b.cell_id and a.tbd_key = b.tbd_key and a.rx_tm_xpos = b.rx_tm_xpos and a.rx_tm_ypos = b.rx_tm_ypos and a.rx_floorz = b.rx_floorz and a.rz = b.rz)
"""
println(qry); stat.execute(qry);
con.close();

}

def executeSql2(scheduleId:String) = {
Class.forName(App.dbDriverHive);
var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
var stat:Statement=con.createStatement();

  
var objNm = "RESULT_NR_BF_RSRP"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------

var qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); stat.execute(qry);

//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
qry = s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry);stat.execute(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
insert into ${objNm} partition (schedule_id)
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, 
       max(rsrp) as rsrp,
       max(schedule_id) as schedule_id
  from RESULT_NR_BF_RSRP_RU
 where schedule_id=${scheduleId}
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
println(qry); stat.execute(qry);
con.close();
}

}