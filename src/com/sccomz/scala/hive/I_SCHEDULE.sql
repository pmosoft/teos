DROP TABLE SCHEDULE;

CREATE EXTERNAL TABLE SCHEDULE (
  TYPE_CD                   STRING
, SCENARIO_ID               INT
, USER_ID                   STRING
, PRIORITIZE                STRING
, PROCESS_CD                STRING
, PROCESS_MSG               STRING
, SCENARIO_PATH             STRING
, REG_DT                    STRING
, MODIFY_DT                 STRING
, RETRY_CNT                 INT
, SERVER_ID                 STRING
, BIN_X_CNT                 INT
, BIN_Y_CNT                 INT
, RU_CNT                    INT
, ANALYSIS_WEIGHT           INT
, PHONE_NO                  STRING
, RESULT_TIME               FLOAT
, TILT_PROCESS_TYPE         INT
, GEOMETRYQUERY_SCHEDULE_ID INT
, RESULT_BIT                STRING
, INTERWORKING_INFO         STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
--COMMENT 'SCHEDULE'
--ROW FORMAT DELIMITED
--FIELDS TERMINATED BY '|'
--LINES TERMINATED BY '\n'
STORED AS PARQUET
--STORED AS TEXTFILE;
LOCATION '/TEOS/warehouse/SCHEDULE';

ALTER TABLE I_SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967) LOCATION '/TEOS/warehouse/SCHEDULE/SCHEDULE_ID=8459967';
ALTER TABLE I_SCHEDULE DROP PARTITION (SCHEDULE_ID=8459967);

SHOW PARTITIONS I_SCHEDULE;
msck repair table I_SCHEDULE;

SELECT * FROM SCHEDULE;
spark.sql("SELECT * FROM parquet.`/teos/warehouse/SCHEDULE`").take(100).foreach(println);
sql("SELECT * FROM I_SCHEDULE").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);

DROP TABLE SCHEDULE_T2;

set hive.exec.dynamic.partition.mode=nonstrict;

sql(s"""set hive.exec.dynamic.partition.mode=nonstrict""").take(100).foreach(println);

sql(s"""DROP TABLE SCHEDULE_T2""").take(100).foreach(println);

sql(s"""
CREATE EXTERNAL TABLE SCHEDULE_T2 (
  TYPE_CD                   STRING
, SCENARIO_ID               INT
, USER_ID                   STRING
, PRIORITIZE                STRING
, PROCESS_CD                STRING
, PROCESS_MSG               STRING
, SCENARIO_PATH             STRING
, REG_DT                    STRING
, MODIFY_DT                 STRING
, RETRY_CNT                 INT
, SERVER_ID                 STRING
, BIN_X_CNT                 INT
, BIN_Y_CNT                 INT
, RU_CNT                    INT
, ANALYSIS_WEIGHT           INT
, PHONE_NO                  STRING
, RESULT_TIME               FLOAT
, TILT_PROCESS_TYPE         INT
, GEOMETRYQUERY_SCHEDULE_ID INT
, RESULT_BIT                STRING
, INTERWORKING_INFO         STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/SCHEDULE_T2'
""").take(100).foreach(println);

sql(s"""INSERT INTO SCHEDULE_T2 PARTITION (SCHEDULE_ID) SELECT * FROM SCHEDULE_T1""").take(100).foreach(println);



