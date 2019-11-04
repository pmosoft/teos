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

SELECT * FROM I_SCENARIO;
SELECT * FROM I_SCHEDULE;

REFRESH TABLE I_SCHEDULE;

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