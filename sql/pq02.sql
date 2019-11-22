SELECT A.RU_CNT , A.POST_DONE_CNT, A.POST_DONE_CNT - B.EXT_DONE_CNT EXT_CNT, B.EXT_ING_CNT, B.EXT_DONE_CNT
FROM  (
		   SELECT COUNT(A.RU_ID)                              AS RU_CNT
		        , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE NULL END) AS POST_DONE_CNT
		   FROM   JOB_DIS A
		   WHERE  A.SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463234)
       ) A,
      (
		   SELECT COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
		        , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
		   FROM   JOB_DIS_ETL B
		   WHERE  B.SCHEDULE_ID = '8463235'
	     ) B
;


SELECT distinct STAT 
FROM   JOB_DIS
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463234)
;

SELECT COUNT(RU_ID)
FROM   JOB_DIS
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463235)
;


SELECT *
FROM   JOB_DIS_ETL
WHERE  SCHEDULE_ID = '8463235'
;

SELECT RU_ID
FROM   JOB_DIS
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463235)



SELECT A.RU_ID
     , A.CLUSTER_NAME	     
FROM  (SELECT * 
       FROM   JOB_DIS
       WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463234)
       AND    STAT = 3) A
       LEFT OUTER JOIN (
       SELECT DISTINCT SCHEDULE_ID, RU_ID 
       FROM   JOB_DIS_ETL 
       WHERE  SCHEDULE_ID = '8463234'
       AND    STAT IN (5)   
       ) B
       ON   A.RU_ID       = B.RU_ID
WHERE  B.RU_ID IS NULL
;

select *
FROM   JOB_DIS_ETL B
WHERE  B.SCHEDULE_ID = '8463234'
;

--scenario_id=5113766
--schedule_id = 8463234 (2DºÐ¼®)

select *
FROM   JOB_DIS
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463234)
and RU_ID = '1012138268'
;

select * from 


-- ru_bld_list 4
-- bld_list    

SELECT
       BIN_X                                          ||'|'||
       BIN_Y                                          ||'|'||
       BIN_Z                                          ||'|'||
       CASE WHEN LOS IS TRUE THEN 1 ELSE 0 END        ||'|'||
       THETA_DEG                                      ||'|'||
       PHI_DEG                                        ||'|'||
       CASE WHEN IN_BLD IS TRUE THEN 'T' ELSE 'F' END ||'|'||
       '8463234'                                ||'|'||
       '1012138268'                                      ||'|'
FROM   LOS_ENG_RESULT
WHERE  SCHEDULE_ID = 8463234
AND    RU_ID  = '1012138268'
;

select COUNT(*)
from (
SELECT A.RU_ID
     , A.CLUSTER_NAME	     
FROM  (select * 
       from JOB_DIS
       where SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463233)
       and STAT = 3) A
       left outer join (
       select distinct SCHEDULE_ID, RU_ID 
       from   JOB_DIS_ETL 
       where  SCHEDULE_ID = '8463233'
       and    STAT in (5)   
       ) B
       on   A.RU_ID       = B.RU_ID
where B.RU_ID is NULL       
) A
;


SELECT
	     ENB_ID         ||'|'||
	     PCI            ||'|'||
	     PCI_PORT       ||'|'||
	     RU_ID          ||'|'||
	     ROUND(CAST(DEM_HEGHT AS NUMERIC),2)      ||'|'||
	     ROUND(CAST(BLD_AVG_HEIGHT AS NUMERIC),2) ||'|'||
	     SCENARIO_ID    ||'|'
FROM   SCENARIO_NR_RU_DEM
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463233)
;


select RU_ID 
from SCENARIO_NR_RU_DEM
group by RU_ID
order by RU_ID
;

select COUNT(*) 
from SCENARIO_NR_RU_DEM
where RU_ID < '1011760123'
;


SELECT
	     ROUND( CAST(DEM_HEGHT AS NUMERIC),2)
FROM   SCENARIO_NR_RU_DEM
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463233)
;

select distinct SCHEDULE_ID, RU_ID 
from   JOB_DIS_ETL 
where  SCHEDULE_ID = '8463233'
and    STAT in (4)   
;
   
   
select stat, count(*)
from job_dis
group by stat
;

select * from job_dis;


SELECT A.RU_CNT , A.EXT_CNT, B.EXT_ING_CNT, B.EXT_DONE_CNT
FROM  (
		   SELECT COUNT(A.RU_ID)                              AS RU_CNT
		        , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE 0 END) AS EXT_CNT
		   FROM   JOB_DIS A
		   WHERE  A.SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8460062)
       ) A,
      (		
		   SELECT COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
		        , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
		   FROM   JOB_DIS_ETL B
		   WHERE  B.SCHEDULE_ID = '8460062'
	     ) B
;


SELECT COUNT(A.RU_ID)                              AS RU_CNT
     , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE NULL END) AS EXT_CNT
FROM   JOB_DIS A
WHERE  A.SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = 8463233)
;


select COUNT(*) from scenario_nr_ru_dem
where  SCENARIO_ID = '5113566'
;

select * from scenario_nr_ru_dem
where  SCENARIO_ID = '5113566'
;

SELECT
       BIN_X                      ||'|'||
       BIN_Y                      ||'|'||
       BIN_Z                      ||'|'||
       LOS                        ||'|'||
       THETA_DEG                  ||'|'||
       PHI_DEG                    ||'|'||
       IN_BLD                     ||'|'
FROM   LOS_ENG_RESULT
WHERE  SCHEDULE_ID = 8460178
--WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = 8460178)
--AND    RU_ID   = '1011760654'
limit 100
;

SELECT
       BIN_X         
     , BIN_Y         
     , BIN_Z         
     , case when LOS is true then 1 else 0 END           
     , THETA_DEG     
     , PHI_DEG       
     , case when IN_BLD is true then 'T' else 'F' END
FROM   LOS_ENG_RESULT
--WHERE  SCHEDULE_ID = 8460178
--WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = 8460178)
--AND    RU_ID   = '1011760654'
limit 100
;

select scenario_id,sector_id
     , count(*)
FROM LOS_ENG_RESULT
group by scenario_id,sector_id
;

select scenario_id,ru_id,stat,cluster_name
from job_dis
where scenario_id = '5104573'
;

select scenario_id,cluster_name,ru_id,stat
from job_dis
where scenario_id = '5104573'
order by scenario_id,cluster_name,ru_id,stat
;

select * from I_SCHEDULE
;

select * from i_du
;

SELECT
       COALESCE(BIN_ID,'')        ||'|'||
       BIN_X                      ||'|'||
       BIN_Y                      ||'|'||
       BIN_Z                      ||'|'||
       COALESCE(BLD_ID,0)         ||'|'||
       LOS                        ||'|'||
       IN_BLD                     ||'|'||
       THETA_DEG                  ||'|'||
       PHI_DEG                    ||'|'||
       SECTOR_X                   ||'|'||
       SECTOR_Y                   ||'|'||
       SECTOR_Z                   ||'|'||
       '8460062'            ||'|'
       ,BIN_ID
FROM   LOS_ENG_RESULT
--WHERE  SCHEDULE_ID = 8460062
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
AND    SECTOR_ID   = '1012242284'
;

drop table PUBLIC.JOB_DIS_ETL;

CREATE TABLE PUBLIC.JOB_DIS_ETL (
	SCHEDULE_ID TEXT NULL,
	RU_ID       TEXT NULL,
	STAT        INT4 NULL,
	UPDATE_TIME TIMESTAMPTZ NULL, 
	CREATED_AT  TIMESTAMPTZ NULL DEFAULT CURRENT_TIMESTAMP
);


SELECT COUNT(A.RU_ID)                              AS RU_CNT
     , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE 0 END) AS EXT_CNT
     , COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
     , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
FROM   JOB_DIS A
     , JOB_DIS_ETL B
WHERE  A.SCENARIO_ID IN (SELECT cast(SCENARIO_ID as text) FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
AND    B.SCHEDULE_ID = '8460062'
;


SELECT COUNT(A.RU_ID)                              AS RU_CNT
     , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE 0 END) AS EXT_CNT
     , COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
     , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
FROM   JOB_DIS A
     , JOB_DIS_ETL B
WHERE  A.SCENARIO_ID IN (SELECT cast(SCENARIO_ID as text) FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
AND    B.SCHEDULE_ID = '8460062'
;


select A.RU_CNT , A.EXT_CNT, B.EXT_ING_CNT, B.EXT_DONE_CNT
from  (
		SELECT COUNT(A.RU_ID)                              AS RU_CNT
		     , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE 0 END) AS EXT_CNT
		FROM   JOB_DIS A
		WHERE  A.SCENARIO_ID IN (SELECT cast(SCENARIO_ID as text) FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
      ) A,
     (		
		SELECT COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
		     , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
		FROM   JOB_DIS_ETL B
		WHERE  B.SCHEDULE_ID = '8460062'
	 ) B	
;


SELECT cast(SCENARIO_ID as text) FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062
;

