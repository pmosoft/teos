-----------------------------------------
-- I_SCENARIO_NR_RU
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO_NR_RU;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU(
 SCENARIO_ID                   NUMERIC     NOT NULL
,ENB_ID                        VARCHAR(10) NOT NULL
,PCI                           NUMERIC     NOT NULL
,PCI_PORT                      NUMERIC     NOT NULL
,RU_ID                         VARCHAR(48) NOT NULL
,MAKER                         VARCHAR(30)
,SECTOR_ORD                    NUMERIC     NOT NULL
,REPEATERATTENUATION           NUMERIC
,REPEATERPWRRATIO              NUMERIC
,RU_SEQ                        NUMERIC
,RADIUS                        NUMERIC
,FEEDER_LOSS                   NUMERIC
,NOISEFLOOR                    NUMERIC
,CORRECTION_VALUE              NUMERIC
,FADE_MARGIN                   NUMERIC
,XPOSITION                     VARCHAR(40)
,YPOSITION                     VARCHAR(40)
,HEIGHT                        NUMERIC
,SITE_ADDR                     VARCHAR(500)
,TYPE                          VARCHAR(10)
,STATUS                        VARCHAR(10)
,SISUL_CD                      VARCHAR(50)
,MSC                           NUMERIC
,BSC                           NUMERIC
,NETWORKID                     NUMERIC
,USABLETRAFFICCH               NUMERIC
,SYSTEMID                      NUMERIC
,RU_TYPE                       NUMERIC
,FA_MODEL_ID                   NUMERIC
,NETWORK_TYPE                  NUMERIC
,RESOLUTION                    NUMERIC
,FA_SEQ                        NUMERIC
,SITE_STARTX                   NUMERIC
,SITE_STARTY                   NUMERIC
,SITE_ENDX                     NUMERIC
,SITE_ENDY                     NUMERIC
,X_BIN_CNT                     NUMERIC
,Y_BIN_CNT                     NUMERIC
);

CREATE INDEX I_SCENARIO_NR_RU_IDX ON PUBLIC.I_SCENARIO_NR_RU USING BTREE (SCENARIO_ID,ENB_ID,PCI,PCI_PORT,RU_ID,SECTOR_ORD);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU TO POSTGRES;
SCENARIO_NR_RU
-----------------------------------------
-- I_SCENARIO_NR_RU
-----------------------------------------
-- DROP TABLE PUBLIC.I_SCENARIO_NR_RU;

CREATE TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE(
  SCENARIO_ID                   NUMERIC     NOT NULL
, RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, CACHE_YN                      VARCHAR(1)      NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_SCENARIO_NR_RU_CACHE_IDX ON PUBLIC.I_SCENARIO_NR_RU_CACHE USING BTREE (SCENARIO_ID,RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE TO POSTGRES;

-----------------------------------------
-- I_SCENARIO_NR_RU_CACHE
-----------------------------------------
-- DROP TABLE PUBLIC.I_NR_RU_CACHE;

CREATE TABLE PUBLIC.I_NR_RU_CACHE (
  RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40) NOT NULL
, YPOSITION                     VARCHAR(40) NOT NULL
, HEIGHT                        NUMERIC     NOT NULL
, RESOLUTION                    NUMERIC     NOT NULL
, MOBILE_HEIGHT                 NUMERIC     NOT NULL
, REG_DT                        DATE            NULL
, MODIFY_DT                     DATE            NULL
);

CREATE INDEX I_NR_RU_CACHE_IDX ON PUBLIC.I_NR_RU_CACHE USING BTREE (RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_NR_RU_CACHE TO POSTGRES;

