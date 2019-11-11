-------------------------------------------------
-- SCHEDULE
-------------------------------------------------
SELECT A.SCHEDULE_ID
     , A.SCENARIO_ID
     , A.REG_DT
     , A.TYPE_CD
     , A.PROCESS_CD
     , A.PROCESS_MSG
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
FROM   I_SCHEDULE A
     , I_SCENARIO B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.PROCESS_CD = '20001'
--AND    TYPE_CD IN ('SC001','SC051')
ORDER BY REG_DT DESC
;

select * from SCHEDULE
;

select * FROM SCENARIO
;

select * FROM SHP
;

