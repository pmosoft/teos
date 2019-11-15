DROP TABLE I_RESULT_NR_2D_LOS_RU;

CREATE EXTERNAL TABLE I_RESULT_NR_2D_LOS_RU (
  SCENARIO_ID                      INT
, BIN_ID                           STRING
, RX_TM_XPOS                       INT
, RX_TM_YPOS                       INT
, RZ                               FLOAT
, RESOLUTION                       INT
, VALUE                            INT
, THETA                            FLOAT
, PHI                              FLOAT
, RU_ID                            STRING
, TX_TM_XPOS                       FLOAT
, TX_TM_YPOS                       FLOAT
, TZ                               FLOAT
, SCHEDULE_ID                      INT
)

SELECT * FROM I_RESULT_NR_2D_LOS_RU;


DROP TABLE RESULT_NR_2D_LOS_RU;

CREATE EXTERNAL TABLE RESULT_NR_2D_LOS_RU
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
LOCATION '/TEOS/warehouse/RESULT_NR_2D_LOS_RU';


sql(s"""
CREATE EXTERNAL TABLE RESULT_NR_2D_LOS_RU
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
LOCATION '/TEOS/warehouse/RESULT_NR_2D_LOS_RU'
""").take(100).foreach(println);

hadoop fs -ls /TEOS/warehouse/RESULT_NR_2D_LOS_RU;
hadoop fs -chmod -R 777 /TEOS/warehouse/RESULT_NR_2D_LOS_RU;

DROP TABLE I_LOS_ENG_RESULT;

CREATE EXTERNAL TABLE I_LOS_ENG_RESULT (
  BIN_ID                            STRING
, BIN_X                             FLOAT
, BIN_Y                             FLOAT
, BIN_Z                             FLOAT
, BLD_ID                            STRING
, BIN_SIZE                          INT
, LOS                               BOOLEAN
, IN_BLD                            BOOLEAN
, THETA_DEG                         INT
, PHI_DEG                           INT
, SECTOR_X                          FLOAT
, SECTOR_Y                          FLOAT
, SECTOR_Z                          FLOAT
)
PARTITIONED BY (SCHEDULE_ID INT, RU_ID STRING)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/LOS_ENG_RESULT';


CREATE EXTERNAL TABLE RESULT_NR_2D_LOS_RU_BAK(
  SCENARIO_ID INT, 
  BIN_ID STRING, 
  RX_TM_XPOS INT, 
  RX_TM_YPOS INT, 
  RZ FLOAT, 
  RESOLUTION INT, 
  VALUE INT, 
  THETA FLOAT, 
  PHI FLOAT, 
  RU_ID STRING, 
  TX_TM_XPOS FLOAT, 
  TX_TM_YPOS FLOAT, 
  TZ FLOAT, 
  IS_BLD STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/TEOS/warehouse/RESULT_NR_2D_LOS_RU_BAK';
  