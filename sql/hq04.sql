CREATE EXTERNAL TABLE I_SCHEDULE2 (
  TYPE_CD                   STRING
, SCENARIO_ID               INT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/SCHEDULE2';


insert into table I_SCHEDULE2 partition (SCHEDULE_ID=1000) select 'sc001',1000;


select * from I_SCHEDULE2;