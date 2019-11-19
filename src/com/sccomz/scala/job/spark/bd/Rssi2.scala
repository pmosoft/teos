package com.sccomz.scala.job.spark.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

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

def execute(scheduleId:String) = {
  val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/TEOS/warehouse").enableHiveSupport().getOrCreate();
  executeSql(spark, scheduleId);
  executeSql2(spark, scheduleId);
  spark.close();
}

def executeSql(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463189"; 
  
var objNm = "RESULT_NR_BF_RSSI_RU"
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
insert into ${objNm} partition (schedule_id=${scheduleId})
select a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss, a.antenna_gain, a.pathlossprime, a.rsrppilot,
       if (RSSINoNoise = 0. , -9999, 10. * log10(RSSINoNoise)) as RSSINoNoise,  
       if ((RSSINoNoise + MobileNoiseFloor) = 0. , -9999, 10. * log10((RSSINoNoise + MobileNoiseFloor))) as RSSI,  
       a.schedule_id
  from
    (
    select a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
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
      from RESULT_NR_BF_RSRPPILOT_RU a, NR_PARAMETER b
     where a.schedule_id = ${scheduleId}
       and a.schedule_id = b.schedule_id
    ) a
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

def executeSql2(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463189"; 
  
var objNm = "RESULT_NR_BF_RSSI"
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
insert into ${objNm} partition (schedule_id=${scheduleId})
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz,
       if (sum((power(10, (RSSINoNoise)/10.0))) = 0. , -9999, 10. * log10(sum((power(10, (RSSINoNoise)/10.0))))) as RSSI
  from RESULT_NR_BF_RSSI_RU
 where schedule_id=${scheduleId}
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

}
  
  
  