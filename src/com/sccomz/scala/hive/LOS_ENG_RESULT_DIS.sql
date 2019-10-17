DROP TABLE LOS_ENG_RESULT_DIS;

CREATE EXTERNAL TABLE LOS_ENG_RESULT_DIS (
  JOB_ID                            STRING
, SCENARIO_ID                       INT
, SCHEDULE_ID                       INT
, BIN_ID                            STRING
, BIN_X                             FLOAT
, BIN_Y                             FLOAT
, BIN_Z                             FLOAT
, BIN_SIZE                          INT
, LOS                               BOOLEAN
, THETA                             INT
, PHI                               INT
, SECTOR_ID                         STRING
, SECTOR_X                          FLOAT
, SECTOR_Y                          FLOAT
, SECTOR_Z                          FLOAT
)