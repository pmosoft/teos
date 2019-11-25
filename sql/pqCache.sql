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
  SCHEDULE_ID                   NUMERIC     NOT NULL
, SCENARIO_ID                   NUMERIC     NOT NULL
, RU_ID                         VARCHAR(48) NOT NULL
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


;

-----------------------------------
-- INS SCENARIO_NR_RU_CACHE
-----------------------------------
WITH TEMP01 AS
(
SELECT A.SCENARIO_ID, A.RU_ID, A.XPOSITION, A.YPOSITION, A.HEIGHT, A.RESOLUTION, B.MOBILE_HEIGHT
FROM   I_SCENARIO_NR_RU A,
      (SELECT SCENARIO_ID, MAX(HEIGHT) AS MOBILE_HEIGHT
       FROM I_MOBILE_PARAMETER
       WHERE SCENARIO_ID = 5105173
       GROUP BY SCENARIO_ID
       ) B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.SCENARIO_ID = 5105173
) , TEMP02 AS (
SELECT A.*
     , CASE WHEN B.RU_ID IS NULL THEN 'N' ELSE 'Y' END AS CACHE_YN
FROM   TEMP01 A
       LEFT JOIN
       I_NR_RU_CACHE B
       ON  A.RU_ID         = B.RU_ID
       AND A.XPOSITION     = B.XPOSITION
       AND A.YPOSITION     = B.YPOSITION
       AND A.HEIGHT        = B.HEIGHT
       AND A.RESOLUTION    = B.RESOLUTION
       AND A.MOBILE_HEIGHT = B.MOBILE_HEIGHT
)
INSERT INTO I_SCENARIO_NR_RU_CACHE SELECT * FROM TEMP02
;

-----------------------------------
-- EXT SCENARIO_NR_RU
-----------------------------------
SELECT *
FROM   I_SCENARIO_NR_RU A
     , I_SCENARIO_NR_RU_CACHE B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.RU_ID = B.RU_ID
AND    B.CACHE_YN = 'N'
;

-----------------------------------
-- INS RU_CACHE
-----------------------------------
INSERT INTO I_NR_RU_CACHE
SELECT '' AS SCHDULE_ID
     , A.*
FROM   I_SCENARIO_NR_RU_CACHE A
WHERE  A.SCENARIO_ID = ''
AND    A.CACHE_YN = 'N'
;

-----------------------------------
-- COPY NR_RU
-----------------------------------
INSERT INTO RESULT_NR_2D_LOS_RU (SCHEDULE_ID=${scheduleId},RU_ID=${ruId})
SELECT RX_TM_XPOS
     , RX_TM_YPOS
     , RZ
     , VALUE
     , THETA
     , PHI
     , IS_BLD
FROM   I_RESULT_NR_2D_LOS_RU A
     , I_SCENARIO_NR_RU_CACHE B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.RU_ID = B.RU_ID
AND    B.CACHE_YN = 'Y'
;

--------------------------------------------------------------------------------
)
SELECT * FROM TEMP01
;

DROP TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE;

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
)
;

CREATE INDEX I_SCENARIO_NR_RU_CACHE_IDX ON PUBLIC.I_SCENARIO_NR_RU_CACHE USING BTREE (SCENARIO_ID,RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_SCENARIO_NR_RU_CACHE TO POSTGRES;

-----------------------------------------
-- I_SCENARIO_NR_RU_CACHE
-----------------------------------------
DROP TABLE PUBLIC.I_NR_RU_CACHE;

CREATE TABLE PUBLIC.I_NR_RU_CACHE(
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


select scenario_id, xposition, yposition, height, resolution
from   i_scenario_nr_ru a
;


CREATE TABLE PUBLIC.I_NR_RU_CACHE(
  RU_ID                         VARCHAR(48) NOT NULL
, XPOSITION                     VARCHAR(40)
, YPOSITION                     VARCHAR(40)
, HEIGHT                        NUMERIC
, RESOLUTION                    NUMERIC
, MOBILE_HEIGHT                 NUMERIC
, REG_DT                        DATE NULL
, MODIFY_DT                     DATE NULL
);

CREATE INDEX I_NR_RU_CACHE_IDX ON PUBLIC.I_NR_RU_CACHE USING BTREE (RU_ID,XPOSITION,YPOSITION,HEIGHT,RESOLUTION,MOBILE_HEIGHT);
ALTER TABLE PUBLIC.I_NR_RU_CACHE OWNER TO POSTGRES;
GRANT ALL ON TABLE PUBLIC.I_NR_RU_CACHE TO POSTGRES;




select scenario_id, max(height) as mobile_height
from i_mobile_parameter
group by scenario_id
;



