-------------------------------------------------
-- ������ �Ⱑ��ġ
-------------------------------------------------

DELETE FROM SCHEDULE_WEIGHT
;

INSERT INTO SCHEDULE_WEIGHT
SELECT
       SYSDATE AS BASE_DT              -- �����Ͻ�
     ,  101000 AS WEIGHT5              -- �Ӱ�ġ5
     ,  101000 AS WEIGHT4              -- �Ӱ�ġ4
     ,  101000 AS WEIGHT3              -- �Ӱ�ġ3
     ,  101000 AS WEIGHT2              -- �Ӱ�ġ2
     ,  101000 AS WEIGHT1              -- �Ӱ�ġ1
     ,      4  AS JOB_H_THRESHOLD      -- �������Ӱ�ġ(1-5 ������ ��)
     ,      3  AS JOB_M_THRESHOLD      -- �̵����Ӱ�ġ(1-5 ������ ��)
     ,      2  AS JOB_L_THRESHOLD      -- �ο����Ӱ�ġ(1-5 ������ ��)
     ,      2  AS JOB_H_MAX_CNT        -- ������ ���� �ִ� ����
     ,      3  AS JOB_M_MAX_CNT        -- �̵��� ���� �ִ� ����
     ,      5  AS JOB_L_MAX_CNT        -- �ο��� ���� �ִ� ����
     , SYSDATE AS REG_DT               -- ����Ͻ�
     , 'ADMIN' AS REG_USER_ID          -- �����
     , SYSDATE AS MOD_DT               -- �����Ͻ�
     , 'ADMIN' AS MOD_USER_ID          -- ������
FROM   DUAL
;

SELECT * FROM SCHEDULE_WEIGHT
;

-------------------------------------------------
-- ������
-------------------------------------------------
--SELECT COUNT(*) FROM  SCHEDULE
UPDATE SCHEDULE
SET    PROCESS_CD = '20001', PROCESS_MSG = ''
WHERE  SCHEDULE_ID = '8461275'  
;

--SELECT COUNT(*) FROM  SCHEDULE
UPDATE SCHEDULE
SET    PROCESS_CD = '10004', PROCESS_MSG = ''
WHERE  PROCESS_CD = '20000'
AND    TYPE_CD = 'SC001'
AND    REG_DT BETWEEN '2019-09-01' AND '2019-09-30'  
;

SELECT * 
FROM   SCHEDULE
WHERE  PROCESS_CD = '10000'
AND    TYPE_CD = 'SC001'
AND    REG_DT >= SYSDATE-1  
;

SELECT *  
FROM   SCHEDULE
WHERE  PROCESS_CD = '20000'  
; 


-------------------------------------------------
-- ������Ÿ���ڵ�
-------------------------------------------------
DELETE FROM SCHEDULE_TYPE_CD;

INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC001','�ó������⺻�м�','(���ĺм�)�ó������⺻�м�','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC051','Building Floor Analysis(BFA)','Building Floor Analysis(BFA)','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
 
SELECT * FROM SCHEDULE_TYPE_CD;

