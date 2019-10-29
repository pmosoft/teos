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