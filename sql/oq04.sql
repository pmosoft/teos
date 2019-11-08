SELECT 
       NVL(FA_SEQ,0)                           ||'|'||
       NVL(SYSTEMTYPE,0)                       ||'|'||
       NVL(CHNO,0)                             ||'|'||
       NVL(UPLINKFREQ,0)                       ||'|'||
       NVL(DOWNLINKFREQ,0)                     ||'|'||
       NVL(ANALY_CHECK,0)                      ||'|'||
       PASSLOSS_MODEL                          ||'|'||
       FREQ                                    ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       REG_ID                                  ||'|'
FROM   FABASE
;


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


TO_CHAR(D.CPU_TIME/(1000000 * DECODE(D.EXECUTIONS,NULL,1,0,1,D.EXECUTIONS)),999999.99 )     AS CPU실행시간
TO_CHAR(D.ELAPSED_TIME/(1000000 * DECODE(D.EXECUTIONS,NULL,1,0,1,D.EXECUTIONS)),999999.99 ) AS 평균실행시간

SELECT * FROM SCHEDULE_STEP;



SELECT END_DT, START_DT, END_DT - START_DT
,TRUNC(ROUND((END_DT-START_DT)*24*60*60))
FROM SCHEDULE_STEP;

DELETE FROM SCHEDULE_STEP_CD;

CREATE TABLE SCHEDULE_STEP (
      SCHEDULE_ID        NUMBER          NOT NULL -- 스케줄ID
    , TYPE_CD            VARCHAR2(10)    NOT NULL -- 타입코드
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- 타입스탭코드
    , START_DT           DATE                NULL -- 시작일시
    , END_DT             DATE                NULL -- 종료일시
    , PROCESS_TIME       INT                 NULL -- 소요시간
    , PROCESS_LOG        VARCHAR2(1000)      NULL -- 프로세스로그
    , REG_DT	         DATE                NULL -- 등록일자
    , REG_USER_ID        VARCHAR2(13)        NULL -- 등록자
    , MOD_DT             DATE                NULL -- 수정일자
    , MOD_USER_ID        VARCHAR2(13)        NULL -- 수정자
    , CONSTRAINT SCHEDULE_STEP_PK PRIMARY KEY (SCHEDULE_ID, TYPE_CD, TYPE_STEP_CD)
);


COMMENT ON TABLE SCHEDULE_STEP IS '스케줄스탭';
COMMENT ON COLUMN SCHEDULE_STEP.SCHEDULE_ID          IS '스케줄ID';
COMMENT ON COLUMN SCHEDULE_STEP.TYPE_CD              IS '타입코드';
COMMENT ON COLUMN SCHEDULE_STEP.TYPE_STEP_CD         IS '타입스탭코드';
COMMENT ON COLUMN SCHEDULE_STEP.START_DT             IS '시작일시';
COMMENT ON COLUMN SCHEDULE_STEP.END_DT               IS '종료일시';
COMMENT ON COLUMN SCHEDULE_STEP.PROCESS_TIME         IS '소요시간';
COMMENT ON COLUMN SCHEDULE_STEP.PROCESS_LOG          IS '프로세스로그';
COMMENT ON COLUMN SCHEDULE_STEP.REG_DT	             IS '등록일자';
COMMENT ON COLUMN SCHEDULE_STEP.REG_USER_ID          IS '등록자';
COMMENT ON COLUMN SCHEDULE_STEP.MOD_DT               IS '수정일자';
COMMENT ON COLUMN SCHEDULE_STEP.MOD_USER_ID          IS '수정자';
