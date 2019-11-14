DROP TABLE SCENARIO_NR_ANTENNA;

CREATE EXTERNAL TABLE SCENARIO_NR_ANTENNA (
  ANTENA_SEQ                    INT
, RU_ID                         STRING
, ANTENA_NM                     STRING
, ORIENTATION                   INT
, TILTING                       INT
, ANTENA_ORD                    INT
, LIMIT_TILTING                 INT
, RU_SEQ                        INT
)
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO_NR_ANTENNA'
;



sql(s"""DROP TABLE SCENARIO_NR_ANTENNA""").take(100).foreach(println);

sql(s"""
CREATE EXTERNAL TABLE SCENARIO_NR_ANTENNA (
  ANTENA_SEQ                    INT
, RU_ID                         STRING
, ANTENA_NM                     STRING
, ORIENTATION                   INT
, TILTING                       INT
, ANTENA_ORD                    INT
, LIMIT_TILTING                 INT
, RU_SEQ                        INT
)
PARTITIONED BY (SCENARIO_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCENARIO_NR_ANTENNA'
""").take(100).foreach(println);

5104574

sql(s"""SELECT DISTINCT SCENARIO_ID FROM SCENARIO_NR_ANTENNA""").take(100).foreach(println);


