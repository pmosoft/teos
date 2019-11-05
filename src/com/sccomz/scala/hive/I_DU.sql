DROP TABLE I_DU;

CREATE EXTERNAL TABLE I_DU (
  SCENARIO_ID                      INT
, ENB_ID                           STRING
, E_NODEB_NM                       STRING
, PCI_CNT                          INT
, STRMAKER                         STRING
, REG_DT                           STRING
)
PARTITIONED BY (SCHEDULE_ID INT)
STORED AS PARQUET
LOCATION '/teos/warehouse/DU';
--
