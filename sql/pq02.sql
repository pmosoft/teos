select scenario_id
     , COUNT(*)
from job_dis
group by scenario_id
;


select *
from job_dis
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

