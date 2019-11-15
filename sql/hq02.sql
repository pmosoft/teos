-------------------------------------
-- schedule
-------------------------------------

DROP TABLE schedule_t1;

CREATE EXTERNAL TABLE `default.schedule_t1`(
  `type_cd` string, 
  `scenario_id` int, 
  `user_id` string, 
  `prioritize` string, 
  `process_cd` string, 
  `process_msg` string, 
  `scenario_path` string, 
  `reg_dt` string, 
  `modify_dt` string, 
  `retry_cnt` int, 
  `server_id` string, 
  `bin_x_cnt` int, 
  `bin_y_cnt` int, 
  `ru_cnt` int, 
  `analysis_weight` int, 
  `phone_no` string, 
  `result_time` float, 
  `tilt_process_type` int, 
  `geometryquery_schedule_id` int, 
  `result_bit` string, 
  `interworking_info` string)
PARTITIONED BY ( 
  `schedule_id` int)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe' 
WITH SERDEPROPERTIES ( 
  'field.delim'='|', 
  'line.delim'='\n', 
  'serialization.format'='|') 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION
  'hdfs://nameservice1/TEOS/warehouse/SCHEDULE_T1'
TBLPROPERTIES (
  'transient_lastDdlTime'='1573609940')


SELECT COUNT(*) FROM schedule_t1;

insert into schedule_t1 partition (schedule_id) SELECT * from schedule;



DROP TABLE SCHEDULE;

CREATE EXTERNAL TABLE SCHEDULE (
  TYPE_CD                   STRING
, SCENARIO_ID               INT
, USER_ID                   STRING
, PRIORITIZE                STRING
, PROCESS_CD                STRING
, PROCESS_MSG               STRING
, SCENARIO_PATH             STRING
, REG_DT                    STRING
, MODIFY_DT                 STRING
, RETRY_CNT                 INT
, SERVER_ID                 STRING
, BIN_X_CNT                 INT
, BIN_Y_CNT                 INT
, RU_CNT                    INT
, ANALYSIS_WEIGHT           INT
, PHONE_NO                  STRING
, RESULT_TIME               FLOAT
, TILT_PROCESS_TYPE         INT
, GEOMETRYQUERY_SCHEDULE_ID INT
, RESULT_BIT                STRING
, INTERWORKING_INFO         STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCHEDULE';


INSERT INTO SCHEDULE_T2 PARTITION (SCHEDULE_ID) SELECT * FROM SCHEDULE_T1;

INSERT INTO SCHEDULE PARTITION (SCHEDULE_ID) SELECT * FROM SCHEDULE_T1;

select * from schedule_t2;


select * from schedule;


----------------------------------------------------------------------------------------------------
CREATE EXTERNAL TABLE `default.scenario_t1`(
  `scenario_nm` string, 
  `user_id` string, 
  `system_id` int, 
  `network_type` int, 
  `sido_cd` string, 
  `sigugun_cd` string, 
  `dong_cd` string, 
  `sido` string, 
  `sigugun` string, 
  `dong` string, 
  `startx` float, 
  `starty` float, 
  `endx` float, 
  `endy` float, 
  `fa_model_id` int, 
  `fa_seq` int, 
  `scenario_desc` string, 
  `use_building` int, 
  `use_multifa` int, 
  `precision` int, 
  `pwrctrlcheckpoint` int, 
  `maxiterationpwrctrl` int, 
  `resolution` int, 
  `model_radius` int, 
  `reg_dt` string, 
  `modify_dt` string, 
  `upper_scenario_id` int, 
  `floorbuilding` int, 
  `floor` int, 
  `floorloss` int, 
  `scenario_sub_id` int, 
  `scenario_solution_num` int, 
  `loss_type` int, 
  `ru_cnt` int, 
  `modify_yn` string, 
  `batch_yn` string, 
  `tm_startx` int, 
  `tm_starty` int, 
  `tm_endx` int, 
  `tm_endy` int, 
  `real_date` string, 
  `real_drm_file` string, 
  `real_comp` string, 
  `real_catt` string, 
  `real_caty` string, 
  `bld_type` string, 
  `ret_period` int, 
  `ret_end_datetime` string, 
  `buildinganalysis3d_yn` string, 
  `buildinganalysis3d_resolution` int, 
  `area_id` int, 
  `buildinganalysis3d_related_yn` string, 
  `related_analysis_covlimitrsrp` int)
PARTITIONED BY ( 
  `scenario_id` int)
ROW FORMAT SERDE 
  'org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe' 
WITH SERDEPROPERTIES ( 
  'field.delim'='|', 
  'line.delim'='\n', 
  'serialization.format'='|') 
STORED AS INPUTFORMAT 
  'org.apache.hadoop.mapred.TextInputFormat' 
OUTPUTFORMAT 
  'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION
  'hdfs://nameservice1/TEOS/warehouse/SCENARIO'
TBLPROPERTIES (
  'transient_lastDdlTime'='1573605915')


INSERT INTO SCENARIO_T1 PARTITION (SCENARIO_ID) SELECT * FROM SCENARIO;

INSERT INTO SCENARIO PARTITION (SCENARIO_ID) SELECT * 
FROM SCENARIO_T2 WHERE SCENARIO_ID IN (
 5105179                                                                       
);


INSERT INTO SCENARIO PARTITION (SCENARIO_ID) SELECT * 
FROM SCENARIO_T2 WHERE SCENARIO_ID IN (
 5112969
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


SELECT COUNT(*) FROM SCENARIO_T2

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

-----------------------------------------------------------------------------------------------



CREATE EXTERNAL TABLE SCENARIO_NR_RU_T2 (                                                                                                                                                                                                                                                                                                             
  ENB_ID                        STRING                                                                                                                        
, PCI                           INT                                                                                                                       
, PCI_PORT                      INT                                                                                                                      
, RU_ID                         STRING                                                                                                                      
, MAKER                         STRING                                                                                                                        
, SECTOR_ORD                    INT                                                                                                                      
, REPEATERATTENUATION           INT                                                                                                                        
, REPEATERPWRRATIO              INT                                                                                                                        
, RU_SEQ                        INT                                                                                                                        
, RADIUS                        INT                                                                                                                        
, FEEDER_LOSS                   INT                                                                                                                         
, NOISEFLOOR                    INT                                                                                                                        
, CORRECTION_VALUE              INT                                                                                                                        
, FADE_MARGIN                   INT                                                                                                                        
, XPOSITION                     STRING                                                                                                                        
, YPOSITION                     STRING                                                                                                                        
, HEIGHT                        INT                                                                                                                        
, SITE_ADDR                     STRING                                                                                                                        
, TYPE                          STRING                                                                                                                        
, STATUS                        STRING                                                                                                                        
, SISUL_CD                      STRING                                                                                                                        
, MSC                           INT                                                                                                                        
, BSC                           INT                                                                                                                        
, NETWORKID                     INT                                                                                                                        
, USABLETRAFFICCH               INT                                                                                                                        
, SYSTEMID                      INT                                                                                                                        
, RU_TYPE                       INT                                                                                                                        
, FA_MODEL_ID                   INT                                                                                                                        
, NETWORK_TYPE                  INT                                                                                                                        
, RESOLUTION                    INT                                                                                                                        
, FA_SEQ                        INT                                                                                                                        
, SITE_STARTX                   FLOAT                                                                                                                        
, SITE_STARTY                   FLOAT                                                                                                                        
, SITE_ENDX                     FLOAT                                                                                                                        
, SITE_ENDY                     FLOAT                                                                                                                        
, X_BIN_CNT                     INT                                                                                                                        
, Y_BIN_CNT                     INT                                                                                                                        
)
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO_NR_RU_T2';

INSERT INTO SCENARIO_NR_RU_T2 PARTITION (SCENARIO_ID) SELECT * FROM SCENARIO_NR_RU;

INSERT INTO SCENARIO_NR_RU PARTITION (SCENARIO_ID) SELECT * FROM SCENARIO_NR_RU_T2;



-------------------------------------------------------------------------------

DROP TABLE SCENARIO_NR_ANTENNA;

CREATE EXTERNAL TABLE SCENARIO_NR_ANTENNA (
  ANTENA_SEQ                    INT
, RU_ID                         STRING
, ANTENA_NM                     STRING
, ORIENTATION                   INT
, TILTING                       INT
, ANTENA_ORD                    INT
, LIMIT_TILTING                 INT
, RU_SEQ                        INT
)
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO_NR_ANTENNA'
;

INSERT INTO SCENARIO_NR_ANTENNA_T2 PARTITION (SCENARIO_ID) SELECT * FROM SCENARIO_NR_ANTENNA;

INSERT INTO SCENARIO_NR_ANTENNA PARTITION (SCENARIO_ID) SELECT * FROM SCENARIO_NR_ANTENNA_T2;


-------------------------------------------------------------------------------------------------
DROP TABLE RESULT_NR_2D_LOS_RU;



CREATE EXTERNAL TABLE RESULT_NR_2D_LOS_RU_BAK(
  SCENARIO_ID INT, 
  BIN_ID STRING, 
  RX_TM_XPOS INT, 
  RX_TM_YPOS INT, 
  RZ FLOAT, 
  RESOLUTION INT, 
  VALUE INT, 
  THETA FLOAT, 
  PHI FLOAT, 
  RU_ID STRING, 
  TX_TM_XPOS FLOAT, 
  TX_TM_YPOS FLOAT, 
  TZ FLOAT, 
  IS_BLD STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_2D_LOS_RU_BAK';


set hive.exec.dynamic.partition.mode=nonstrict;


INSERT INTO RESULT_NR_2D_LOS_RU_BAK PARTITION (SCHEDULE_ID) SELECT * FROM RESULT_NR_2D_LOS_RU;
 
SELECT COUNT(*)
FROM (
SELECT DISTINCT SCHEDULE_ID,RU_ID FROM RESULT_NR_2D_LOS_RU;
)




