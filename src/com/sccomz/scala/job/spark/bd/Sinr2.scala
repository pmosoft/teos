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
           RESULT_NR_2D_RSSI_RU
 * 출    력 : RESULT_NR_2D_SINR_RU
           RESULT_NR_2D_SINR 
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.Sinr
Sinr.execute("8463189");

 */

object Sinr2 {


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
  
var objNm = "RESULT_NR_BF_SINR_RU"
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
qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); sql(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
with NR_PARAMETER as -- 파라미터 정보
(
select a.scenario_id, b.schedule_id, c.ant_category,
       c.number_of_cc, c.number_of_sc_per_rb, c.rb_per_cc, c.bandwidth_per_cc, c.subcarrierspacing,
       c.dlintercell, c.dlintracell, c.dlcovlimitrsrp_yn, c.dlcoveragelimitrsrplos, c.dlcoveragelimitrsrp,
       c.diversitygainratio,
       d.noisefigure
  from SCENARIO a, SCHEDULE b, NRSYSTEM c, MOBILE_PARAMETER d
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
   and a.scenario_id = c.scenario_id
   and a.scenario_id = d.scenario_id
),
RU as -- RU_SEQ 정보
(
select a.scenario_id, b.schedule_id, a.ru_id, a.ru_seq
  from SCENARIO_NR_RU a, SCHEDULE b
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
),
ruRSSI as -- RU별 RSSI 정보
(
select b.scenario_id, a.schedule_id, a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss,
       a.antenna_gain, a.pathlossprime, a.rsrppilot, a.rssinonoise, a.rssi, c.ru_seq,
       b.ant_category, b.dlcovlimitrsrp_yn,
       power(10, 
       case when upper(b.ant_category) = 'COMMON' then
                    -174. + b.noisefigure + 10. * log10 ((b.subcarrierspacing / 1000.) * 1000000.)
            else    -174. + b.noisefigure + 10. * log10 ((b.bandwidth_per_cc * b.number_of_cc) * 1000000.)
        end / 10.) as m_dNowMilliWatt
  from RESULT_NR_BF_RSSI_RU a, NR_PARAMETER b, RU c
 where a.schedule_id = ${scheduleId}
   and a.schedule_id = b.schedule_id
   and a.schedule_id = c.schedule_id
   and a.ru_id = c.ru_id
),
ruSumRSSI as -- ENB_ID, CELL_ID, TBD_KEY, BIN 별 RSSI sum
(
SELECT a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       sum(power(10, a.RSSINoNoise / 10.) * b.diversitygainratio) as RSSINoNoiseSUMMilliWatt
  from RESULT_NR_BF_RSSI_RU a, NR_PARAMETER b
 where a.schedule_id = ${scheduleId}
   and a.schedule_id = b.schedule_id
 group by a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz
),
ScenRSSI as -- 시나리오 RSSI 정보
(
select schedule_id, tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, rssi
  from RESULT_NR_BF_RSSI
 where schedule_id = ${scheduleId}
),
ruRSSIResult as -- ruRSSI + ruSumRSSI 결과(RU별 결과)
(
select a.schedule_id, a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss,
       a.antenna_gain, a.pathlossprime, a.rsrppilot, a.rssinonoise, a.rssi, a.ru_seq,
       a.ant_category, a.dlcovlimitrsrp_yn,
       a.m_dNowMilliWatt,
       b.RSSINoNoiseSUMMilliWatt
  from ruRSSI a left outer join ruSumRSSI b
   on (a.enb_id = b.enb_id and a.cell_id = b.cell_id and a.tbd_key = b.tbd_key and a.rx_tm_xpos = b.rx_tm_xpos and a.rx_tm_ypos = b.rx_tm_ypos and a.rx_floorz = b.rx_floorz and a.rz = b.rz)
),
SINRtemp as
(
SELECT a.schedule_id, a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss,
       a.antenna_gain, a.pathlossprime, a.rsrppilot, a.rssinonoise, a.rssi, a.ru_seq,
       a.ant_category, a.dlcovlimitrsrp_yn,
       a.m_dNowMilliWatt,
       a.RSSINoNoiseSUMMilliWatt,
       a.rssi -
       if
       (
       ((b.dlintercell * (power(10, c.rssi / 10.) - a.m_dNowMilliWatt) - (b.dlintercell - b.dlintracell) * a.RSSINoNoiseSUMMilliWatt) + a.m_dNowMilliWatt) == 0,
       -9999,
       10. * log10(((b.dlintercell * (power(10, c.rssi / 10.) - a.m_dNowMilliWatt) - (b.dlintercell - b.dlintracell) * a.RSSINoNoiseSUMMilliWatt) + a.m_dNowMilliWatt))
       ) as fSINRdB,
       b.dlcoveragelimitrsrplos, b.dlcoveragelimitrsrp,
       c.rssi as scenrssi
  from ruRSSIResult a, NR_PARAMETER b, ScenRSSI c
 where a.schedule_id = ${scheduleId}
   and a.schedule_id = b.schedule_id
   and a.schedule_id = c.schedule_id
   and a.tbd_key = c.tbd_key
   and a.rx_tm_xpos = c.rx_tm_xpos
   and a.rx_tm_ypos = c.rx_tm_ypos
   and a.rx_floorz = c.rx_floorz
)
insert into ${objNm} partition (schedule_id=${scheduleId})
select a.ru_id, a.enb_id, a.cell_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz, a.rz,
       a.los, a.pathloss,
       a.antenna_gain, a.pathlossprime, a.rsrppilot, a.rssinonoise, a.rssi, a.ru_seq,
       a.scenrssi, b.rsrp,
       round(if (a.fSINRdB > 35., 35., a.fSINRdB),2) as SINR,
--       round(case when a.dlcovlimitrsrp_yn = 1 THEN
  --               case when b.rsrp <= if (a.los = 1, a.dlcoveragelimitrsrplos, a.dlcoveragelimitrsrp) then 340282346638528859811704183484516925440.
    --                  else if (a.fSINRdB > 35., 35., a.fSINRdB)
      --            end
        --    else if (a.fSINRdB > 35., 35., a.fSINRdB)
       -- end,2) as SINR,
       a.schedule_id
  from SINRtemp a, RESULT_NR_BF_RSRP_RU b
 where a.schedule_id = b.schedule_id
   and a.ru_id = b.ru_id
   and a.tbd_key = b.tbd_key
   and a.rx_tm_xpos = b.rx_tm_xpos
   and a.rx_tm_ypos = b.rx_tm_ypos
   and a.rx_floorz = b.rx_floorz
   and a.rz = b.rz
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

def executeSql2(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463189"; 
  
var objNm = "RESULT_NR_BF_SINR"
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
qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; println(qry); sql(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
with SINRtemp as
(
SELECT a.schedule_id, a.tbd_key, a.rx_tm_xpos, a.rx_tm_ypos, a.rx_floorz,
       case when b.ru_seq is null then null else a.sinr end as sinr
  from (select * from RESULT_NR_BF_SINR_RU where schedule_id = ${scheduleId}) a
       left outer join 
       (select * from RESULT_NR_BF_BESTSERVER where schedule_id = ${scheduleId}) b
    on (a.schedule_id = b.schedule_id and a.tbd_key = b.tbd_key and a.rx_tm_xpos = b.rx_tm_xpos and a.rx_tm_ypos = b.rx_tm_ypos and a.rx_floorz = b.rx_floorz and a.ru_seq = b.ru_seq)
)
insert into ${objNm} partition (schedule_id=${scheduleId})
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, 
       max(sinr) as SINR
  from SINRtemp RSLT
 where schedule_id=${scheduleId}
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

}