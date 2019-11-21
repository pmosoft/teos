DROP TABLE SCENARIO;

CREATE EXTERNAL TABLE SCENARIO (
  SCENARIO_NM                      STRING     
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
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO';

ALTER TABLE I_SCENARIO ADD PARTITION (SCHEDULE_ID=8459967) LOCATION '/TEOS/warehouse/SCENARIO/SCHEDULE_ID=8459967';
ALTER TABLE I_SCENARIO DROP PARTITION (SCHEDULE_ID=8459967);

SELECT * FROM I_SCENARIO;
sql("SELECT * FROM I_SCENARIO").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCENARIO").take(100).foreach(println);

LOAD DATA INPATH '/home/hadoop/mh_hr_employees.csv'  
INTO TABLE Employee PARTITION (state='Maharashtra', department='HR');



DROP TABLE SCENARIO;

CREATE EXTERNAL TABLE SCENARIO (
  SCENARIO_NM                      STRING     
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
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO';




INSERT INTO SCENARIO_T2 PARTITION (SCENARIO_ID) SELECT * 
FROM SCENARIO WHERE SCENARIO_ID IN (
 5105179                                                                       
,5104178
,5109046
,5113166
,5110325
,5103801
,5109987
,5103372
,5102966
,5102782
,5105188
,5106985
,5105384
,5104772
,5106984
,5106986
,5104573
,5102166
,5105178
,5105970
,5106981
,5104175
,5104180
,5111727
,5105785
,5107566
,5111699
,5102781
,5100968
,5108366
,5106982
,5101012
,5105366
,5103371
,5104173
,5105189
,5112966
,5102971
,5103836
,5104176
,5112970
,5112166
,5103366
,5105187
,5107567
,5104177
,5107778
,5104181
);



INSERT INTO SCENARIO_T2 PARTITION (SCENARIO_ID) SELECT * 
FROM SCENARIO WHERE SCENARIO_ID IN (
 5112167
,5104182
,5105385
,5103802
,5107168
,5107968
,5112766
,5105173
,5103168
,5104172
,5103982
,5102975
,5112968
,5112967
,5112093
,5112171
,5105383
,5107169
,5112169
,5108367
,5112168
,5112969
,5107967
,5108171
,5103373
,5103766
,5103167
,5105789
,5112366
,5105382
,5108566
,5103166
,5103767
,5103972
,5104179
,5107766
,5104572
,5105186
,5101004
,5103368
,5112767
,5103370
,5100967
,5109465
,5107767
,5107785
,5109970
,5107966
);



set hive.exec.dynamic.partition.mode=nonstrict;

sql(s"""set hive.exec.dynamic.partition.mode=nonstrict""").take(100).foreach(println);

sql(s"""DROP TABLE SCENARIO""").take(100).foreach(println);

sql(s"""
CREATE EXTERNAL TABLE SCENARIO (
  SCENARIO_NM                      STRING     
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
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO'
""").take(100).foreach(println);




spark.sql(s"""SELECT DISTINCT SCENARIO_ID FROM SCENARIO""").take(100).foreach(println);

spark.sql(s"""SELECT DISTINCT SCHEDULE_ID FROM SCHEDULE""").take(100).foreach(println);

hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO_NR_RU_AVG_HEIGHT



hadoop fs -chown -R icpap:hadoop /TEOS/warehouse/SCENARIO
hadoop fs -chmod -R 777 /TEOS/warehouse/



