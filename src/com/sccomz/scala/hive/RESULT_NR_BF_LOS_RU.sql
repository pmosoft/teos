DROP TABLE RESULT_NR_BF_LOS_RU;

CREATE EXTERNAL TABLE RESULT_NR_BF_LOS_RU
(
  RX_TM_XPOS INT          -- BIN의 X좌표(TM좌표)
, RX_TM_YPOS INT          -- BIN의 Y좌표(TM좌표)
, RZ         FLOAT        -- BIN의 해발고도(meter)
, VALUE      INT          -- LOS여부(1, 0)
, THETA      FLOAT        -- 송신점과 수신점간의 정북을 기준으로 반시계 방향으로 측정한 degree 각도
, PHI        FLOAT        -- 송신점과 수신점간의 송신점이 수직일때를 기준으로 Tilt각도를 측정한 degree 각도
, IS_BLD     STRING       -- 수신점BIN이 건물에 포함되어 있는지 여부를 체크(t, f)
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_BF_LOS_RU';


sql(s"""
CREATE EXTERNAL TABLE RESULT_NR_BF_LOS_RU
(
  RX_TM_XPOS INT          -- BIN의 X좌표(TM좌표)
, RX_TM_YPOS INT          -- BIN의 Y좌표(TM좌표)
, RZ         FLOAT        -- BIN의 해발고도(meter)
, VALUE      INT          -- LOS여부(1, 0)
, THETA      FLOAT        -- 송신점과 수신점간의 정북을 기준으로 반시계 방향으로 측정한 degree 각도
, PHI        FLOAT        -- 송신점과 수신점간의 송신점이 수직일때를 기준으로 Tilt각도를 측정한 degree 각도
, IS_BLD     STRING       -- 수신점BIN이 건물에 포함되어 있는지 여부를 체크(t, f)
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_BF_LOS_RU'
""").take(100).foreach(println);

hadoop fs -ls /TEOS/warehouse/RESULT_NR_BF_LOS_RU;
hadoop fs -chmod -R 777 /TEOS/warehouse/RESULT_NR_BF_LOS_RU;





CREATE EXTERNAL TABLE default.result_nr_bf_los_ru(
  ru_id string, 
  tbd_key string, 
  rx_tm_xpos float, 
  rx_tm_ypos float, 
  rx_floorz int, 
  rx_gbh float, 
  theta float, 
  phi float, 
  value int)
PARTITIONED BY ( 
  schedule_id int)


CREATE EXTERNAL TABLE RESULT_NR_BF_LOS_RU_BAK(
  RU_ID STRING, 
  TBD_KEY STRING, 
  RX_TM_XPOS FLOAT, 
  RX_TM_YPOS FLOAT, 
  RX_FLOORZ INT, 
  RX_GBH FLOAT, 
  THETA FLOAT, 
  PHI FLOAT, 
  VALUE INT
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_BF_LOS_RU_BAK';


SELECT * FROM SCHEDULE;
spark.sql("SELECT * FROM parquet./teos/warehouse/SCHEDULE").take(100).foreach(println);
sql("SELECT * FROM I_SCHEDULE").take(100).foreach(println);
spark.sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT SUM(SCENARIO_ID) FROM I_SCHEDULE").take(100).foreach(println);
sql("SELECT scenario_id,COUNT(*) FROM SCENARIO_NR_RU GROUP BY scenario_id").take(100).foreach(println);

DROP TABLE SCHEDULE_T2;

set hive.exec.dynamic.partition.mode=nonstrict;

sql(s"""set hive.exec.dynamic.partition.mode=nonstrict""").take(100).foreach(println);

sql(s"""DROP TABLE SCHEDULE_T2""").take(100).foreach(println);

sql(s"""
CREATE EXTERNAL TABLE RESULT_NR_BF_LOS_RU(
  TBD_KEY STRING, 
  RX_TM_XPOS FLOAT, 
  RX_TM_YPOS FLOAT, 
  RX_FLOORZ INT, 
  RX_GBH FLOAT, 
  THETA FLOAT, 
  PHI FLOAT, 
  VALUE INT
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_BF_LOS_RU'
""").take(100).foreach(println);

sql(s"""INSERT INTO RESULT_NR_BF_LOS_RU_BAK PARTITION (SCHEDULE_ID) SELECT * FROM RESULT_NR_BF_LOS_RU""").take(100).foreach(println);

sql(s"""DROP TABLE RESULT_NR_BF_LOS_RU""").take(100).foreach(println);


/TEOS/warehouse/RESULT_NR_BF_LOS_RU
