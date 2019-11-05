-------------------------------------------------
-- 스케줄 잡가중치
-------------------------------------------------

DELETE FROM SCHEDULE_WEIGHT
;

--84	4763304	782265600

INSERT INTO SCHEDULE_WEIGHT
SELECT
       SYSDATE AS BASE_DT              -- 기준일시
     , 5000000 AS JOB_H_THRESHOLD      -- 하이잡임계치(5000000 이상                       )
     , 1000000 AS JOB_M_THRESHOLD      -- 미들잡임계치(1000001-4999999사이의 값)
     ,       1 AS JOB_L_THRESHOLD      -- 로우잡임계치(      1- 999999 사이의 값)
     ,       2 AS JOB_H_MAX_CNT        -- 하이잡 실행 최대 갯수
     ,       3 AS JOB_M_MAX_CNT        -- 미들잡 실행 최대 갯수
     ,       5 AS JOB_L_MAX_CNT        -- 로우잡 실행 최대 갯수
     , SYSDATE AS REG_DT               -- 등록일시
     , 'ADMIN' AS REG_USER_ID          -- 등록자
     , SYSDATE AS MOD_DT               -- 수정일시
     , 'ADMIN' AS MOD_USER_ID          -- 수정자
FROM   DUAL
;

SELECT * FROM SCHEDULE_WEIGHT
;

-------------------------------------------------
-- 스케줄
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
-- 스케줄타입코드
-------------------------------------------------
DELETE FROM SCHEDULE_TYPE_CD;

INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC001','시나리오기본분석','(전파분석)시나리오기본분석','N','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_TYPE_CD VALUES ('SC051','Building Floor Analysis(BFA)','Building Floor Analysis(BFA)','Y','Y', SYSDATE,'ADMIN',SYSDATE,'ADMIN');
 
SELECT * FROM SCHEDULE_TYPE_CD;

COMMIT;

-------------------------------------------------
-- 스케줄스탭코드
-------------------------------------------------
DELETE FROM SCHEDULE_STEP_CD;

INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','01','시나리오기본정보 Postgre 및 Hdfs 이관','Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','02','Postgre Shell 수행'             ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','03','LOS정보 Postgre에서 Hdfs로 이관'    ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','04','Spark Eng분석'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC001','05','Result파일 생성'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');

INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','01','시나리오기본정보 Postgre 및 Hdfs 이관','Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','02','Postgre Shell 수행'             ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','03','LOS정보 Postgre에서 Hdfs로 이관'    ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','04','Spark Eng분석'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
INSERT INTO SCHEDULE_STEP_CD VALUES ('SC051','05','Result파일 생성'                  ,'Y',SYSDATE,'ADMIN',SYSDATE,'ADMIN');
 
SELECT * FROM SCHEDULE_STEP_CD;

COMMIT;

