DROP TABLE I_RESULT_NR_2D_LOS;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_LOS (
  SCENARIO_ID                      INT
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, X_POINT                          INT
, Y_POINT                          INT
, LOS                              INT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/RESULT_NR_2D_LOS';

SELECT * FROM I_RESULT_NR_2D_LOS;