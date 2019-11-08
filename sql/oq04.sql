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


TO_CHAR(D.CPU_TIME/(1000000 * DECODE(D.EXECUTIONS,NULL,1,0,1,D.EXECUTIONS)),999999.99 )     AS CPU����ð�
TO_CHAR(D.ELAPSED_TIME/(1000000 * DECODE(D.EXECUTIONS,NULL,1,0,1,D.EXECUTIONS)),999999.99 ) AS ��ս���ð�

SELECT * FROM SCHEDULE_STEP;



SELECT END_DT, START_DT, END_DT - START_DT
,TRUNC(ROUND((END_DT-START_DT)*24*60*60))
FROM SCHEDULE_STEP;

DELETE FROM SCHEDULE_STEP_CD;

CREATE TABLE SCHEDULE_STEP (
      SCHEDULE_ID        NUMBER          NOT NULL -- ������ID
    , TYPE_CD            VARCHAR2(10)    NOT NULL -- Ÿ���ڵ�
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- Ÿ�Խ����ڵ�
    , START_DT           DATE                NULL -- �����Ͻ�
    , END_DT             DATE                NULL -- �����Ͻ�
    , PROCESS_TIME       INT                 NULL -- �ҿ�ð�
    , PROCESS_LOG        VARCHAR2(1000)      NULL -- ���μ����α�
    , REG_DT	         DATE                NULL -- �������
    , REG_USER_ID        VARCHAR2(13)        NULL -- �����
    , MOD_DT             DATE                NULL -- ��������
    , MOD_USER_ID        VARCHAR2(13)        NULL -- ������
    , CONSTRAINT SCHEDULE_STEP_PK PRIMARY KEY (SCHEDULE_ID, TYPE_CD, TYPE_STEP_CD)
);


COMMENT ON TABLE SCHEDULE_STEP IS '�����ٽ���';
COMMENT ON COLUMN SCHEDULE_STEP.SCHEDULE_ID          IS '������ID';
COMMENT ON COLUMN SCHEDULE_STEP.TYPE_CD              IS 'Ÿ���ڵ�';
COMMENT ON COLUMN SCHEDULE_STEP.TYPE_STEP_CD         IS 'Ÿ�Խ����ڵ�';
COMMENT ON COLUMN SCHEDULE_STEP.START_DT             IS '�����Ͻ�';
COMMENT ON COLUMN SCHEDULE_STEP.END_DT               IS '�����Ͻ�';
COMMENT ON COLUMN SCHEDULE_STEP.PROCESS_TIME         IS '�ҿ�ð�';
COMMENT ON COLUMN SCHEDULE_STEP.PROCESS_LOG          IS '���μ����α�';
COMMENT ON COLUMN SCHEDULE_STEP.REG_DT	             IS '�������';
COMMENT ON COLUMN SCHEDULE_STEP.REG_USER_ID          IS '�����';
COMMENT ON COLUMN SCHEDULE_STEP.MOD_DT               IS '��������';
COMMENT ON COLUMN SCHEDULE_STEP.MOD_USER_ID          IS '������';
