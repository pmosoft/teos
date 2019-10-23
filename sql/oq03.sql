    SELECT SCHEDULE_ID                            AS SCHEDULE_ID
         , TYPE_CD                                AS TYPE_CD
         , SCENARIO_ID                            AS SCENARIO_ID
         , USER_ID                                AS USER_ID
         , PRIORITIZE                             AS PRIORITIZE
         , PROCESS_CD                             AS PROCESS_CD
         , PROCESS_MSG                            AS PROCESS_MSG
         , SCENARIO_PATH                          AS SCENARIO_PATH
         , TO_CHAR(REG_DT, 'YYYYMMDDHH24MISS')    AS REG_DT
         , TO_CHAR(MODIFY_DT, 'YYYYMMDDHH24MISS') AS MODIFY_DT
    FROM  (SELECT ROW_NUMBER() OVER(ORDER BY SCHEDULE_ID ASC, TYPE_CD ASC, PRIORITIZE ASC, RU_CNT ASC) AS ROW_NUM
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
           WHERE  PROCESS_CD IN ('10001')
           --AND    TYPE_CD IN ('SC001','SC051')
          )
    WHERE ROW_NUM <= 2
    ORDER BY ROW_NUM


---------------------------------

SELECT SERVER_ID, COUNT(*)
FROM SCHEDULE
GROUP BY SERVER_ID
ORDER BY SERVER_ID
;

---------------------------------


SELECT * FROM SCHEDULE
;


SELECT * FROM SCHEDULE
WHERE PROCESS_CD = '10002'
;

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
     , A.SCENARIO_NM                       -- �ó����� �̸�
     , A.SIDO                              --
     , A.SIGUGUN                           --
     , A.DONG                              --
     , B.PROCESS_CD
     , B.PROCESS_MSG
     , TRUNC((A.TM_ENDX-A.TM_STARTX) * (A.TM_ENDY-A.TM_STARTY)) AS ����
     , A.RESOLUTION                        -- RESOLUTION
     , B.BIN_X_CNT
     , B.BIN_Y_CNT
     , B.RU_CNT
     , B.ANALYSIS_WEIGHT
     , B.RESULT_TIME
     , A.REG_DT                            -- �����
FROM   SCENARIO A
     , SCHEDULE B
WHERE  A.SCENARIO_ID = B.SCENARIO_ID
AND    A.REG_DT > SYSDATE -30
ORDER BY B.SCHEDULE_ID,A.REG_DT DESC
;


/
SELECT
       SCENARIO_ID                       --
     , SCENARIO_NM                       -- �ó����� �̸�
     , TRUNC((TM_ENDX-TM_STARTX) * (TM_ENDY-TM_STARTY)) AS ����
     , RU_CNT
     , SIDO                              --
     , SIGUGUN                           --
     , DONG                              --
     , FA_MODEL_ID                       --
     , FA_SEQ                            -- ���ļ� �Ϸù�ȣ
     , RESOLUTION                        -- RESOLUTION
     , REG_DT                            -- �����
     , FLOORLOSS                         --
     , SCENARIO_SUB_ID                   -- �θ�ID
     , SCENARIO_SOLUTION_NUM             -- �ַ�� �м� ���� 4����
     , LOSS_TYPE                         -- LOSS_TYPE
     , BUILDINGANALYSIS3D_YN             -- 3D�м�����
     , BUILDINGANALYSIS3D_RESOLUTION     -- 3D�м�Resolution
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