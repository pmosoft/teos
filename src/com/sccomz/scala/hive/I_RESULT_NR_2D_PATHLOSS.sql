DROP TABLE I_RESULT_NR_2D_PATHLOSS;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_PATHLOSS (
  SCENARIO_ID                      INT
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, X_POINT                          INT
, Y_POINT                          INT
, PATHLOSS                         FLOAT
, SCHEDULE_ID                      INT
)

SELECT * FROM I_RESULT_NR_2D_PATHLOSS;