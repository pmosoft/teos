DROP TABLE PSH_TEST02;

CREATE TABLE PSH_TEST02 AS
SELECT DISTINCT
       RESOLUTION     
     , BIN_X_CNT
     , BIN_Y_CNT
     , BIN_CNT
     , AREA
     , RU_CNT
     , JOB_THRESHOLD
     , RESULT_TIME
FROM PSH_TEST01
WHERE RESULT_TIME > 0
AND RU_CNT > 0
ORDER BY RESULT_TIME, JOB_THRESHOLD 
;


SELECT '1000000 미만' AS JOB_THRESHOLD
     , MIN(JOB_THRESHOLD),ROUND(AVG(JOB_THRESHOLD)),MAX(JOB_THRESHOLD)
     , MIN(BIN_CNT),ROUND(AVG(BIN_CNT)),MAX(BIN_CNT)
     , MIN(RU_CNT),ROUND(AVG(RU_CNT)),MAX(RU_CNT)
     , MIN(RESULT_TIME),ROUND(AVG(RESULT_TIME)),MAX(RESULT_TIME)
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 1000000
UNION ALL
SELECT '2000000 미만' AS JOB_THRESHOLD
     , MIN(JOB_THRESHOLD),ROUND(AVG(JOB_THRESHOLD)),MAX(JOB_THRESHOLD)
     , MIN(BIN_CNT),ROUND(AVG(BIN_CNT)),MAX(BIN_CNT)
     , MIN(RU_CNT),ROUND(AVG(RU_CNT)),MAX(RU_CNT)
     , MIN(RESULT_TIME),ROUND(AVG(RESULT_TIME)),MAX(RESULT_TIME)
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 2000000
UNION ALL
SELECT '3000000 미만' AS JOB_THRESHOLD
     , MIN(JOB_THRESHOLD),ROUND(AVG(JOB_THRESHOLD)),MAX(JOB_THRESHOLD)
     , MIN(BIN_CNT),ROUND(AVG(BIN_CNT)),MAX(BIN_CNT)
     , MIN(RU_CNT),ROUND(AVG(RU_CNT)),MAX(RU_CNT)
     , MIN(RESULT_TIME),ROUND(AVG(RESULT_TIME)),MAX(RESULT_TIME)
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 3000000
;

SELECT '100000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 100000
UNION ALL
SELECT '500000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 500000
UNION ALL
SELECT '1000000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 1000000
UNION ALL
SELECT '2000000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 2000000
UNION ALL
SELECT '3000000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 3000000
UNION ALL
SELECT '4000000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 4000000
UNION ALL
SELECT '5000000 미만' AS JOB_THRESHOLD
     , ROUND(AVG(JOB_THRESHOLD)) AS AVG_JOB_THRESHOLD
     , ROUND(AVG(BIN_CNT)) AS AVG_BIN_CNT
     , ROUND(AVG(RU_CNT)) AS AVG_RU_CNT
FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 5000000
;

SELECT * FROM PSH_TEST02 ORDER BY JOB_THRESHOLD
;
SELECT 617000/3600/24 FROM DUAL;

SELECT * FROM PSH_TEST02
WHERE 1=1
AND   JOB_THRESHOLD < 1000000
ORDER BY JOB_THRESHOLD
;


SELECT * FROM PSH_TEST02

