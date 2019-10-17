    SELECT JDBC_NM
         , DB
         , DRIVER
         , URL
         , USR_ID
         , USR_PW
         , REG_DTM
         , REG_USR_ID
         , UPD_DTM
         , UPD_USR_ID
    FROM   TDACM00010
    WHERE  UPPER(JDBC_NM) = UPPER('EOSLOH')
    ORDER BY REG_DTM

/

DELETE FROM CELLPLAN.SCENARIO;
/

SELECT COUNT(*) FROM SCENARIO;
;
/

SELECT * FROM SCENARIO --
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


/
SELECT 
       SCENARIO_ID                       --  
     , SCENARIO_NM                       -- �ó����� �̸�
     , SIDO                              --  
     , SIGUGUN                           --  
     , DONG                              --  
     ,TRUNC((TM_ENDX-TM_STARTX) * (TM_ENDY-TM_STARTY)) AS ����
     , FA_MODEL_ID                       --  
     , FA_SEQ                            -- ���ļ� �Ϸù�ȣ
     , RESOLUTION                        -- RESOLUTION
     , REG_DT                            -- �����?
     , FLOORLOSS                         --  
     , SCENARIO_SUB_ID                   -- �θ�ID
     , SCENARIO_SOLUTION_NUM             -- �ַ��? �м� ���� 4����
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