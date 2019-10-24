WITH TEMP01 AS (
-------------------
-- 스케줄 수행대상건 조회
-------------------
SELECT A.SCHEDULE_ID, A.SCENARIO_ID
     --, A.BIN_X_CNT, A.BIN_Y_CNT
FROM   SCHEDULE A
WHERE  1=1
AND    A.PROCESS_CD = '20001'
AND   (NVL(A.RU_CNT,0) = 0 OR NVL(A.BIN_X_CNT,0) = 0 OR NVL(A.BIN_Y_CNT,0) = 0)
UNION
SELECT A.SCHEDULE_ID, A.SCENARIO_ID
     --, A.BIN_X_CNT, A.BIN_Y_CNT
FROM   SCHEDULE A
       LEFT JOIN SCHEDULE_EXT B
           ON A.SCHEDULE_ID = B.SCHEDULE_ID
WHERE  1=1
AND    A.PROCESS_CD = '20001'
AND    B.SCHEDULE_ID IS NULL
), TEMP02 AS (
-------------------
-- 시나리오 정보 조회
-------------------
SELECT A.*
     , CASE WHEN B.RESOLUTION IS NULL OR B.RESOLUTION = 0 THEN 10 ELSE B.RESOLUTION END AS RESOLUTION
     , NVL(B.TM_STARTX  , 0.0) AS TM_STARTX
     , NVL(B.TM_STARTY  , 0.0) AS TM_STARTY
     , NVL(B.TM_ENDX    , 0.0) AS TM_ENDX
     , NVL(B.TM_ENDY    , 0.0) AS TM_ENDY
     , NVL(B.TM_ENDX    , 0.0) - NVL(B.TM_STARTX    , 0.0) AS WIDTH
     , NVL(B.TM_ENDY    , 0.0) - NVL(B.TM_STARTY    , 0.0) AS HEIGHT
FROM   TEMP01 A,
       SCENARIO B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
), TEMP03 AS (
-------------------
-- RU 및 면적 산출1
-------------------
SELECT A.*
     , A.WIDTH  / A.RESOLUTION AS NO_BLOCK_W
     , A.HEIGHT / A.RESOLUTION AS NO_BLOCK_H
FROM   TEMP02 A
), TEMP04 AS (
-------------------
-- RU 및 면적 산출2
-------------------
SELECT A.*
     , CASE WHEN MOD(A.WIDTH, A.RESOLUTION) = 0
            THEN (A.RESOLUTION *  NO_BLOCK_W     ) + TM_STARTX
            ELSE (A.RESOLUTION * (NO_BLOCK_W + 1)) + TM_STARTX
       END AS ENDX
     , CASE WHEN MOD(A.HEIGHT, A.RESOLUTION) = 0
            THEN (A.RESOLUTION *  NO_BLOCK_H     ) + TM_STARTY
            ELSE (A.RESOLUTION * (NO_BLOCK_H + 1)) + TM_STARTY
       END AS ENDY
FROM   TEMP03 A
), TEMP05 AS (
-------------------
-- RU 및 면적 산출3
-------------------
SELECT A.*
     , ROUND((A.ENDX - A.TM_STARTX) / A.RESOLUTION) AS BIN_X_CNT
     , ROUND((A.ENDY - A.TM_STARTY) / A.RESOLUTION) AS BIN_Y_CNT
FROM   TEMP04 A
), TEMP06 AS (
-------------------
-- RU 및 면적 산출3
-------------------
SELECT A.*
     , A.BIN_X_CNT * A.BIN_Y_CNT AS BIN_CNT
     , ROUND((A.TM_ENDX-A.TM_STARTX) * (A.TM_ENDY-A.TM_STARTY),0) AS AREA
FROM   TEMP05 A
), TEMP07 AS (
-------------------
-- RU 및 면적 산출4(RU_CNT)
-------------------
SELECT T_SCENARIO.SCENARIO_ID
     , COUNT(*) AS RU_CNT
FROM   TEMP01   T_SCENARIO
     , DU       T_DU
     , SITE     T_SITE
     ,(SELECT SCENARIO_ID
            , ENB_ID
            , PCI
            , PCI_PORT
            , RU_ID
            , MAX(MAKER)               AS MAKER
            , MAX(REPEATERATTENUATION) AS REPEATERATTENUATION
            , MAX(REPEATERPWRRATIO)    AS REPEATERPWRRATIO
            , MAX(RU_SEQ)              AS RU_SEQ
       FROM   RU
       WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM TEMP01)
       GROUP BY SCENARIO_ID, ENB_ID, PCI, PCI_PORT, RU_ID
       ) T_RU
 WHERE T_DU.SCENARIO_ID       = T_SCENARIO.SCENARIO_ID
   AND T_DU.SCENARIO_ID       = T_RU.SCENARIO_ID
   AND T_DU.ENB_ID            = T_RU.ENB_ID
   AND T_RU.SCENARIO_ID       = T_SITE.SCENARIO_ID
   AND T_RU.ENB_ID            = T_SITE.ENB_ID
   AND T_RU.PCI               = T_SITE.PCI
   AND T_RU.PCI_PORT          = T_SITE.PCI_PORT
   AND T_RU.RU_ID             = T_SITE.RU_ID
   AND T_SITE.TYPE            IN ('RU', 'RU_N')
   AND T_SITE.STATUS          = 1
GROUP BY T_SCENARIO.SCENARIO_ID
)
--SELECT *
SELECT
       A.SCHEDULE_ID
     , A.BIN_X_CNT
     , A.BIN_Y_CNT
     , A.BIN_CNT
     , A.AREA
     , NVL(B.RU_CNT,0) AS RU_CNT
FROM   TEMP06 A
     , TEMP07 B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID(+)