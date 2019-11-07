DROP TABLE I_RESULT_NR_2D_RSRPPILOT_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_RSRPPILOT_RU (
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
, TEMP_ANTGAIN                     FLOAT 
, TEMP_LOS                         INT 
, TEMP_PATHLOSS                    FLOAT 
, TEMP_PLPRIME                     FLOAT 
, TEMP_RSRPLILOT                   FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_RSRPPILOT_RU'
;

SELECT * FROM I_RESULT_NR_2D_PATHLOSS_RU;