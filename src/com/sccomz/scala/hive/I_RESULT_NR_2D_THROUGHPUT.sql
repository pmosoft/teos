DROP TABLE I_RESULT_NR_2D_THROUGHPUT;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_THROUGHPUT (
  SCENARIO_ID                      INT 
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT 
, X_POINT                          INT 
, Y_POINT                          INT 
, THROUGHPUT                       FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_THROUGHPUT'
;


SELECT * FROM RESULT_NR_2D_THROUGHPUT
;

