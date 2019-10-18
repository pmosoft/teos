DROP TABLE I_SCHEDULE;

CREATE EXTERNAL TABLE I_SCHEDULE (
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
, RESULT_TIME               INT
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
LOCATION '/teos/warehouse/SCHEDULE';



CREATE EXTERNAL TABLE I_SCHEDULE (
  TYPE_CD                   STRING
, SCENARIO_ID               STRING
, USER_ID                   STRING
, PRIORITIZE                STRING
, PROCESS_CD                STRING
, PROCESS_MSG               STRING
, SCENARIO_PATH             STRING
, REG_DT                    STRING
, MODIFY_DT                 STRING
, RETRY_CNT                 STRING
, SERVER_ID                 STRING
, BIN_X_CNT                 STRING
, BIN_Y_CNT                 STRING
, RU_CNT                    STRING
, ANALYSIS_WEIGHT           STRING
, PHONE_NO                  STRING
, RESULT_TIME               STRING
, TILT_PROCESS_TYPE         STRING
, GEOMETRYQUERY_SCHEDULE_ID STRING
, RESULT_BIT                STRING
, INTERWORKING_INFO         STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/SCHEDULE';


DROP TABLE I_SCHEDULE;

CREATE EXTERNAL TABLE I_SCHEDULE (
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
LOCATION '/teos/warehouse/SCHEDULE';





--ALTER TABLE SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967);
ALTER TABLE SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967) LOCATION '/teos/warehouse/SCHEDULE/SCHEDULE_ID=8459967';
ALTER TABLE SCHEDULE DROP PARTITION (SCHEDULE_ID=8459967);

SHOW PARTITIONS SCHEDULE;

msck repair table SCHEDULE;


LOAD DATA INPATH '/user/teos/sam/entity/SCHEDULE_8443705.dat' INTO TABLE SCHEDULE;
LOAD DATA INPATH '/teos/warehouse/SCHEDULE' INTO TABLE SCHEDULE;


SELECT * FROM SCHEDULE;

spark.sql("SELECT * FROM parquet.`/teos/warehouse/SCHEDULE`").take(100).foreach(println);

sql("SELECT * FROM I_SCHEDULE").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);

sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);

sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);



