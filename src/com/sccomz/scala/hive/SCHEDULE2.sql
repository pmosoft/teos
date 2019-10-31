DROP TABLE I_SCHEDULE2;

CREATE EXTERNAL TABLE I_SCHEDULE2 (
  TYPE_CD                   STRING
, SCENARIO_ID               INT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/SCHEDULE2';

ALTER TABLE I_SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967) LOCATION '/teos/warehouse/SCHEDULE/SCHEDULE_ID=8459967';
ALTER TABLE I_SCHEDULE DROP PARTITION (SCHEDULE_ID=8459967);

SHOW PARTITIONS I_SCHEDULE;
msck repair table I_SCHEDULE;

SELECT * FROM SCHEDULE;
spark.sql("SELECT * FROM parquet.`/teos/warehouse/SCHEDULE`").take(100).foreach(println);
sql("SELECT * FROM I_SCHEDULE").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);
sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);


