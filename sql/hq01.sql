DROP TABLE I_SCENARIO;

CREATE EXTERNAL TABLE I_SCENARIO (
  SCENARIO_ID                      INT
, SCENARIO_NM                      STRING     
, USER_ID                          STRING     
, SYSTEM_ID                        INT        
, NETWORK_TYPE                     INT     
, SIDO_CD                          STRING     
, SIGUGUN_CD                       STRING     
, DONG_CD                          STRING     
, SIDO                             STRING     
, SIGUGUN                          STRING     
, DONG                             STRING     
, STARTX                           FLOAT        
, STARTY                           FLOAT      
, ENDX                             FLOAT      
, ENDY                             FLOAT      
, FA_MODEL_ID                      INT        
, FA_SEQ                           INT       
, SCENARIO_DESC                    STRING        
, USE_BUILDING                     INT        
, USE_MULTIFA                      INT        
, PRECISION                        INT        
, PWRCTRLCHECKPOINT                INT        
, MAXITERATIONPWRCTRL              INT        
, RESOLUTION                       INT        
, MODEL_RADIUS                     INT     
, REG_DT                           STRING     
, MODIFY_DT                        STRING     
, UPPER_SCENARIO_ID                INT        
, FLOORBUILDING                    INT        
, FLOOR                            INT        
, FLOORLOSS                        INT        
, SCENARIO_SUB_ID                  INT        
, SCENARIO_SOLUTION_NUM            INT        
, LOSS_TYPE                        INT        
, RU_CNT                           INT     
, MODIFY_YN                        STRING     
, BATCH_YN                         STRING     
, TM_STARTX                        INT        
, TM_STARTY                        INT        
, TM_ENDX                          INT        
, TM_ENDY                          INT     
, REAL_DATE                        STRING     
, REAL_DRM_FILE                    STRING     
, REAL_COMP                        STRING     
, REAL_CATT                        STRING     
, REAL_CATY                        STRING     
, BLD_TYPE                         STRING     
, RET_PERIOD                       INT        
, RET_END_DATETIME                 STRING     
, BUILDINGANALYSIS3D_YN            STRING     
, BUILDINGANALYSIS3D_RESOLUTION    INT        
, AREA_ID                          INT        
, BUILDINGANALYSIS3D_RELATED_YN    STRING     
, RELATED_ANALYSIS_COVLIMITRSRP    INT
)
PARTITIONED BY (SCHEDULE_ID INT)
--COMMENT 'SCENARIO'
STORED AS PARQUET
LOCATION '/teos/warehouse/SCENARIO';


ALTER TABLE I_SCENARIO ADD PARTITION (SCHEDULE_ID=8459967) LOCATION '/teos/warehouse/SCENARIO/SCHEDULE_ID=8459967';
ALTER TABLE I_SCENARIO DROP PARTITION (SCHEDULE_ID=8459967);

SELECT * FROM SCENARIO WHERE SCENARIO_ID = 5104574;

SELECT B.SCHEDULE_ID
     , A.X_BIN_CNT, A.Y_BIN_CNT
     --, DATE_FORMAT(CURRENT_DATE(), 'yyyyMMdd') AS TODATE
     , B.USER_ID
     , A.ENB_ID
     , A.PCI
     , A.PCI_PORT
     , A.RU_ID
     , CONCAT(B.USER_ID,'/',A.SCENARIO_ID) AS SECTOR_PATH
     , CONCAT(B.USER_ID,'/',A.SCENARIO_ID,'/ENB_',A.ENB_ID,'/PCI_',A.PCI,'_PORT_',A.PCI_PORT,'_',A.RU_ID) AS RU_PATH
FROM   SCENARIO_NR_RU A, SCHEDULE B
WHERE  B.SCHEDULE_ID = 8463235
AND    A.SCENARIO_ID = B.SCENARIO_ID
;


SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = 8463235;

SELECT * FROM SCENARIO_NR_RU WHERE SCENARIO_ID = 5113766;

SELECT * FROM SCENARIO_NR_ANTENNA WHERE SCENARIO_ID = 5113766;



SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = 8460965;

SELECT * FROM SCHEDULE WHERE SCHEDULE_ID = 8463235;
SELECT * FROM SCENARIO WHERE SCENARIO_ID = 5113566;
SELECT * FROM SCENARIO_NR_RU WHERE SCENARIO_ID = 5113566;
SELECT * FROM SCENARIO_NR_ANTENNA WHERE SCENARIO_ID = 5113566;
SELECT * FROM MOBILE_PARAMETER WHERE SCENARIO_ID = 5113566;
SELECT * FROM NRUETRAFFIC WHERE SCENARIO_ID = 5113566;

SELECT * FROM NRSECTORPARAMETER WHERE SCENARIO_ID = 5113566;
SELECT * FROM NRSYSTEM WHERE SCENARIO_ID = 5113566;

SELECT COUNT(*) FROM SCENARIO_NR_RU_AVG_HEIGHT WHERE SCENARIO_ID = 5113566;

SELECT * FROM SCENARIO_NR_RU_AVG_HEIGHT WHERE SCENARIO_ID = 5113566;

SELECT * FROM RESULT_NR_2D_LOS WHERE SCHEDULE_ID = 8463233;

SELECT COUNT(*) FROM RESULT_NR_2D_LOS WHERE SCENARIO_ID = 5113566;

ALTER TABLE SCENARIO_NR_RU DROP IF EXISTS PARTITION (SCENARIO_ID=5104574);
ALTER TABLE SCENARIO_NR_RU ADD PARTITION (SCENARIO_ID=5104574) LOCATION '/TEOS/warehouse/SCENARIO_NR_RU/SCENARIO_ID=5104574';

SELECT DISTINCT concat('INSERT INTO TEMP001 VALUE (',RU_ID,')') FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8460062;

SELECT DISTINCT concat('INSERT INTO TEMP001 VALUE (',RU_ID,')') FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8460062;

SELECT * FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8463235;

SELECT DISTINCT RU_ID FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8463234;

SELECT * FROM result_nr_bf_los_ru;

set hive.exec.dynamic.partition.mode=nonstrict;

INSERT INTO RESULT_NR_BF_LOS_RU_BAK PARTITION (SCHEDULE_ID) SELECT * FROM RESULT_NR_BF_LOS_RU;

SELECT * FROM RESULT_NR_BF_LOS_RU;


SELECT * FROM RESULT_NR_BF_SCEN_HEADER;

-- 8460965

SELECT SCHEDULE_ID,RU_ID FROM RESULT_NR_2D_LOS_RU LIMIT 1;

sql("SELECT * FROM I_SCENARIO").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCENARIO").take(100).foreach(println);

sql("SELECT scenario_id,COUNT(*) FROM result_nr_2d_los_ru group by scenario_id").take(100).foreach(println);

sql("SELECT * FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573").take(100).foreach(println);

sql("SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT").take(100).foreach(println);

sql("SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_PASSLOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT").take(100).foreach(println);

sql("SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT asc").take(10000).foreach(println);

SELECT * FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT;

SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT;
;

SELECT * FROM i_result_nr_2d_pathloss WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT
;


CREATE EXTERNAL TABLE TEST03 (
  RU_ID                            STRING
, BIN_X                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/TEST03';

set hive.exec.dynamic.partition.mode=nonstrict;


insert into TEST03 partition (schedule_id)
select '222' AS RU_ID
     , 222 AS BIN_X
     , '111' AS SCHEDULE_ID
;

SELECT * FROM TEST03
;

SELECT * FROM RESULT_NR_2D_LOS WHERE schedule_id = 8463233 LIMIT 1
;


select *
  from RESULT_NR_2D_LOS_RU RSLT
 where  RSLT.ru_id = 1011760123
 ;

