DROP TABLE I_RESULT_NR_2D_RSRP;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_RSRP (
  SCENARIO_ID                      INT 
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT 
, X_POINT                          INT 
, Y_POINT                          INT 
, RSRP                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_RSRP'
;


DROP TABLE I_RESULT_NR_2D_RSRP_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_RSRP_RU (
  SCENARIO_ID                      INT 
, RU_ID                            STRING 
, ENB_ID                           INT 
, CELL_ID                          INT 
, RX_TM_XPOS                       INT 
, RX_TM_YPOS                       INT 
, LOS                              INT 
, PATHLOSS                         FLOAT 
, ANTENNA_GAIN                     FLOAT 
, PATHLOSSPRIME                    FLOAT 
, RSRPPILOT                        FLOAT 
, RSRP                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_RSRP_RU'
;


SELECT * FROM I_RESULT_NR_2D_RSRPPILOT_RU
;

SELECT * FROM I_RESULT_NR_2D_PATHLOSS_RU
;



DROP TABLE I_RESULT_NR_2D_PATHLOSS_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_PATHLOSS_RU (
  SCENARIO_ID                      INT
, RU_ID                            STRING
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, RZ                               FLOAT
, LOS                              INT
, PATHLOSS                         FLOAT
, IS_UMI_MODEL                     INT
, DIST2D                           FLOAT
, DIST3D                           FLOAT
, DISTBP                           FLOAT
, HBS                              FLOAT
, HUT                              FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_PATHLOSS_RU'
;


SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.SCENARIO_ID = B.SCENARIO_ID
;

WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT 
       (((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - B.SITE_STARTX) DIV B.RESOLUTION) AS X_POINT,
       (((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - B.SITE_STARTY) DIV B.RESOLUTION) AS Y_POINT,
       value
 FROM  RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = 8460062
   AND A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.RU_ID = 1012242308
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN B.SITE_STARTX AND B.SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN B.SITE_STARTY AND B.SITE_ENDY
 ORDER BY X_POINT, Y_POINT
 ;


SELECT * FROM RESULT_NR_2D_LOS_RU LIMIT 1;




SELECT * 
FROM   RESULT_NR_2D_LOS_RU 
WHERE  SCENARIO_ID = 8460062
AND    RU_ID = 1012242300 
LIMIT 100
;














SELECT * FROM I_SCENARIO
;

SELECT * FROM i_schedule
;

SELECT * FROM i_schedule


DROP TABLE I_FABASE;

CREATE EXTERNAL TABLE I_FABASE
(
 FA_SEQ               INT
,SYSTEMTYPE           INT
,CHNO                 INT
,UPLINKFREQ           INT
,DOWNLINKFREQ         INT
,ANALY_CHECK          INT
,PASSLOSS_MODEL       STRING
,FREQ                 STRING
,UPLINKFREQ_COMMENT   STRING
,DOWNLINKFREQ_COMMENT STRING
,REG_DT               STRING
,REG_ID               STRING
)
STORED AS PARQUET
LOCATION '/teos/warehouse/FABASE'
;

SELECT * FROM I_FABASE
;

CREATE EXTERNAL TABLE I_SCENARIO_NR_RU (                                                                                                                                                                                                                                                                                                             
  SCENARIO_ID                   INT                                                                                                                        
, ENB_ID                        STRING                                                                                                                        
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
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/SCENARIO_NR_RU';


SELECT COUNT(*) FROM result_nr_2d_los_ru;

SELECT x_point, y_point, los FROM result_nr_2d_los ORDER BY x_point, y_point
;

SELECT * FROM result_nr_2d_los
;

SELECT * FROM scenario_nr_ru
;

select * from i_los_eng_result;

ALTER TABLE I_LOS_ENG_RESULT DROP IF EXISTS PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284);

ALTER TABLE I_LOS_ENG_RESULT ADD PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284) LOCATION '/teos/warehouse/LOS_ENG_RESULT/SCHEDULE_ID=8460062/RU_ID=1012242284';


DROP TABLE I_LOS_ENG_RESULT;

CREATE EXTERNAL TABLE I_LOS_ENG_RESULT (
  BIN_ID                            STRING
, BIN_X                             FLOAT
, BIN_Y                             FLOAT
, BIN_Z                             FLOAT
, BLD_ID                            STRING
, BIN_SIZE                          INT
, LOS                               BOOLEAN
, IN_BLD                            BOOLEAN
, THETA_DEG                         INT
, PHI_DEG                           INT
, SECTOR_X                          FLOAT
, SECTOR_Y                          FLOAT
, SECTOR_Z                          FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/teos/warehouse/LOS_ENG_RESULT';
