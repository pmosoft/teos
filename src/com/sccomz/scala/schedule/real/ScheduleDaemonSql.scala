package com.sccomz.scala.schedule.real

object ScheduleDaemonSql {



def selectSchedule10001() = {
s"""
WITH TEMP01 AS (
-------------------
-- 스케줄 수행대상건 조회
-------------------
SELECT B.JOB_WEIGHT, A.SCHEDULE_ID
     , ROW_NUMBER() OVER (PARTITION BY B.JOB_WEIGHT ORDER BY B.JOB_WEIGHT, A.SCHEDULE_ID) AS GNUM
FROM   SCHEDULE A
     , SCHEDULE_EXT B
WHERE  A.SCHEDULE_ID = B.SCHEDULE_ID
AND    A.PROCESS_CD = '20001'
AND    A.TYPE_CD IN (SELECT TYPE_CD FROM SCHEDULE_TYPE_CD WHERE USE_YN = 'Y')
ORDER BY B.JOB_WEIGHT, A.SCHEDULE_ID
), TEMP02 AS (
-------------------
-- 스케줄 수행중인건 조회
-------------------
SELECT COUNT(CASE WHEN B.JOB_WEIGHT = 3 THEN 1 ELSE 0 END) AS H_JOB 
     , COUNT(CASE WHEN B.JOB_WEIGHT = 2 THEN 1 ELSE 0 END) AS M_JOB
     , COUNT(CASE WHEN B.JOB_WEIGHT = 1 THEN 1 ELSE 0 END) AS L_JOB
FROM   SCHEDULE A
     , SCHEDULE_EXT B
WHERE  A.SCHEDULE_ID = B.SCHEDULE_ID
AND    A.PROCESS_CD IN ('20002','20003')
AND    A.TYPE_CD IN (SELECT TYPE_CD FROM SCHEDULE_TYPE_CD WHERE USE_YN = 'Y')
), TEMP03 AS (
-------------------
-- 스케줄 수행 가능건 조회
-------------------
SELECT JOB_H_MAX_CNT - H_JOB AS H_EXE_CNT
     , JOB_M_MAX_CNT - M_JOB AS M_EXE_CNT
     , JOB_L_MAX_CNT - L_JOB AS L_EXE_CNT
FROM   TEMP02 A
     ,(SELECT JOB_H_MAX_CNT, JOB_M_MAX_CNT, JOB_L_MAX_CNT
       FROM   SCHEDULE_WEIGHT
       WHERE  BASE_DT = (SELECT MAX(BASE_DT) FROM SCHEDULE_WEIGHT)) B
), TEMP04 AS (
-------------------
-- 스케줄 수행 가능건 조회
-------------------
SELECT A.*
     , B.*
     , CASE WHEN A.JOB_WEIGHT = 3 AND GNUM <= H_EXE_CNT THEN 'Y' 
            WHEN A.JOB_WEIGHT = 2 AND GNUM <= M_EXE_CNT THEN 'Y'
            WHEN A.JOB_WEIGHT = 1 AND GNUM <= L_EXE_CNT THEN 'Y'
            ELSE 'N' 
       END AS EXE_YN  
FROM   TEMP01 A, TEMP03 B
)
SELECT * FROM TEMP04 WHERE EXE_YN = 'Y'
"""
  }

def selectBinRuCount() = {
s"""
WITH TEMP01 AS (
-------------------
-- 스케줄 수행대상건 조회
-------------------
---------------------------------------------------------
-- orgin
---------------------------------------------------------
SELECT A.SCHEDULE_ID, A.SCENARIO_ID, A.TYPE_CD, B.BD_YN
     --, A.BIN_X_CNT, A.BIN_Y_CNT
FROM   SCHEDULE A
     , SCHEDULE_TYPE_CD B
WHERE  1=1
AND    A.TYPE_CD = B.TYPE_CD
AND    A.PROCESS_CD IN ('20001','20003','20004','20009')
-- 테스트를 위하여 아래 주석처리. 운영시 주석해제요.
--AND    A.PROCESS_CD = '20001'
--AND   (NVL(A.RU_CNT,0) = 0 OR NVL(A.BIN_X_CNT,0) = 0 OR NVL(A.BIN_Y_CNT,0) = 0)
AND    B.USE_YN = 'Y'
UNION
SELECT A.SCHEDULE_ID, A.SCENARIO_ID, A.TYPE_CD, A.BD_YN
FROM  (SELECT A.SCHEDULE_ID, A.SCENARIO_ID, A.TYPE_CD, B.BD_YN
            --, A.BIN_X_CNT, A.BIN_Y_CNT
       FROM   SCHEDULE A
            , SCHEDULE_TYPE_CD B
       WHERE  1=1
       AND    A.TYPE_CD = B.TYPE_CD
       AND    A.PROCESS_CD = '20001'
       AND    B.USE_YN = 'Y'
       ) A
       LEFT JOIN SCHEDULE_EXT B
           ON A.SCHEDULE_ID = B.SCHEDULE_ID
WHERE  1=1
AND    B.SCHEDULE_ID IS NULL
-----------------------------------------------
--SELECT A.SCHEDULE_ID, A.SCENARIO_ID, A.TYPE_CD, B.BD_YN
--     --, A.BIN_X_CNT, A.BIN_Y_CNT
--FROM   SCHEDULE A
--     , SCHEDULE_TYPE_CD B
--WHERE  1=1
--AND    A.TYPE_CD = B.TYPE_CD
--AND    A.SCHEDULE_ID IN (8460964,8460966,8460968)
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
     , B.BUILDINGANALYSIS3D_RESOLUTION
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
SELECT T_SCENARIO.SCHEDULE_ID
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
GROUP BY T_SCENARIO.SCHEDULE_ID
), TEMP08 AS (
-------------------
-- 빌딩 면적 산출
-------------------
SELECT
       A.SCHEDULE_ID
     , ROUND(SUM(B.BD_FLOOR_MAX * B.BD_AREA)) AS BD_AREA
     --, ADDR_ADM_DONG
     --, BD_FLOOR_MAX
     --, BD_AREA
     --, WTM_SX
     --, WTM_SY
     --, WTM_EX
     --, WTM_EY
FROM   TEMP02  A
     , BD_LOSS B
WHERE  A.BD_YN = 'Y'
AND    B.WTM_EX >= TM_STARTX
AND    B.WTM_SX <= TM_ENDX
AND    B.WTM_EY >= TM_STARTY
AND    B.WTM_SY <= TM_ENDY
GROUP BY A.SCHEDULE_ID
), TEMP09 AS (
-------------------
-- 잡무게 산출
-------------------
SELECT
       A.SCHEDULE_ID
     , A.TYPE_CD
     , A.BIN_X_CNT
     , A.BIN_Y_CNT
     , A.BIN_CNT
     , A.RESOLUTION
     , A.AREA
     , NVL(B.RU_CNT,0)             AS RU_CNT
     , NVL(C.BD_AREA,0)            AS BD_AREA
     , CASE WHEN A.BD_YN = 'N'
            THEN A.BIN_CNT * NVL(B.RU_CNT,0)
            ELSE A.BIN_CNT * (NVL(C.BD_AREA,0) / POWER(A.BUILDINGANALYSIS3D_RESOLUTION,2))
       END  AS JOB_THRESHOLD
     , A.BD_YN
     , A.BUILDINGANALYSIS3D_RESOLUTION
FROM   TEMP06 A
     , TEMP07 B
     , TEMP08 C
WHERE  A.SCHEDULE_ID = B.SCHEDULE_ID(+)
AND    A.SCHEDULE_ID = C.SCHEDULE_ID(+)
)
SELECT A.*
     , CASE WHEN JOB_THRESHOLD >= B.JOB_H_THRESHOLD THEN 3
            WHEN JOB_THRESHOLD <= B.JOB_L_THRESHOLD THEN 1
            ELSE 2
       END AS JOB_WEIGHT
     , B.*
FROM   TEMP09 A
     ,(SELECT JOB_H_THRESHOLD, JOB_M_THRESHOLD, JOB_L_THRESHOLD
       FROM   SCHEDULE_WEIGHT
       WHERE  BASE_DT = (SELECT MAX(BASE_DT) FROM SCHEDULE_WEIGHT)) B
"""
}

def updateScheduleBinRuCnt(scheduleId:String,binXCnt:String,binYCnt:String,ruCnt:String) = {
s"""
UPDATE SCHEDULE
   SET BIN_X_CNT   = ${binXCnt}
     , BIN_Y_CNT   = ${binYCnt}
     , RU_CNT      = ${ruCnt}
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

def insertScheduleExt(scheduleId:String,jobWeight:String,jobThreshold:String) = {
s"""
INSERT INTO SCHEDULE_EXT
SELECT
       ${scheduleId}     AS SCHEDULE_ID           -- 스케줄ID
     , ${jobWeight}      AS JOB_WEIGHT            -- 잡가중치(3:상, 2:중, 1:하)
     , ${jobThreshold}   AS JOB_THRESHOLD         -- 임계치
     , SYSDATE           AS REG_DT                -- 등록일시
     , 'ADMIN'           AS REG_USER_ID           -- 등록자
     , SYSDATE           AS MOD_DT                -- 수정일시
     , 'ADMIN'           AS MOD_USER_ID           -- 수정자
FROM   DUAL
"""
}

def deleteScheduleExt(scheduleId:String) = {
s"""
DELETE SCHEDULE_EXT WHERE SCHEDULE_ID = ${scheduleId}
"""
}


def test01() = {
"""

SELECT
 ADDR_ADM_DONG
,BD_FLOOR_MAX
,BD_AREA
,WTM_SX
,WTM_SY
,WTM_EX
,WTM_EY
FROM BD_LOSS
WHERE WTM_EX >= 201431.13034243
AND   WTM_SX <= 202386.13034243
AND   WTM_EY >= 445365.04232378
AND   WTM_SY <= 446098.04232378


"""
}



}