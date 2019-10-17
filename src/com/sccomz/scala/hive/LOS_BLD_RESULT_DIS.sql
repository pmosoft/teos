DROP TABLE LOS_BLD_RESULT_DIS;

CREATE EXTERNAL TABLE LOS_BLD_RESULT_DIS (
  JOB_ID                           STRING
, SCENARIO_ID                      INT
, SECTOR_ID                        STRING
, TBD                              STRING
, FLOOR_X                          FLOAT
, FLOOR_Y                          FLOAT
, FLOOR_Z                          INT
, LOS                              BOOLEAN
)