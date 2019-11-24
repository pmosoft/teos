-----------------------------------------
-- I_SCENARIO_NR_RU
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO_NR_RU;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU(
 SCENARIO_ID                   NUMERIC     NOT NULL
,ENB_ID                        VARCHAR(10) NOT NULL
,PCI                           NUMERIC     NOT NULL
,PCI_PORT                      NUMERIC     NOT NULL
,RU_ID                         VARCHAR(48) NOT NULL
,MAKER                         VARCHAR(30)
,SECTOR_ORD                    NUMERIC     NOT NULL
,REPEATERATTENUATION           NUMERIC
,REPEATERPWRRATIO              NUMERIC
,RU_SEQ                        NUMERIC
,RADIUS                        NUMERIC
,FEEDER_LOSS                   NUMERIC
,NOISEFLOOR                    NUMERIC
,CORRECTION_VALUE              NUMERIC
,FADE_MARGIN                   NUMERIC
,XPOSITION                     VARCHAR(40)
,YPOSITION                     VARCHAR(40)
,HEIGHT                        NUMERIC
,SITE_ADDR                     VARCHAR(500)
,TYPE                          VARCHAR(10)
,STATUS                        VARCHAR(10)
,SISUL_CD                      VARCHAR(50)
,MSC                           NUMERIC
,BSC                           NUMERIC
,NETWORKID                     NUMERIC
,USABLETRAFFICCH               NUMERIC
,SYSTEMID                      NUMERIC
,RU_TYPE                       NUMERIC
,FA_MODEL_ID                   NUMERIC
,NETWORK_TYPE                  NUMERIC
,RESOLUTION                    NUMERIC
,FA_SEQ                        NUMERIC
,SITE_STARTX                   NUMERIC
,SITE_STARTY                   NUMERIC
,SITE_ENDX                     NUMERIC
,SITE_ENDY                     NUMERIC
,X_BIN_CNT                     NUMERIC
,Y_BIN_CNT                     NUMERIC
);

CREATE INDEX I_SCENARIO_NR_RU_IDX ON PUBLIC.I_SCENARIO_NR_RU USING BTREE (SCENARIO_ID,ENB_ID,PCI,PCI_PORT,RU_ID,SECTOR_ORD);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU TO POSTGRES;
SCENARIO_NR_RU
-----------------------------------------
-- I_SCENARIO_NR_RU
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO_NR_RU;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE(
  SCENARIO_ID                   NUMERIC     NOT NULL
, RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, CACHE_YN                      VARCHAR(1)      NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_SCENARIO_NR_RU_CACHE_IDX ON PUBLIC.I_SCENARIO_NR_RU_CACHE USING BTREE (SCENARIO_ID,RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE TO POSTGRES;

-----------------------------------------
-- I_SCENARIO_NR_RU_CACHE
-----------------------------------------
-- DROP TABLE PUBLIC.I_NR_RU_CACHE;

CREATE TABLE PUBLIC.I_NR_RU_CACHE (
  RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_NR_RU_CACHE_IDX ON PUBLIC.I_NR_RU_CACHE USING BTREE (RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_NR_RU_CACHE TO POSTGRES;


;


with temp01 as
(
select a.scenario_id, a.ru_id, a.xposition, a.yposition, a.height, a.resolution, b.mobile_height
from   i_scenario_nr_ru a,
      (select scenario_id, max(height) as mobile_height
       from i_mobile_parameter
       where scenario_id = 5105173
       group by scenario_id
       ) b
where  a.scenario_id = b.scenario_id
and    a.scenario_id = 5105173
) , temp02 as (
select *
from   temp01 a,
       I_NR_RU_CACHE b
where  a.ru_id
and, a.xposition
and, a.yposition
and, a.height
and, a.resolution
and               , b.mobile_height



)
select * from temp01
;

DROP TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE(
  SCENARIO_ID                   NUMERIC     NOT NULL
, RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, CACHE_YN                      VARCHAR(1)      NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_SCENARIO_NR_RU_CACHE_IDX ON PUBLIC.I_SCENARIO_NR_RU_CACHE USING BTREE (SCENARIO_ID,RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE TO POSTGRES;

-----------------------------------------
-- I_SCENARIO_NR_RU_CACHE
-----------------------------------------
DROP TABLE PUBLIC.I_NR_RU_CACHE;

CREATE TABLE PUBLIC.I_NR_RU_CACHE(
  RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_NR_RU_CACHE_IDX ON PUBLIC.I_NR_RU_CACHE USING BTREE (RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_NR_RU_CACHE TO POSTGRES;


select scenario_id, xposition, yposition, height, resolution
from   i_scenario_nr_ru a
;


CREATE TABLE PUBLIC.I_NR_RU_CACHE(
  RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40)
, YPOSITION                     VARCHAR(40)
, HEIGHT                        NUMERIC
, RESOLUTION                    NUMERIC
, MOBILE_HEIGHT                 NUMERIC
, REG_DT                        DATE NULL
, MODIFY_DT                     DATE NULL
);

CREATE INDEX I_NR_RU_CACHE_IDX ON PUBLIC.I_NR_RU_CACHE USING BTREE (RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_NR_RU_CACHE TO POSTGRES;




select scenario_id, max(height) as mobile_height
from i_mobile_parameter
group by scenario_id
;

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


