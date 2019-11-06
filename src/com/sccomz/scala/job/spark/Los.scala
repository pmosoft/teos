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
 * 입    력 :
           SCHEDULE
           SCENARIO

 * 출    력 : Los
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute("8459967");

 */

object Los {

var spark: SparkSession = null
var objNm = "LOS"
val conf = new Configuration()
val fs = FileSystem.get(conf)

def execute(scheduleId:String) = {
  //------------------------------------------------------
  println(objNm + " 시작");
  //------------------------------------------------------
  spark = SparkSession.builder().appName("Los").getOrCreate();
  loadTables(); 
  excuteSql(scheduleId);
}

def loadTables() = { 
  //LoadTable.lodTable(spark,"SCHEDULE",scheduleId,"*","",true)
  //LoadTable.lodTable(spark,"SCENARIO",scheduleId,"*","",true)
}

def excuteSql(scheduleId:String) = {
  //var scheduleId = "8460062"
  
  //--------------------------------------
      println("target 파일 삭제");
  //--------------------------------------
  fs.delete(new Path(s"""/user/hive/warehouse/result_nr_2d_los/schedule_id=${scheduleId}"""),true)
  spark.sql(s"""ALTER TABLE I_RESULT_NR_2D_LOS DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""")    
    
var qry = ""
qry = s"""
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
         where b.schedule_id = 8460062  
           and a.scenario_id = b.scenario_id
        ) AREA, 
       (select 5104573 as scenario_id, 8460062 as schedule_id, '' as ru_id,
               rx_tm_xpos, rx_tm_ypos, --value
               1 value
 --from PATHLOSSPLB_temp               
 --            from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
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
//val sqlDf = spark.sql(qry)
//sqlDf.cache.createOrReplaceTempView(objNm); sqlDf.count()

}

def saveToParqeut() {}

def test01(){
var qry = s"""

--------------------------------------------------------------------------------------------------------------------------
-- 1. LOS RU Data Check (from POSTGIS to Hive)
--------------------------------------------------------------------------------------------------------------------------
select * from RESULT_NR_2D_LOS_RU;

-- Legacy에서 추출한 LOS RU (152RU Radial UI TEST)
select * from RESULT_NR_2D_LOS_RU_temp_8460855;

select rx_tm_xpos div 10 * 10 as xpos, rx_tm_ypos div 10 * 10 as ypos, a.*
from RESULT_NR_2D_LOS_RU_temp_8460855 a
;

-- Legacy에서 추출한 LOS RU (152RU BINtoBIN)
select * from RESULT_NR_2D_LOS_RU_temp_8460853;

-- Legacy에서 추출한 LOS RU (1350RU Radial)
select count(*) from RESULT_NR_2D_LOS_RU_temp_8460970;
-- 77218266

select * from RESULT_NR_2D_LOS_RU;

select schedule_id, count(*) from RESULT_NR_2D_LOS_RU
group by schedule_id
;
8460966	40138
8460853	7515983
8460855	7529899
8460970	77218266
8460062	10744360
8460964	69150
;

select ru_id, count(*) from RESULT_NR_2D_LOS_RU where schedule_id=8460062
group by ru_id
;

select * from RESULT_NR_2D_LOS_RU where schedule_id=8460062
;

select * from scenario_nr_ru
;

SELECT * FROM scenario;

select * from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295;


select ru_id,value,count(*) from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
group by ru_id,value
;


--------------------------------------------------------------------------------------------------------------------------
-- 2. LOS Analyze by Scenario Area
--------------------------------------------------------------------------------------------------------------------------
alter table RESULT_NR_2D_LOS drop partition(schedule_id=8460062);
--alter table RESULT_NR_2D_LOS drop partition(schedule_id=8460970);

set hive.exec.dynamic.partition.mode=nonstrict;

with AREA as
(
select a.scenario_id, b.schedule_id,
       a.tm_startx div a.resolution * a.resolution as tm_startx,
       a.tm_starty div a.resolution * a.resolution as tm_starty,
       a.tm_endx div a.resolution * a.resolution as tm_endx,
       a.tm_endy div a.resolution * a.resolution as tm_endy,
       a.resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = 8460062  
-- where b.schedule_id = 8460970
   and a.scenario_id = b.scenario_id
)
insert into RESULT_NR_2D_LOS partition (schedule_id)
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los,
       max(AREA.schedule_id) as schedule_id
  from AREA, RESULT_NR_2D_LOS_RU RSLT
--  from AREA, 
--       (select 5104573 as scenario_id, 8460062 as schedule_id, ru_id,
--               rx_tm_xpos, rx_tm_ypos, --value
--               if (value = 'f', 0 ,1) as value
--      --   from RESULT_NR_2D_LOS_RU_temp_8460855 -- 창원시청 (Radial 152개 RU UI TEST)
------         from RESULT_NR_2D_LOS_RU_temp_8460853 -- 창원시청 (BINtoBIN 152개 RU)     
--  --  --     from RESULT_NR_2D_LOS_RU_temp_8460970 -- 강남구 (Radial 1350개 RU)    
------			from RESULT_NR_2D_LOS_RU_temp_8460062  -- 창원시청 (BIN Test)
--             from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
----            where ru_id = 1012242295
--       ) RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
;

-- Check Result Data
select * from RESULT_NR_2D_LOS
 where schedule_id = 8460062
;

select count(*) from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
;

select * from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
;

select los, count(*) from RESULT_NR_2D_LOS
where schedule_id = 8460062
group by los
;
-- 0: 28822 1: 62978 (BIN방식 Legacy)
-- 0: 66949 1: 55920 (@@@)

-- 0: 32542 1: 59865


--------------------------------------------------------------------------------------------------------------------------
-- 3. Export Result Data (for Test)
--------------------------------------------------------------------------------------------------------------------------
hive -e "select * from RESULT_NR_2D_LOS where schedule_id = 8460062;" | sed 's/[[:space:]]\+/,/g' > los_test.csv

--hive -e "select scenario_id, rx_tm_xpos, rx_tm_ypos, rx_x_point, rx_y_point, value, schedule_id from RESULT_NR_2D_LOS_RU_temp_8460855;" | sed 's/[[:space:]]\+/,/g' > los_test.csv;

---------------------------------------------------------E-N-D---------------------------------------------------------------------------


-- PLB 정보
drop table RESULT_NR_2D_PLB_temp;
CREATE TABLE RESULT_NR_2D_PLB_temp
(
	SCENARIO_ID int,
	RX_TM_XPOS int,
	RX_TM_YPOS int,
	X_POINT int,
	Y_POINT int,
	PLB int,
	SCHEDULE_ID int
)
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
;

truncate table RESULT_NR_2D_PLB_temp;

with AREA as
(
select a.scenario_id, b.schedule_id,
       a.tm_startx div a.resolution * a.resolution as tm_startx,
       a.tm_starty div a.resolution * a.resolution as tm_starty,
       a.tm_endx div a.resolution * a.resolution as tm_endx,
       a.tm_endy div a.resolution * a.resolution as tm_endy,
       a.resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = 8460062  
   and a.scenario_id = b.scenario_id
)
insert into RESULT_NR_2D_PLB_temp
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los,
       max(AREA.schedule_id) as schedule_id
  from AREA, 
       (select 5104573 as scenario_id, 8460062 as schedule_id, '' as ru_id,
               rx_tm_xpos, rx_tm_ypos, --value
               1 value
 --from PATHLOSSPLB_temp               
 --            from RESULT_NR_2D_LOS_RU_temp_8460062_1012242295
   from RESULT_NR_2D_LOS_RU_temp_8460062
         where (is_bld = 't' or is_bld = 'T')
       ) RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
;

select * from RESULT_NR_2D_LOS_RU_temp_8460062;

select * from PATHLOSSPLB_temp;

select * from RESULT_NR_2D_PLB_temp;

--hive -e "select * from RESULT_NR_2D_PLB_temp where schedule_id = 8460062;" | sed 's/[[:space:]]\+/,/g' > los_test.csv
hive -e "select * from RESULT_NR_2D_PLB_temp;" | sed 's/[[:space:]]\+/,/g' > los_test.csv

----------------------------------------------------------------------------------------------------------------------------------------
select  from SCENARIO
;

select * from SCHEDULE
;

select * from MOBILE_PARAMETER
;

select * from FABASE
;

select * from SCENARIO_NR_RU
;

select * from SCENARIO_NR_ANTENNA
;

select * from RESULT_NR_2D_PATHLOSS_RU
;

select scenario_id, ru_id, resolution, site_startx, site_starty, site_endx, site_endy, x_bin_cnt, y_bin_cnt,
       floor((site_endx - site_startx) / resolution) x_cnt,
       floor((site_endy - site_starty) / resolution) y_cnt
 from SCENARIO_NR_RU
; 


select * from ANTENABASE
;

select * from NRPARAMETER_ANT_MAKER
;

WITH T1 AS
(
SELECT fa_seq
  FROM SCENARIO
  WHERE scenario_id = 5104573 --@@@
) 
SELECT                         
    A.antena_seq                   
    ,A.antena_nm                   
    ,A.maker                       
    ,A.beamwidth                   
    ,A.fronttobackratio            
    ,A.maxgain                     
    ,A.class                       
    ,A.tiltingtype                 
    ,A.horizontalpattern           
    ,A.verticalpattern             
    ,A.type                      
    ,A.fa_seq                      
    ,A.img_path                    
    ,A.his_antena_seq              
    ,A.def_tilt                    
    ,A.antena_standard_nm         
    ,A.beamheight                  
    ,B.maker                      
    ,NVL(B.limit_tilting, -1) AS limit_tilting                 
FROM (select A.* from ANTENABASE A, T1
       where A.fa_seq = T1.fa_seq
     ) A
 LEFT OUTER JOIN NRPARAMETER_ANT_MAKER B
   ON (A.fa_seq = B.fa_seq AND A.antena_standard_nm = B.antenna_nm)       
;





"""
  
}

}
