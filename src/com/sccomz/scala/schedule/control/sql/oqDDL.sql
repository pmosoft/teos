-------------------------------------------------
-- ������ �Ⱑ��ġ
-------------------------------------------------

DROP TABLE SCHEDULE_WEIGHT;

CREATE TABLE SCHEDULE_WEIGHT (
      BASE_DT             DATE            NOT NULL -- �����Ͻ�
    , WEIGHT5             INT                 NULL -- �Ӱ�ġ5(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))
    , WEIGHT4             INT                 NULL -- �Ӱ�ġ4(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))
    , WEIGHT3             INT                 NULL -- �Ӱ�ġ3(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))
    , WEIGHT2             INT                 NULL -- �Ӱ�ġ2(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))
    , WEIGHT1             INT                 NULL -- �Ӱ�ġ1(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))
    , JOB_H_THRESHOLD     INT                 NULL -- �������Ӱ�ġ(1-5 ������ ��)
    , JOB_M_THRESHOLD     INT                 NULL -- �̵����Ӱ�ġ(1-5 ������ ��)
    , JOB_L_THRESHOLD     INT                 NULL -- �ο����Ӱ�ġ(1-5 ������ ��)
    , JOB_H_MAX_CNT       INT                 NULL -- ������ ���� �ִ� ����
    , JOB_M_MAX_CNT       INT                 NULL -- �̵��� ���� �ִ� ����
    , JOB_L_MAX_CNT       INT                 NULL -- �ο��� ���� �ִ� ����
    , REG_DT	          DATE                NULL -- ����Ͻ�
    , REG_USER_ID         VARCHAR2(13)        NULL -- �����
    , MOD_DT              DATE                NULL -- �����Ͻ�
    , MOD_USER_ID         VARCHAR2(13)        NULL -- ������
    , CONSTRAINT SCHEDULE_WEIGHT_PK PRIMARY KEY (BASE_DT)
);

COMMENT ON TABLE  SCHEDULE_WEIGHT IS '������ �Ⱑ��ġ';
COMMENT ON COLUMN SCHEDULE_WEIGHT.BASE_DT           IS '�����Ͻ�';
COMMENT ON COLUMN SCHEDULE_WEIGHT.WEIGHT5           IS '�Ӱ�ġ5(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))';
COMMENT ON COLUMN SCHEDULE_WEIGHT.WEIGHT4           IS '�Ӱ�ġ4(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))';
COMMENT ON COLUMN SCHEDULE_WEIGHT.WEIGHT3           IS '�Ӱ�ġ3(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))';
COMMENT ON COLUMN SCHEDULE_WEIGHT.WEIGHT2           IS '�Ӱ�ġ2(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))';
COMMENT ON COLUMN SCHEDULE_WEIGHT.WEIGHT1           IS '�Ӱ�ġ1(RU����*BIN����,RU���� * (BIN���� + ����(����*����)/3D �м� Resolution ^2))';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_H_THRESHOLD   IS '�������Ӱ�ġ(1-5 ������ ��)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_M_THRESHOLD   IS '�̵����Ӱ�ġ(1-5 ������ ��)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_L_THRESHOLD   IS '�ο����Ӱ�ġ(1-5 ������ ��)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_H_MAX_CNT     IS '������ ���� �ִ� ����';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_M_MAX_CNT     IS '�̵��� ���� �ִ� ����';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_L_MAX_CNT     IS '�ο��� ���� �ִ� ����';
COMMENT ON COLUMN SCHEDULE_WEIGHT.REG_DT	        IS '����Ͻ� ';
COMMENT ON COLUMN SCHEDULE_WEIGHT.REG_USER_ID       IS '�����';
COMMENT ON COLUMN SCHEDULE_WEIGHT.MOD_DT            IS '�����Ͻ�';
COMMENT ON COLUMN SCHEDULE_WEIGHT.MOD_USER_ID       IS '������';

-------------------------------------------------
-- ������Ȯ��
-------------------------------------------------
DROP TABLE SCHEDULE_EXT;

CREATE TABLE SCHEDULE_EXT (
      SCHEDULE_ID        NUMBER          NOT NULL -- ������ID
    , JOB_WEIGHT         INT                 NULL -- �Ⱑ��ġ(3:��, 2:��, 1:��)
    , WEIGHT             INT                 NULL -- �Ӱ�ġ
    , REG_DT	         DATE                NULL -- �������
    , REG_USER_ID        VARCHAR2(13)        NULL -- �����
    , MOD_DT             DATE                NULL -- ��������
    , MOD_USER_ID        VARCHAR2(13)        NULL -- ������
    , CONSTRAINT SCHEDULE_EXT_PK PRIMARY KEY (SCHEDULE_ID)
);

COMMENT ON TABLE SCHEDULE_EXT IS '������Ȯ��';
COMMENT ON COLUMN SCHEDULE_EXT.SCHEDULE_ID    IS '������ID';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_WEIGHT     IS '�Ⱑ��ġ(3:��, 2:��, 1:��)';
COMMENT ON COLUMN SCHEDULE_EXT.WEIGHT         IS '�Ӱ�ġ'; 
COMMENT ON COLUMN SCHEDULE_EXT.REG_DT	      IS '����Ͻ� ';        
COMMENT ON COLUMN SCHEDULE_EXT.REG_USER_ID    IS '�����';          
COMMENT ON COLUMN SCHEDULE_EXT.MOD_DT         IS '�����Ͻ�';         
COMMENT ON COLUMN SCHEDULE_EXT.MOD_USER_ID    IS '������';          

-------------------------------------------------
-- �����ٽ���
-------------------------------------------------
DROP TABLE SCHEDULE_STEP;

CREATE TABLE SCHEDULE_STEP (
      SCHEDULE_ID        NUMBER          NOT NULL -- ������ID
    , TYPE_CD            VARCHAR2(10)    NOT NULL -- Ÿ���ڵ�
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- Ÿ�Խ����ڵ�
    , START_DT           DATE                NULL -- �����Ͻ�
    , END_DT             DATE                NULL -- �����Ͻ�
    , PROCESS_TIME       INT                 NULL -- �ҿ�ð�
    , PROCESS_LOG        VARCHAR2(1000)      NULL -- ���μ����α�
    , REG_DT	           DATE                NULL -- �������
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

-------------------------------------------------
-- �����ٽ����ڵ�
-------------------------------------------------
DROP TABLE SCHEDULE_STEP_CD;

CREATE TABLE SCHEDULE_STEP_CD (
      TYPE_CD            VARCHAR2(10)    NOT NULL -- Ÿ���ڵ�
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- Ÿ�Խ����ڵ�
    , TYPE_STEP_CD_NM    VARCHAR2(1000)      NULL -- Ÿ�Խ����ڵ��
    , USE_YN             VARCHAR2(1)         NULL -- �������
    , REG_DT	         DATE                NULL -- �������
    , REG_USER_ID        VARCHAR2(13)        NULL -- �����
    , MOD_DT             DATE                NULL -- ��������
    , MOD_USER_ID        VARCHAR2(13)        NULL -- ������
    , CONSTRAINT SCHEDULE_STEP_CD_PK PRIMARY KEY (TYPE_CD, TYPE_STEP_CD)
);


COMMENT ON TABLE SCHEDULE_STEP_CD IS '�����ٽ����ڵ�';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_CD               IS 'Ÿ���ڵ�';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_STEP_CD          IS 'Ÿ�Խ����ڵ�';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_STEP_CD_NM       IS 'Ÿ�Խ����ڵ��';
COMMENT ON COLUMN SCHEDULE_STEP_CD.USE_YN                IS '�������';
COMMENT ON COLUMN SCHEDULE_STEP_CD.REG_DT	             IS '�������';
COMMENT ON COLUMN SCHEDULE_STEP_CD.REG_USER_ID           IS '�����';
COMMENT ON COLUMN SCHEDULE_STEP_CD.MOD_DT                IS '��������';
COMMENT ON COLUMN SCHEDULE_STEP_CD.MOD_USER_ID           IS '������';


-------------------------------------------------
-- ������Ÿ���ڵ�
-------------------------------------------------
DROP TABLE SCHEDULE_TYPE_CD;

CREATE TABLE SCHEDULE_TYPE_CD (
      TYPE_CD            VARCHAR2(10)    NOT NULL -- Ÿ���ڵ�
    , TYPE_NM            VARCHAR2(1000)      NULL -- Ÿ���ڵ��
    , TYPE_DESC          VARCHAR2(1000)      NULL -- Ÿ���ڵ弳��
    , USE_YN             VARCHAR2(1)         NULL -- �������
    , REG_DT	         DATE                NULL -- �������
    , REG_USER_ID        VARCHAR2(13)        NULL -- �����
    , MOD_DT             DATE                NULL -- ��������
    , MOD_USER_ID        VARCHAR2(13)        NULL -- ������
    , CONSTRAINT SCHEDULE_STEP_CD_PK PRIMARY KEY (TYPE_CD)
);
 

COMMENT ON TABLE SCHEDULE_TYPE_CD IS '������Ÿ���ڵ�';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.TYPE_CD               IS 'Ÿ���ڵ�';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.TYPE_NM               IS 'Ÿ���ڵ��';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.TYPE_DESC             IS 'Ÿ���ڵ弳��';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.USE_YN                IS '�������';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.REG_DT	             IS '�������';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.REG_USER_ID           IS '�����';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.MOD_DT                IS '��������';
COMMENT ON COLUMN SCHEDULE_TYPE_CD.MOD_USER_ID           IS '������';

