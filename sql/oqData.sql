-------------------------------------------------
-- ������ �Ⱑ��ġ
-------------------------------------------------

DELETE FROM SCHEDULE_WEIGHT
;

--84	4763304	782265600

INSERT INTO SCHEDULE_WEIGHT
SELECT
       SYSDATE AS BASE_DT              -- �����Ͻ�
     , 5000000 AS JOB_H_THRESHOLD      -- �������Ӱ�ġ(5000000 �̻�                       )
     , 1000000 AS JOB_M_THRESHOLD      -- �̵����Ӱ�ġ(1000001-4999999������ ��)
     ,       1 AS JOB_L_THRESHOLD      -- �ο����Ӱ�ġ(      1- 999999 ������ ��)
     ,       2 AS JOB_H_MAX_CNT        -- ������ ���� �ִ� ����
     ,       3 AS JOB_M_MAX_CNT        -- �̵��� ���� �ִ� ����
     ,       5 AS JOB_L_MAX_CNT        -- �ο��� ���� �ִ� ����
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

INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC001','�ó������⺻�м�','(���ĺм�)�ó������⺻�м�','N','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC051','Building Floor Analysis(BFA)','Building Floor Analysis(BFA)','Y','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
 
SELECT * FROM SCHEDULE_TYPE_CD;

COMMIT;

-------------------------------------------------
-- �����ٽ����ڵ�
-------------------------------------------------
DELETE FROM SCHEDULE_STEP_CD;

INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','01','�ó������⺻���� Postgre �� Hdfs �̰�','Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','02','Postgre Shell ����'             ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','03','LOS���� Postgre���� Hdfs�� �̰�'    ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','04','Spark Eng�м�'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','05','Result���� ����'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');

INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','01','�ó������⺻���� Postgre �� Hdfs �̰�','Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','02','Postgre Shell ����'             ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','03','LOS���� Postgre���� Hdfs�� �̰�'    ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','04','Spark Eng�м�'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','05','Result���� ����'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
 
SELECT * FROM SCHEDULE_STEP_CD;

COMMIT;

