DROP TABLE RU;

CREATE EXTERNAL TABLE RU (
  SCENARIO_ID                      INT
, ENB_ID                           STRING
, PCI                              INT
, PCI_PORT                         INT
, RU_ID                            STRING
, MAKER                            STRING
, SITE_TYPE                        STRING
, PAIR_ENODEB                      INT
, REPEATERATTENUATION              INT
, REPEATERPWRRATIO                 INT
, RU_NM                            STRING
, FA_SEQ                           INT
, SECTOR_ORD                       INT
, RU_SEQ                           INT
, RRH_SEQ                          INT
, REG_DT                           STRING
, SWING_YN                         INT
, ANT_CHK_YN                       INT
, TILT_YN                          INT
, FA_SEQ_ORG                       INT
)
PARTITIONED BY (SCENARIO_ID INT)
COMMENT 'SCENARIO'
STORED AS PARQUET
LOCATION '/teos/warehouse/RU';
--
ALTER TABLE SCHEDULE ADD PARTITION (SCHEDULE_ID=8459967);
ALTER TABLE SCENARIO ADD PARTITION (SCENARIO_ID=8459967) LOCATION '/teos/warehouse/SCENARIO/SCENARIO_ID=8459967';
ALTER TABLE SCENARIO DROP PARTITION (SCENARIO_ID=8459967);