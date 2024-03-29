-------------------------------------------------
-- SCHEDULE
-------------------------------------------------

SELECT A.SCHEDULE_ID
     , A.SCENARIO_ID
     , A.REG_DT
     , A.TYPE_CD
     , A.PROCESS_CD
     , A.PROCESS_MSG
     , D.TYPE_STEP_CD
     , D.PROCESS_LOG
     , B.SCENARIO_NM
     , B.SIDO
     , B.SIGUGUN
     , B.DONG
     , ROUND((B.TM_ENDX-B.TM_STARTX) * (B.TM_ENDY-B.TM_STARTY),0) AS AREA
     , A.BIN_X_CNT
     , A.BIN_Y_CNT
     , A.RU_CNT
     , ROUND(A.RESULT_TIME,0) AS RESULT_TIME
     , B.RESOLUTION
     , B.BUILDINGANALYSIS3D_RESOLUTION     -- 3D분석Resolution
     , B.BUILDINGANALYSIS3D_YN             -- 3D분석여부
     , A.SCENARIO_PATH
     , NVL(C.JOB_WEIGHT,0) AS JOB_WEIGHT     
     , NVL(C.JOB_THRESHOLD,0) AS JOB_THRESHOLD     
FROM   SCHEDULE A
     , SCENARIO B
     , SCHEDULE_EXT C
     , SCHEDULE_STEP D
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.SCHEDULE_ID = C.SCHEDULE_ID(+)
AND    A.SCHEDULE_ID = D.SCHEDULE_ID(+)
AND    A.TYPE_CD = D.TYPE_CD(+)
--AND    A.SCHEDULE_ID = 8463290
--AND    A.SCENARIO_PATH > ' '
--AND    A.SCENARIO_ID = 5104574
-- 서울시_NR_2D scenario_id=5113566 (schedule_id = 8463233)
--AND    A.SCENARIO_ID IN (5104573)
--AND    A.PROCESS_CD LIKE '2000%'
-- 8463235
--AND    A.TYPE_CD = 'SC001'
--AND    C.DONG = '대치2동'
--AND    A.TYPE_CD IN ('SC001','SC051')
AND    A.RU_CNT < 10
--AND    A.RESULT_TIME > 0
AND    A.REG_DT > SYSDATE -100   
--AND RESULT_TIME > 600000
ORDER BY A.REG_DT DESC
;

SELECT BUILDINGANALYSIS3D_RESOLUTION AS RESOLUTION
FROM   SCENARIO
;

SELECT SCENARIO_ID 
FROM   SCHEDULE A,
       SCENARIO B
WHERE SCHEDULE_ID = ${scheduleId}

8463235	5113766
SELECT CASE WHEN COUNT(*) = 1 THEN 'Y' ELSE 'N' END FROM SCHEDULE WHERE scenario_id=5113766 AND TYPE_CD = 'SC051'
;

--SELECT 157 * 155 FROM DUAL;

select * from NRUETRAFFIC
 where scenario_id = 5113566
;

UPDATE SCHEDULE
SET    PROCESS_CD = '20001', PROCESS_MSG=''
--SELECT * FROM SCHEDULE
WHERE  SCHEDULE_ID IN (8462895,8462896)
;

COMMIT
;

SELECT * FROM SCHEDULE_EXT
;

SELECT 307*301 FROM DUAL
;

SELECT 307*149 FROM DUAL
;

SELECT 370000 / 92407 FROM DUAL
;

SELECT * FROM i_result_nr_2d_los
;

SELECT 10304/ 4 FROM DUAL;

SELECT SCHEDULE_ID, COUNT(*)
FROM (
    SELECT SCHEDULE_ID, SCENARIO_ID
    FROM SCHEDULE
    GROUP BY SCHEDULE_ID, SCENARIO_ID
    )
GROUP BY SCHEDULE_ID
HAVING COUNT(*) > 1
;

SELECT RESULT_TIME
FROM SCHEDULE
WHERE RESULT_TIME > 9580.4
GROUP BY RESULT_TIME
ORDER BY RESULT_TIME DESC
;

SELECT * FROM SCENARIO
WHERE SCENARIO_ID IN (
    SELECT SCENARIO_ID
    FROM SCHEDULE
    WHERE RESULT_TIME > 9580.4
)
ORDER BY  SCENARIO_NM
;

SELECT * FROM SCHEDULE_WEIGHT
;

;


--scenario_id=5113766
--schedule_id = 8463234 (2D분석)
--schedule_id = 8463235 (3D분석)


-------------------------------------------------
-- SCENARIO
-------------------------------------------------
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



