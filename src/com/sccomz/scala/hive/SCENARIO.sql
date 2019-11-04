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
sql("SELECT * FROM I_SCENARIO").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCENARIO").take(100).foreach(println);

LOAD DATA INPATH '/home/hadoop/mh_hr_employees.csv'  
INTO TABLE Employee PARTITION (state='Maharashtra', department='HR');
