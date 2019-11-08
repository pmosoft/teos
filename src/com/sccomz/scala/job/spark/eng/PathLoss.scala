package com.sccomz.scala.job.spark.eng

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

/*
 * 설    명 :
 * 입    력 : SCENARIO_NR_RU
           SCHEDULE
           MOBILE_PARAMETER
           SCENARIO_NR_RU_AVG_HEIGHT (#)
           SCENARIO
           FABASE
           RESULT_NR_2D_LOS_RU
 * 출    력 : RESULT_NR_2D_PATHLOSS_RU
 * 
 * 수정내역 :
 * 2019-11-01 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.eng.PathLoss
PathLoss.executeSql("8463189");

 */

object PathLoss {
  
def main(args: Array[String]): Unit = {  
  var scheduleId = if (args.length < 1) "" else args(0);
  execute(scheduleId);
}     

def execute(scheduleId:String) = {
  val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/teos/warehouse").enableHiveSupport().getOrCreate();
  executeSql(spark, scheduleId);
  spark.close();
}

def executeSql(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463189"; 
  
var objNm = "RESULT_NR_2D_PATHLOSS_RU"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = "";
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/teos/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
import spark.implicits._
import spark.sql
qry = s"""ALTER TABLE I_${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; sql(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
qry = s"""
with RU as
(
-- RU List + Mobile Height(rh) + RU Avg Height in range
select a.scenario_id, b.schedule_id, a.enb_id, a.pci, a.pci_port, a.ru_id,
       a.xposition as tx_tm_xpos, a.yposition as tx_tm_ypos, a.height as th, c.height as rh,
       a.resolution,
       if (d.avgbuildingheight < d.txtotalheight, 0, 1) is_umi_model, -- 0(UMA), 1(UMI)
       nvl(e.floorloss, 0) as floorloss
  from SCENARIO_NR_RU a, SCHEDULE b, MOBILE_PARAMETER c, SCENARIO_NR_RU_AVG_HEIGHT d, SCENARIO e
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
   and b.scenario_id = c.scenario_id
   and b.scenario_id = d.scenario_id
   and a.ru_id = d.ru_id
   and b.scenario_id = e.scenario_id
),
FREQ as
(
-- Frequency info
select b.schedule_id, c.downlinkfreq * 1000000. as fq, c.downlinkfreq as mfq, c.downlinkfreq / 1000. as gfq
  from SCENARIO a, SCHEDULE b, FABASE c
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
   and a.system_id = c.systemtype
   and a.fa_seq = c.fa_seq
),
LOS_PREPARE as
(
-- hBS : Tx actual antenna height(th)
-- hUT : Rx actual antenna height(rh)
-- hE  : effective environment height
select RES.scenario_id, RES.schedule_id, RES.ru_id,
--       RES.tx_tm_xpos, RES.tx_tm_ypos, RES.tz, RU.th,
       RU.tx_tm_xpos, RU.tx_tm_ypos, RES.tz, RU.th,
       RES.rx_tm_xpos, RES.rx_tm_ypos, RES.rz, RU.rh,
       RES.value,
       case when upper(RES.is_bld) = 'T' THEN RU.floorloss
            else 0
        end as PLB,
       RU.th as hBS, 
       case when RES.rz - (RES.tz - RU.th) < 1.5 then 1.5 else RES.rz - (RES.tz - RU.th) end as hUT,
       RU.is_umi_model,
       case when RU.is_umi_model = 1 then
			       1.
		    else
			       if 
			       (
			       		(RES.tz - RU.th) < 13. ,
			       		1. ,
			            (
			             1. /
			             (
				             1. +
				             power(((RES.tz - RU.th) - 13.) / 10. , 1.5)
				             +
				             case when sqrt(power(RU.tx_tm_xpos - RES.rx_tm_xpos, 2) + power(RU.tx_tm_ypos - RES.rx_tm_ypos, 2)) <= 18. then
				                           0.
				                  else 5. / 4. * power( sqrt(power(RU.tx_tm_xpos - RES.rx_tm_xpos, 2) + power(RU.tx_tm_ypos - RES.rx_tm_ypos, 2)) / 100. , 3) * exp(-1. * sqrt(power(RU.tx_tm_xpos - RES.rx_tm_xpos, 2) + power(RU.tx_tm_ypos - RES.rx_tm_ypos, 2)) / 150.)    
				              end
				            )
			            )
			       )
       end as hE		       
  from RESULT_NR_2D_LOS_RU RES, RU
 where RES.schedule_id = ${scheduleId}
   and RES.schedule_id = RU.schedule_id
   and RES.ru_id = RU.ru_id
),
LOS_BASE as
(
-- distBP : breaking pinint distance : 4 * (hBS - hE) * (hUT - hE) * fq[Hz] * (3.0 * 10^8 m/s)
-- dist2d : distance 2d(Tp and Rp)
-- dist3d : distance 3D
select LOS_PREPARE.scenario_id, LOS_PREPARE.schedule_id, LOS_PREPARE.ru_id,
       LOS_PREPARE.tx_tm_xpos, LOS_PREPARE.tx_tm_ypos, LOS_PREPARE.tz, LOS_PREPARE.th,
       LOS_PREPARE.rx_tm_xpos, LOS_PREPARE.rx_tm_ypos, LOS_PREPARE.rz, LOS_PREPARE.rh, LOS_PREPARE.value, LOS_PREPARE.PLB,
       LOS_PREPARE.hBS, LOS_PREPARE.hUT,
       LOS_PREPARE.is_umi_model,
       sqrt(power(LOS_PREPARE.tx_tm_xpos - LOS_PREPARE.rx_tm_xpos, 2) + power(LOS_PREPARE.tx_tm_ypos - LOS_PREPARE.rx_tm_ypos, 2)) as dist2d,
       sqrt(power(LOS_PREPARE.tx_tm_xpos - LOS_PREPARE.rx_tm_xpos, 2) + power(LOS_PREPARE.tx_tm_ypos - LOS_PREPARE.rx_tm_ypos, 2) + power((LOS_PREPARE.th - LOS_PREPARE.rh),2)) as dist3d,
       4. * (LOS_PREPARE.hBS - LOS_PREPARE.hE) * (LOS_PREPARE.hUT - LOS_PREPARE.hE) * FREQ.fq / (300000000.) as distBP,
       FREQ.fq, FREQ.mfq, FREQ.gfq
  from LOS_PREPARE, FREQ
 where LOS_PREPARE.schedule_id = ${scheduleId}
   and LOS_PREPARE.schedule_id = FREQ.schedule_id
),
LOS_temp as
(
select scenario_id, schedule_id, ru_id,
       rx_tm_xpos, rx_tm_ypos, rz, value, PLB,
       dist2d, dist3d, distBP,
       hBS, hUT,
       fq, mfq, gfq,
       is_umi_model,
       if
       (
       is_umi_model = 1,
       case when distBP <= dist2d and dist2d <= 5000. THEN
                     32.4 + 40. * log10(dist3d) + 20. * log10(gfq) - 9.5 * log10(1. * power(distBP,2) + 1. * power(hBS - hUT, 2))
            else     32.4 + 21. * log10(dist3d) + 20. * log10(gfq)
        end,
       case when distBP <= dist2d and dist2d <= 5000. THEN
                     28. + 40. * log10(dist3d) + 20. * log10(gfq) - 9. * log10(power(distBP,2) + power(hBS - hUT, 2))
            else     28. + 22. * log10(dist3d) + 20. * log10(gfq)
        end
       ) as PL_LOS_temp,
       if
       (
       is_umi_model = 1,
       case when hUT > 22.5 THEN
                     30.9 + (22.25 - 0.5 * log10(hUT)) * log10(dist3d) + 20. * log10(gfq)
            else 0
        end,
       case when hUT > 22.5 THEN
                     28. + 22. * log10(dist3d) + 20. * log10(gfq)
            else 0
        end
       ) as PL_LOS_AV
  from LOS_BASE
),
LOS as -- LOS라고 가정하고 분석
(
select scenario_id, schedule_id, ru_id,
       rx_tm_xpos, rx_tm_ypos, rz, value, PLB,
       dist2d, dist3d, distBP,
       hBS, hUT,
       fq, mfq, gfq,
       is_umi_model,
       if
       (
	       is_umi_model = 1,
	       case when PL_LOS_temp >= PL_LOS_AV THEN
	                     PL_LOS_temp + 4.
	            else     PL_LOS_AV + if (5. * exp(-0.01*hUT) > 2. , 5. * exp(-0.01*hUT) , 2.)
	        end,
	       case when hUT > 22.5 THEN
	                    PL_LOS_AV + 4.64 * exp(-0.0066*hUT)
	            else    PL_LOS_temp + 4.
	        end
       ) as PL_LOS,
       PL_LOS_temp
  from LOS_temp
),
NLOS_temp as
(
select scenario_id, schedule_id, ru_id,
       rx_tm_xpos, rx_tm_ypos, rz, value, PLB,
       dist2d, dist3d, distBP,
       hBS, hUT,
       fq, mfq, gfq,
       is_umi_model,
       PL_LOS,
       PL_LOS_temp,
       if 
       (
	       is_umi_model = 1,
	       case when dist2d <= 5000. then
	                     if ( PL_LOS > 35.3 * log10(dist3d) + 22.4 + 21.3 * log10(gfq) - 0.3 * (hUT - 1.5) , PL_LOS, 35.3 * log10(dist3d) + 22.4 + 21.3 * log10(gfq) - 0.3 * (hUT - 1.5) )
	            else 35.3 * log10(dist3d) + 22.4 + 21.3 * log10(gfq) - 0.3 * (hUT - 1.5)
	        end,
	       13.54 + 39.08 * log10(dist3d) + 20. * log10(gfq) - 0.6 * (hUT - 1.5)
       ) as PL_NLOS_temp,
       if 
       (
	       is_umi_model = 1,
	       case when hUT > 22.5 THEN
	                     32.4 + (43.2 - 7.6 * log10(hUT)) * log10(dist3d) + 20. * log10(gfq)
	            else 0
	        end,
	       -17.5 + (46. - 7. * log10(hUT)) * log10(dist3d) + 20. * log10( 40. * 3.14 * gfq / 3. ) 
       ) as PL_NLOS_AV
  from LOS
),
NLOS as  -- NLOS라고 가정하고 분석
(
select NLOS_temp.scenario_id, NLOS_temp.schedule_id, NLOS_temp.ru_id,
       NLOS_temp.rx_tm_xpos div b.resolution * b.resolution as rx_tm_xpos,
       NLOS_temp.rx_tm_ypos div b.resolution * b.resolution as rx_tm_ypos,
       NLOS_temp.rz, NLOS_temp.value, NLOS_temp.PLB,
       NLOS_temp.dist2d, NLOS_temp.dist3d, NLOS_temp.distBP,
       NLOS_temp.hBS, NLOS_temp.hUT,
       NLOS_temp.fq, NLOS_temp.mfq, NLOS_temp.gfq,
       NLOS_temp.is_umi_model,
       NLOS_temp.PL_LOS,
       if 
       (
       NLOS_temp.is_umi_model = 1,
       case when NLOS_temp.PL_NLOS_temp >= NLOS_temp.PL_NLOS_AV then
                     NLOS_temp.PL_NLOS_temp + 7.82
            else     NLOS_temp.PL_NLOS_AV + 8.
        end,
       case when NLOS_temp.hUT > 10. then
                     NLOS_temp.PL_NLOS_AV + 6.
            else
            (
                 if
                 (
	                 NLOS_temp.dist2d <= 5000. and NLOS_temp.PL_LOS_temp > NLOS_temp.PL_NLOS_temp,
	                 NLOS_temp.PL_LOS_temp + 6.,
	                 NLOS_temp.PL_NLOS_temp + 6.
                 )
            )
        end
       ) as PL_NLOS
  from NLOS_temp left outer join -- 좌표를 resolution단위로 변환을 위해서 JOIN
       (  
       select a.scenario_id, b.schedule_id, a.resolution
         from SCENARIO a, SCHEDULE b
        where b.schedule_id = ${scheduleId}
          and a.scenario_id = b.scenario_id
        limit 1
       ) b
       on  NLOS_temp.scenario_id = b.scenario_id
)
insert into I_${objNm} partition (schedule_id=${scheduleId})
select NLOS.scenario_id
     , NLOS.ru_id
     , NLOS.rx_tm_xpos
     , NLOS.rx_tm_ypos
     , NLOS.rz
     , NLOS.value
     ,(case when NLOS.value = 1 then PL_LOS else PL_NLOS end + PLB) as PATHLOSS
     , NLOS.is_umi_model
     , NLOS.dist2d
     , NLOS.dist3d
     , NLOS.distBP
     , NLOS.hBS
     , NLOS.hUT
  from NLOS
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}


}
