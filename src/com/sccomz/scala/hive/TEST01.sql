DROP TABLE TEST02;

CREATE EXTERNAL TABLE TEST02 (
  BIN_ID                            STRING
, BIN_X                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/TEST02';


CREATE EXTERNAL TABLE TEST03 (
  RU_ID                            STRING
, BIN_X                             FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/TEST03';


insert into TEST03 partition (schedule_id)
select '111'
     , 111
     , '111'
"""; sql(qry);



var qry = "";
import spark.implicits._
import spark.sql
sql("set hive.exec.dynamic.partition.mode=nonstrict");

qry = s"""
insert into TEST02 partition (schedule_id)
select '222'
     , 222
     , 222
"""; sql(qry);

LOAD DATA INPATH '/disk2/etl/LOS_ENG_RESULT2_8459967_11111.dat'
INTO TABLE LOS_ENG_RESULT2 PARTITION (SCHEDULE_ID=8459967, ru_id='11111');



