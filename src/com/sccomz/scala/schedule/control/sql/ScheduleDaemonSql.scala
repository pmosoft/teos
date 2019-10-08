package com.sccomz.scala.schedule.control.sql

object ScheduleDaemonSql {

def insertScheduleExt() = {
"""


"""
}

def selectSchedule10001() = {
s"""
    -- SELECT SCHEDULE_ID                            AS SCHEDULE_ID
    --      , TYPE_CD                                AS TYPE_CD
    --      , SCENARIO_ID                            AS SCENARIO_ID
    --      , USER_ID                                AS USER_ID
    --      , PRIORITIZE                             AS PRIORITIZE
    --      , PROCESS_CD                             AS PROCESS_CD
    --      , PROCESS_MSG                            AS PROCESS_MSG
    --      , SCENARIO_PATH                          AS SCENARIO_PATH
    --      , TO_CHAR(REG_DT, 'YYYYMMDDHH24MISS')    AS REG_DT
    --      , TO_CHAR(MODIFY_DT, 'YYYYMMDDHH24MISS') AS MODIFY_DT
    -- FROM  (SELECT ROW_NUMBER() OVER(ORDER BY SCHEDULE_ID ASC, TYPE_CD ASC, PRIORITIZE ASC, RU_CNT ASC) AS ROW_NUM
    --             , SCHEDULE_ID
    --             , TYPE_CD
    --             , SCENARIO_ID
    --             , USER_ID
    --             , PRIORITIZE
    --             , PROCESS_CD
    --             , PROCESS_MSG
    --             , SCENARIO_PATH
    --             , REG_DT
    --             , MODIFY_DT
    --        FROM   SCHEDULE
    --        WHERE  PROCESS_CD IN ('10001')
    --        --AND    TYPE_CD IN ('SC001','SC051')
    --       )
    -- WHERE ROW_NUM <= 2
    -- ORDER BY ROW_NUM

SELECT *
FROM
   (SELECT A.SCHEDULE_ID
         , A.TYPE_CD
         , A.SCENARIO_ID
         , A.PROCESS_CD
         , A.PROCESS_MSG
         , B.SCENARIO_NM
         , B.SIDO                              --
         , B.SIGUGUN                           --
         , B.DONG                              --
         , ROUND((B.TM_ENDX-B.TM_STARTX) * (B.TM_ENDY-B.TM_STARTY),0) AS AREA
         , A.RU_CNT
         , ROUND(A.RESULT_TIME,0) AS RESULT_TIME
         , A.PRIORITIZE
         , A.REG_DT
         , A.MODIFY_DT
    FROM   SCHEDULE A
         , SCENARIO B
    WHERE  A.SCENARIO_ID = B.SCENARIO_ID
    AND    A.PROCESS_CD > '10001'
    --AND    C.DONG = '대치2동'
    --AND    TYPE_CD IN ('SC001','SC051')
    --AND    A.RU_CNT > 0
    --AND    A.RESULT_TIME > 0
    --AND    A.REG_DT > SYSDATE -30
   )
--ORDER BY SCHEDULE_ID ,RU_CNT DESC, RESULT_TIME DESC, AREA DESC
--ORDER BY RU_CNT DESC, RESULT_TIME DESC, AREA DESC
ORDER BY RESULT_TIME DESC, AREA DESC
		"""
}

def selectSchedule10003(scheduleId:String) = {
s"""
SELECT ROW_NUMBER() OVER(ORDER BY SCHEDULE_ID ASC, TYPE_CD ASC, PRIORITIZE ASC, RU_CNT ASC) AS ROW_NUM
     , SCHEDULE_ID
     , TYPE_CD
     , SCENARIO_ID
     , USER_ID
     , PRIORITIZE
     , PROCESS_CD
     , PROCESS_MSG
     , SCENARIO_PATH
     , REG_DT
     , MODIFY_DT
FROM   SCHEDULE
WHERE  PROCESS_CD IN ('10002','10003') -- 분석대상,분석중
--AND    TYPE_CD IN ('SC001','SC051')
"""
  }

def selectBinCount() = {
s"""
WITH TEMP01 AS (
-------------------
-- 스케줄 수행대상건 조회
-------------------
SELECT A.SCHEDULE_ID, A.SCENARIO_ID
     --, A.BIN_X_CNT, A.BIN_Y_CNT
FROM   SCHEDULE A
WHERE  1=1
AND    A.PROCESS_CD = '10001'
AND   (NVL(A.RU_CNT,0) = 0 OR NVL(A.BIN_X_CNT,0) = 0 OR NVL(A.BIN_Y_CNT,0) = 0)
UNION
SELECT A.SCHEDULE_ID, A.SCENARIO_ID
     --, A.BIN_X_CNT, A.BIN_Y_CNT
FROM   SCHEDULE A
       LEFT JOIN SCHEDULE_EXT B
           ON A.SCHEDULE_ID = B.SCHEDULE_ID
WHERE  1=1
AND    A.PROCESS_CD = '10001'
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
     , A.AREA
     , NVL(B.RU_CNT,0) AS RU_CNT
FROM   TEMP06 A
     , TEMP07 B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID(+)
"""
}

def selectRuCount(scheduleId:String) = {
s"""
SELECT COUNT(*) AS RU_CNT
FROM   SCENARIO T_SCENARIO
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
       WHERE  SCENARIO_ID = ${scheduleId}
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
   and T_SCENARIO.SCENARIO_ID = ${scheduleId}
 ORDER BY T_RU.ENB_ID, T_RU.PCI, T_RU.PCI_PORT, T_RU.RU_ID
"""
}

def updateScheduleBinCnt(scheduleId:String,binXCnt:String,binYCnt:String,ruCnt:String) = {
s"""
UPDATE SCHEDULE
   SET BIN_X_CNT = ${binXCnt}
     , BIN_Y_CNT = ${binYCnt}
     , RU_CNT    = ${ruCnt}
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}


def insertScheduleExt(scheduleId:String,ruCnt:String,area:String) = {
s"""
INSERT INTO SCHEDULE_EXT
WITH TEMP01 AS (
-------------------
-- 스케줄 가중치 조회
-------------------
SELECT
       ${scheduleId}     AS SCHEDULE_ID           -- 스케줄ID
     , 0                 AS JOB_WEIGHT            -- 잡가중치(3:상, 2:중, 1:하)
     , JOB_H_THRESHOLD   AS JOB_H_THRESHOLD       -- 하이잡임계치(1-5 사이의 값)
     , JOB_M_THRESHOLD   AS JOB_M_THRESHOLD       -- 미들잡임계치(1-5 사이의 값)
     , JOB_L_THRESHOLD   AS JOB_L_THRESHOLD       -- 로우잡임계치(1-5 사이의 값)
     , 0                 AS RU_CNT_WEIGHT         -- RU갯수가중치값(1..5)
     , CASE WHEN ${ruCnt} > RU_CNT_W5 THEN 5
            WHEN ${ruCnt} > RU_CNT_W4 THEN 4
            WHEN ${ruCnt} > RU_CNT_W3 THEN 3
            WHEN ${ruCnt} > RU_CNT_W2 THEN 2
            WHEN ${ruCnt} > RU_CNT_W1 THEN 1
            ELSE 1
       END
     , JOB_RU_CNT_WEIGHT AS JOB_RU_CNT_WEIGHT     -- RU갯수가중치
     , ${area}           AS AREA                  -- 면적
     , 0                 AS AREA_WEIGHT           -- 면적가중치값(1..5)
     , JOB_AREA_WEIGHT   AS JOB_AREA_WEIGHT       -- 면적가중치
     , SYSDATE           AS REG_DT                -- 등록일시
     , 'ADMIN'           AS REG_USER_ID           -- 등록자
     , SYSDATE           AS MOD_DT                -- 수정일시
     , 'ADMIN'           AS MOD_USER_ID           -- 수정자
FROM   SCHEDULE_WEIGHT
WHERE  1=1
AND    BASE_DT = (SELECT MAX(BASE_DT) FROM SCHEDULE_WEIGHT)
), TEMP02 AS (
-------------------
-- RU 및 면적 산출3
-------------------









SELECT
       ${scheduleId}     AS SCHEDULE_ID           -- 스케줄ID
     , 0                 AS JOB_WEIGHT            -- 잡가중치(3:상, 2:중, 1:하)
     , JOB_H_THRESHOLD   AS JOB_H_THRESHOLD       -- 하이잡임계치(1-5 사이의 값)
     , JOB_M_THRESHOLD   AS JOB_M_THRESHOLD       -- 미들잡임계치(1-5 사이의 값)
     , JOB_L_THRESHOLD   AS JOB_L_THRESHOLD       -- 로우잡임계치(1-5 사이의 값)
     , 0                 AS RU_CNT_WEIGHT         -- RU갯수가중치값(1..5)
     , JOB_RU_CNT_WEIGHT AS JOB_RU_CNT_WEIGHT     -- RU갯수가중치
     , ${area}           AS AREA                  -- 면적
     , 0                 AS AREA_WEIGHT           -- 면적가중치값(1..5)
     , JOB_AREA_WEIGHT   AS JOB_AREA_WEIGHT       -- 면적가중치
     , SYSDATE           AS REG_DT                -- 등록일시
     , 'ADMIN'           AS REG_USER_ID           -- 등록자
     , SYSDATE           AS MOD_DT                -- 수정일시
     , 'ADMIN'           AS MOD_USER_ID           -- 수정자
FROM   SCHEDULE_WEIGHT
WHERE  1=1
AND    BASE_DT = (SELECT MAX(BASE_DT) FROM SCHEDULE_WEIGHT)













 VALUES WHERE SCHEDULE_ID = ${scheduleId}
"""
}


def deleteScheduleExt(scheduleId:String) = {
s"""
DELETE SCHEDULE_EXT WHERE SCHEDULE_ID = ${scheduleId}
"""
}





def test01() = {
s"""
SELECT COUNT(*)
FROM   SCHEDULE
WHERE  PROCESS_CD IN ('10001')
"""
}


}