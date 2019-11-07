DROP TABLE I_RESULT_NR_2D_SINR;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_SINR (
  SCENARIO_ID                      INT 
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT 
, X_POINT                          INT 
, Y_POINT                          INT 
, SINR                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_SINR'
;


SELECT * FROM RESULT_NR_2D_SINR
;

