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

SELECT * FROM RESULT_NR_2D_RSRP_RU;

