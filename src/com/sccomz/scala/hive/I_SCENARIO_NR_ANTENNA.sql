DROP TABLE I_SCENARIO_NR_ANTENNA;

CREATE EXTERNAL TABLE I_SCENARIO_NR_ANTENNA (
  SCENARIO_ID                   INT
, ANTENA_SEQ                    INT
, RU_ID                         STRING
, ANTENA_NM                     STRING
, ORIENTATION                   INT
, TILTING                       INT
, ANTENA_ORD                    INT
, LIMIT_TILTING                 INT
, RU_SEQ                        INT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/SCENARIO_NR_ANTENNA'
;
