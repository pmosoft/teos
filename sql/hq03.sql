SELECT COUNT(*) FROM result_nr_2d_los_ru;

SELECT x_point, y_point, los FROM result_nr_2d_los ORDER BY x_point, y_point
;

SELECT * FROM result_nr_2d_los
;

SELECT * FROM scenario_nr_ru
;

select * from i_los_eng_result;

ALTER TABLE I_LOS_ENG_RESULT DROP IF EXISTS PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284);

ALTER TABLE I_LOS_ENG_RESULT ADD PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284) LOCATION '/teos/warehouse/LOS_ENG_RESULT/SCHEDULE_ID=8460062/RU_ID=1012242284';


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
LOCATION '/teos/warehouse/LOS_ENG_RESULT';
