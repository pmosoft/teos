DROP TABLE I_RESULT_NR_2D_LOS_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_LOS_RU (
  SCENARIO_ID                      INT
, BIN_ID                           STRING
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, RZ                               FLOAT
, RESOLUTION                       INT
, VALUE                            INT
, THETA                            FLOAT
, PHI                              FLOAT
, RU_ID                            STRING
, TX_TM_XPOS                       FLOAT
, TX_TM_YPOS                       FLOAT
, TZ                               FLOAT
, SCHEDULE_ID                      INT
)

SELECT * FROM I_RESULT_NR_2D_LOS_RU;