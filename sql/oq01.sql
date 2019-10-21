-------------------------------
-- SCENARIO
-------------------------------
SELECT * FROM SCENARIO
;

SELECT NVL(TRIM(SCENARIO_DESC),'') 
,NVL(UPPER_SCENARIO_ID,0)
FROM SCENARIO
WHERE UPPER_SCENARIO_ID IS NULL
;

SELECT 
       NVL(SCENARIO_ID,0) AS SCENARIO_ID     --  
     , SCENARIO_NM AS SCENARIO_NM                     --   
     , USER_ID                           --  
     , NVL(SYSTEM_ID,0) AS SYSTEM_ID     --  
     , NVL(NETWORK_TYPE,0) AS NETWORK_TYPE     --  
     , SIDO_CD                           --  
     , SIGUGUN_CD                        --  
     , DONG_CD                           --  
     , SIDO                              --  
     , SIGUGUN                           --  
     , DONG                              --  
     , NVL(STARTX,0) AS STARTX           --  
     , NVL(STARTY,0) AS STARTY           --  
     , NVL(ENDX,0) AS ENDX               --  
     , NVL(ENDY,0) AS ENDY               --  
     , NVL(FA_MODEL_ID,0) AS FA_MODEL_ID     --  
     , NVL(FA_SEQ,0) AS FA_SEQ           --  
     , SCENARIO_DESC                     --  
     , NVL(USE_BUILDING,0) AS USE_BUILDING     --  
     , NVL(USE_MULTIFA,0) AS USE_MULTIFA     --  
     , NVL(PRECISION,0) AS PRECISION     --  
     , NVL(PWRCTRLCHECKPOINT,0) AS PWRCTRLCHECKPOINT     --  
     , NVL(MAXITERATIONPWRCTRL,0) AS MAXITERATIONPWRCTRL     --  
     , NVL(RESOLUTION,0) AS RESOLUTION     --  
     , NVL(MODEL_RADIUS,0) AS MODEL_RADIUS     --  
     , TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') AS REG_DT     --  
     , TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') AS MODIFY_DT     --  
     , NVL(UPPER_SCENARIO_ID,0) AS UPPER_SCENARIO_ID     --  
     , NVL(FLOORBUILDING,0) AS FLOORBUILDING     --  
     , NVL(FLOOR,0) AS FLOOR             --  
     , NVL(FLOORLOSS,0) AS FLOORLOSS     --  
     , NVL(SCENARIO_SUB_ID,0) AS SCENARIO_SUB_ID     --  
     , NVL(SCENARIO_SOLUTION_NUM,0) AS SCENARIO_SOLUTION_NUM     --  
     , NVL(LOSS_TYPE,0) AS LOSS_TYPE     --  
     , NVL(RU_CNT,0) AS RU_CNT           --  
     , MODIFY_YN                         --  
     , BATCH_YN                          --  
     , NVL(TM_STARTX,0) AS TM_STARTX     --  
     , NVL(TM_STARTY,0) AS TM_STARTY     --  
     , NVL(TM_ENDX,0) AS TM_ENDX         --  
     , NVL(TM_ENDY,0) AS TM_ENDY         --  
     , REAL_DATE                         --  
     , REAL_DRM_FILE                     --  
     , REAL_COMP                         --  
     , REAL_CATT                         --  
     , REAL_CATY                         --  
     , BLD_TYPE                          --  
     , NVL(RET_PERIOD,0) AS RET_PERIOD     --  
     , RET_END_DATETIME                  --  
     , BUILDINGANALYSIS3D_YN             --  
     , NVL(BUILDINGANALYSIS3D_RESOLUTION,0) AS BUILDINGANALYSIS3D_RESOLUTION     --  
     , NVL(AREA_ID,0) AS AREA_ID         --  
     , BUILDINGANALYSIS3D_RELATED_YN     --  
     , NVL(RELATED_ANALYSIS_COVLIMITRSRP,0) AS RELATED_ANALYSIS_COVLIMITRSRP     --  
FROM   SCENARIO
WHERE 1=1 
AND SCENARIO_ID LIKE '%'
;

-------------------------------
-- SCHEDULE
-------------------------------

SELECT SERVER_ID, COUNT(*)
FROM SCHEDULE
GROUP BY SERVER_ID
ORDER BY SERVER_ID
;

SELECT * FROM SCHEDULE
;

/
SELECT * FROM SCHEDULE
WHERE PROCESS_CD = '10002'

/

SELECT * FROM SCHEDULE
WHERE SERVER_ID = 'AS007'
/

SELECT ANALYSIS_WEIGHT, COUNT(*)
FROM SCHEDULE
GROUP BY ANALYSIS_WEIGHT
ORDER BY ANALYSIS_WEIGHT
;
/
SELECT *
FROM SCENARIO


/

SELECT COUNT(*) FROM SCENARIO

/

SELECT * FROM SCENARIO
;

/

SELECT NETWORK_TYPE, COUNT(*)
FROM SCENARIO
GROUP BY NETWORK_TYPE
;

/



SELECT ANALYSIS_WEIGHT, COUNT(*)
FROM SCHEDULE
GROUP BY ANALYSIS_WEIGHT


/

SELECT SCHEDULE_ID, COUNT(*)
FROM (
    SELECT SCHEDULE_ID, SCENARIO_ID
    FROM SCHEDULE
    GROUP BY SCHEDULE_ID, SCENARIO_ID
    )
GROUP BY SCHEDULE_ID
HAVING COUNT(*) > 1

/

SELECT * FROM SCHEDULE
WHERE SCENARIO_ID IN (
SELECT SCENARIO_ID, COUNT(*)
FROM (
    SELECT SCHEDULE_ID, SCENARIO_ID
    FROM SCHEDULE
    GROUP BY SCHEDULE_ID, SCENARIO_ID
    )
GROUP BY SCENARIO_ID
HAVING COUNT(*) > 1
)

/

SELECT *
FROM (
SELECT SCENARIO_NM, COUNT(*) CNT
FROM SCENARIO
GROUP BY  SCENARIO_NM
ORDER BY  SCENARIO_NM
) A
ORDER BY CNT DESC
;



/

SELECT RESULT_TIME
FROM SCHEDULE
WHERE RESULT_TIME > 9580.4
GROUP BY RESULT_TIME
ORDER BY RESULT_TIME DESC



/

SELECT * FROM SCENARIO
WHERE SCENARIO_ID IN (
    SELECT SCENARIO_ID
    FROM SCHEDULE
    WHERE RESULT_TIME > 9580.4
)
ORDER BY  SCENARIO_NM
;

SELECT * FROM SCENARIO
WHERE SCENARIO_ID IN (
    SELECT SCENARIO_ID
    FROM SCHEDULE
    WHERE RESULT_TIME > 9580.4
)
ORDER BY  SCENARIO_NM

/

SELECT * FROM SCENARIO
ORDER BY REG_DT


SELECT COUNT(*) FROM SCENARIO
;

/

SELECT *
FROM (
SELECT SCENARIO_NM, COUNT(*) CNT
FROM SCENARIO
GROUP BY  SCENARIO_NM
ORDER BY  SCENARIO_NM
) A
ORDER BY CNT DESC
;



/

SELECT RESULT_TIME
FROM SCHEDULE
WHERE RESULT_TIME > 9580.4
GROUP BY RESULT_TIME
ORDER BY RESULT_TIME DESC



/

SELECT * FROM SCENARIO
WHERE SCENARIO_ID IN (
    SELECT SCENARIO_ID
    FROM SCHEDULE
    WHERE RESULT_TIME > 9580.4
)
ORDER BY  SCENARIO_NM
;

SELECT * FROM SCENARIO
WHERE SCENARIO_ID IN (
    SELECT SCENARIO_ID
    FROM SCHEDULE
    WHERE RESULT_TIME > 9580.4
)
ORDER BY  SCENARIO_NM



SELECT * FROM SCENARIO
ORDER BY REG_DT
;


SELECT
       A.SCENARIO_ID                       --
     , A.SCENARIO_NM                       -- 시나리오 이름
     , A.SIDO                              --
     , A.SIGUGUN                           --
     , A.DONG                              --
     , B.PROCESS_CD
     , B.PROCESS_MSG
     , TRUNC((A.TM_ENDX-A.TM_STARTX) * (A.TM_ENDY-A.TM_STARTY)) AS 면적
     , A.RESOLUTION                        -- RESOLUTION
     , B.BIN_X_CNT
     , B.BIN_Y_CNT
     , B.RU_CNT
     , B.ANALYSIS_WEIGHT
     , B.RESULT_TIME
     , A.REG_DT                            -- 등록일
FROM   SCENARIO A
     , SCHEDULE B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.REG_DT > SYSDATE -30
ORDER BY B.SCHEDULE_ID,A.REG_DT DESC
;


/
SELECT
       SCENARIO_ID                       --
     , SCENARIO_NM                       -- 시나리오 이름
     , TRUNC((TM_ENDX-TM_STARTX) * (TM_ENDY-TM_STARTY)) AS 면적
     , RU_CNT
     , SIDO                              --
     , SIGUGUN                           --
     , DONG                              --
     , FA_MODEL_ID                       --
     , FA_SEQ                            -- 주파수 일련번호
     , RESOLUTION                        -- RESOLUTION
     , REG_DT                            -- 등록일
     , FLOORLOSS                         --
     , SCENARIO_SUB_ID                   -- 부모ID
     , SCENARIO_SOLUTION_NUM             -- 솔루션 분석 유형 4가지
     , LOSS_TYPE                         -- LOSS_TYPE
     , BUILDINGANALYSIS3D_YN             -- 3D분석여부
     , BUILDINGANALYSIS3D_RESOLUTION     -- 3D분석Resolution
     , AREA_ID
FROM SCENARIO
WHERE REG_DT > SYSDATE -30
ORDER BY REG_DT DESC


/

CREATE TABLE PSH001 (
TAB_NM VARCHAR(100)
,CNT NUMBER
)

/


SELECT * FROM PSH001
WHERE CNT > 0