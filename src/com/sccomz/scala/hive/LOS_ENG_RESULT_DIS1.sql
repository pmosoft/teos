DROP TABLE LOS_ENG_RESULT_DIS1;

CREATE EXTERNAL TABLE LOS_ENG_RESULT_DIS1 (
  JOB_ID                            STRING
, SCENARIO_ID                       INT
, BIN_ID                            STRING
, BIN_X                             FLOAT
, BIN_Y                             FLOAT
, BIN_SIZE                          INT
, LOS                               BOOLEAN
)