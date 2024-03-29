DROP TABLE LOS_ENG_RESULT2;

CREATE EXTERNAL TABLE LOS_ENG_RESULT2 (
  BIN_ID                            STRING
, BIN_X                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/teos/warehouse/LOS_ENG_RESULT2';

LOAD DATA INPATH '/disk2/etl/LOS_ENG_RESULT2_8459967_11111.dat'
INTO TABLE LOS_ENG_RESULT2 PARTITION (SCHEDULE_ID=8459967, ru_id='11111');
