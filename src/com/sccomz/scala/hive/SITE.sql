DROP TABLE SITE;

CREATE EXTERNAL TABLE SITE (
  SCENARIO_ID                      INT
, ENB_ID                           STRING
, PCI                              INT
, PCI_PORT                         INT
, SITE_NM                          STRING
, XPOSITION                        STRING
, YPOSITION                        STRING
, HEIGHT                           INT
, BLT_HEIGHT                       INT
, TOWER_HEIGHT                     INT
, SITE_ADDR                        STRING
, TYPE                             STRING
, CORRECTION_VALUE                 INT
, FEEDER_LOSS                      INT
, FADE_MARGIN                      INT
, STATUS                           STRING
, MSC                              INT
, BSC                              INT
, NETWORKID                        INT
, USABLETRAFFICCH                  INT
, SYSTEMID                         INT
, STRYPOS                          STRING
, STRXPOS                          STRING
, ATTENUATION                      INT
, SITE_GUBUN                       STRING
, RU_ID                            STRING
, RADIUS                           INT
, NOISEFLOOR                       INT
, FA_SEQ                           STRING
, RU_TYPE                          INT
, REG_DT                           STRING
, SISUL_CD                         STRING
, TM_XPOSITION                     STRING
, TM_YPOSITION                     STRING
, RU_DIV_CD                        INT
)
PARTITIONED BY (SCENARIO_ID INT)
COMMENT 'SCENARIO'
STORED AS PARQUET
LOCATION '/teos/warehouse/SITE';
--
ALTER TABLE SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967);
ALTER TABLE SCENARIO ADD PARTITION (SCENARIO_ID=8459967) LOCATION '/teos/warehouse/SCENARIO/SCENARIO_ID=8459967';
ALTER TABLE SCENARIO DROP PARTITION (SCENARIO_ID=8459967);