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
  //val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/TEOS/warehouse").enableHiveSupport().getOrCreate();
  //executeSql(spark, scheduleId);
  //executeSql2(spark, scheduleId);
  //spark.close();
  
  executeSql(scheduleId);
  executeSql2(scheduleId);

  
}

def executeSql(scheduleId:String) = {
Class.forName(App.dbDriverHive);
var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
var stat:Statement=con.createStatement();
  
//var scheduleId = "8463233";
  
var objNm = "RESULT_NR_2D_RSRP_RU"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------

var qry=s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry);stat.execute(qry);

//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
qry=s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry);stat.execute(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
WITH OVERLAB AS
(
select enb_id, cell_id, rx_tm_xpos, rx_tm_ypos,
       case when sum(power(10., rsrppilot / 10.)) = 0. then -9999
            else 10. * log10 (sum(power(10., rsrppilot / 10.)))
        end as rsrppilot
  from RESULT_NR_2D_RSRPPILOT_RU
 where schedule_id = ${scheduleId}
group by enb_id, cell_id, rx_tm_xpos, rx_tm_ypos
 having count(*) > 1
)
insert into ${objNm} partition (schedule_id)
select a.scenario_id, a.ru_id, a.enb_id, a.cell_id, a.rx_tm_xpos, a.rx_tm_ypos,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       case when b.rsrppilot is not null then b.rsrppilot else a.rsrppilot end rsrp,
       a.schedule_id
  from (select * from RESULT_NR_2D_RSRPPILOT_RU where schedule_id = ${scheduleId}) a left outer join OVERLAB b
    on (a.enb_id = b.enb_id and a.cell_id = b.cell_id and a.rx_tm_xpos = b.rx_tm_xpos and a.rx_tm_ypos = b.rx_tm_ypos)
"""
println(qry); stat.execute(qry);
con.close();

}

def executeSql2(scheduleId:String) = {
Class.forName(App.dbDriverHive);
var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
var stat:Statement=con.createStatement();

  
var objNm = "RESULT_NR_2D_RSRP"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------

var qry=s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry);stat.execute(qry);

//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
qry=s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry);stat.execute(qry);

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
       max(rsrp) as rsrp,
       max(AREA.schedule_id) as schedule_id
  from AREA, RESULT_NR_2D_RSRP_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
println(qry); stat.execute(qry);
con.close();


}

}
