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

