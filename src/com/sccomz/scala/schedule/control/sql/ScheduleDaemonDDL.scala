package com.sccomz.scala.schedule.control.sql

object ScheduleDaemonDDL {


def createSCHEDULE_WEIGHT(scheduleId:String) = {
"""
-- 스케줄 잡가중치
CREATE TABLE SCHEDULE_WEIGHT (
      BASE_DT            DATE            NOT NULL -- 기준일시
    , RU_CNT_W5          INT                 NULL -- RU갯수가중치5값
    , RU_CNT_W4          INT                 NULL -- RU갯수가중치4값
    , RU_CNT_W3          INT                 NULL -- RU갯수가중치3값
    , RU_CNT_W2          INT                 NULL -- RU갯수가중치2값
    , RU_CNT_W1          INT                 NULL -- RU갯수가중치1값
    , AREA_W5            INT                 NULL -- 면적가중치5값
    , AREA_W4            INT                 NULL -- 면적가중치4값
    , AREA_W3            INT                 NULL -- 면적가중치3값
    , AREA_W2            INT                 NULL -- 면적가중치2값
    , AREA_W1            INT                 NULL -- 면적가중치1값
    , JOB_RU_CNT_WEIGHT  NUMBER(5,2)         NULL -- 잡RU갯수가중치(0..1 RU갯수가중치와면접가중치의 합은 1 이어야 한다)
    , JOB_AREA_WEIGHT    NUMBER(5,2)         NULL -- 잡면적가중치(0..1 RU갯수가중치와면접가중치의 합은 1 이어야 한다)
    , JOB_H_THRESHOLD    INT                 NULL -- 하이잡임계치(1-5 사이의 값)
    , JOB_M_THRESHOLD    INT                 NULL -- 미들잡임계치(1-5 사이의 값)
    , JOB_L_THRESHOLD    INT                 NULL -- 로우잡임계치(1-5 사이의 값)
    , JOB_H_MAX_CNT      INT                 NULL -- 하이잡 실행 최대 갯수
    , JOB_M_MAX_CNT      INT                 NULL -- 미들잡 실행 최대 갯수
    , JOB_L_MAX_CNT      INT                 NULL -- 로우잡 실행 최대 갯수
    , REG_DT	           DATE                NULL -- 등록일시
    , REG_USER_ID        VARCHAR2(13)        NULL -- 등록자
    , MOD_DT             DATE                NULL -- 수정일시
    , MOD_USER_ID        VARCHAR2(13)        NULL -- 수정자
    , CONSTRAINT SCHEDULE_WEIGHT_PK PRIMARY KEY (BASE_DT)
);

COMMENT ON TABLE  SCHEDULE_WEIGHT IS '스케줄 잡가중치';
COMMENT ON COLUMN SCHEDULE_WEIGHT.BASE_DT           IS '기준일시';
COMMENT ON COLUMN SCHEDULE_WEIGHT.RU_CNT_W5         IS 'RU갯수가중치5값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.RU_CNT_W4         IS 'RU갯수가중치4값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.RU_CNT_W3         IS 'RU갯수가중치3값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.RU_CNT_W2         IS 'RU갯수가중치2값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.RU_CNT_W1         IS 'RU갯수가중치1값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.AREA_W5           IS '면적가중치5값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.AREA_W4           IS '면적가중치4값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.AREA_W3           IS '면적가중치3값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.AREA_W2           IS '면적가중치2값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.AREA_W1           IS '면적가중치1값';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_RU_CNT_WEIGHT IS '잡RU갯수가중치(0..1 RU갯수가중치+면접가중치=1)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_AREA_WEIGHT   IS '잡면적가중치(0..1 RU갯수가중치+면접가중치=1)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_H_THRESHOLD   IS '하이잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_M_THRESHOLD   IS '미들잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_L_THRESHOLD   IS '로우잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_H_MAX_CNT     IS '하이잡 실행 최대 갯수';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_M_MAX_CNT     IS '미들잡 실행 최대 갯수';
COMMENT ON COLUMN SCHEDULE_WEIGHT.JOB_L_MAX_CNT     IS '로우잡 실행 최대 갯수';
COMMENT ON COLUMN SCHEDULE_WEIGHT.REG_DT	          IS '등록일시 ';
COMMENT ON COLUMN SCHEDULE_WEIGHT.REG_USER_ID       IS '등록자';
COMMENT ON COLUMN SCHEDULE_WEIGHT.MOD_DT            IS '수정일시';
COMMENT ON COLUMN SCHEDULE_WEIGHT.MOD_USER_ID       IS '수정자';

"""
}

def createSCHEDULE_EXT(scheduleId:String) = {
"""
-- 스케줄확장
CREATE TABLE SCHEDULE_EXT (
      SCHEDULE_ID        NUMBER          NOT NULL -- 스케줄ID
    , JOB_WEIGHT         INT                 NULL -- 잡가중치(3:상, 2:중, 1:하)
    , JOB_H_THRESHOLD    INT                 NULL -- 하이잡임계치(1-5 사이의 값)
    , JOB_M_THRESHOLD    INT                 NULL -- 미들잡임계치(1-5 사이의 값)
    , JOB_L_THRESHOLD    INT                 NULL -- 로우잡임계치(1-5 사이의 값)
    , RU_CNT_WEIGHT      INT                 NULL -- RU갯수가중치값(1..5)
    , JOB_RU_CNT_WEIGHT  INT                 NULL -- RU갯수가중치
    , AREA               INT                 NULL -- 면적
    , AREA_WEIGHT        INT                 NULL -- 면적가중치값(1..5)
    , JOB_AREA_WEIGHT    INT                 NULL -- 면적가중치
    , REG_DT	           DATE                NULL -- 등록일자
    , REG_USER_ID        VARCHAR2(13)        NULL -- 등록자
    , MOD_DT             DATE                NULL -- 수정일자
    , MOD_USER_ID        VARCHAR2(13)        NULL -- 수정자
    , CONSTRAINT SCHEDULE_EXT_PK PRIMARY KEY (SCHEDULE_ID)
);

COMMENT ON TABLE SCHEDULE_EXT IS '스케줄확장';
COMMENT ON COLUMN SCHEDULE_EXT.SCHEDULE_ID         IS '스케줄ID';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_WEIGHT          IS '잡가중치(3:상, 2:중, 1:하)';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_H_THRESHOLD     IS '하이잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_M_THRESHOLD     IS '미들잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_L_THRESHOLD     IS '로우잡임계치(1-5 사이의 값)';
COMMENT ON COLUMN SCHEDULE_EXT.RU_CNT_WEIGHT       IS 'RU갯수가중치값(1..5)';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_RU_CNT_WEIGHT   IS 'RU갯수가중치';
COMMENT ON COLUMN SCHEDULE_EXT.AREA                IS '면적';
COMMENT ON COLUMN SCHEDULE_EXT.AREA_WEIGHT         IS '면적가중치값(1..5)';
COMMENT ON COLUMN SCHEDULE_EXT.JOB_AREA_WEIGHT     IS '면적가중치';
COMMENT ON COLUMN SCHEDULE_EXT.REG_DT              IS '등록일자';
COMMENT ON COLUMN SCHEDULE_EXT.REG_USER_ID         IS '등록자';
COMMENT ON COLUMN SCHEDULE_EXT.MOD_DT              IS '수정일자';
COMMENT ON COLUMN SCHEDULE_EXT.MOD_USER_ID         IS '수정자';

"""
}

def createSCHEDULE_STEP(scheduleId:String) = {
"""
-- 스케줄스탭
CREATE TABLE SCHEDULE_STEP (
      SCHEDULE_ID        NUMBER          NOT NULL -- 스케줄ID
    , TYPE_CD            VARCHAR2(10)    NOT NULL -- 타입코드
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- 타입스탭코드
    , START_DT           DATE                NULL -- 시작일시
    , END_DT             DATE                NULL -- 종료일시
    , PROCESS_TIME       INT                 NULL -- 소요시간
    , PROCESS_LOG        VARCHAR2(1000)      NULL -- 프로세스로그
    , REG_DT	           DATE                NULL -- 등록일자
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


"""
}

def createSCHEDULE_STEP_CD(scheduleId:String) = {
"""
-- 스케줄스탭코드
CREATE TABLE SCHEDULE_STEP_CD (
      TYPE_CD            VARCHAR2(10)    NOT NULL -- 타입코드
    , TYPE_STEP_CD       VARCHAR2(2)     NOT NULL -- 타입스탭코드
    , TYPE_STEP_CD_NM    VARCHAR2(1000)      NULL -- 타입스탭코드명
    , USE_YN             VARCHAR2(1)         NULL -- 사용유무
    , REG_DT	           DATE                NULL -- 등록일자
    , REG_USER_ID        VARCHAR2(13)        NULL -- 등록자
    , MOD_DT             DATE                NULL -- 수정일자
    , MOD_USER_ID        VARCHAR2(13)        NULL -- 수정자
    , CONSTRAINT SCHEDULE_STEP_CD_PK PRIMARY KEY (TYPE_CD, TYPE_STEP_CD)
);


COMMENT ON TABLE SCHEDULE_STEP_CD IS '스케줄스탭코드';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_CD               IS '타입코드';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_STEP_CD          IS '타입스탭코드';
COMMENT ON COLUMN SCHEDULE_STEP_CD.TYPE_STEP_CD_NM       IS '타입스탭코드명';
COMMENT ON COLUMN SCHEDULE_STEP_CD.USE_YN                IS '사용유무';
COMMENT ON COLUMN SCHEDULE_STEP_CD.REG_DT	               IS '등록일자';
COMMENT ON COLUMN SCHEDULE_STEP_CD.REG_USER_ID           IS '등록자';
COMMENT ON COLUMN SCHEDULE_STEP_CD.MOD_DT                IS '수정일자';
COMMENT ON COLUMN SCHEDULE_STEP_CD.MOD_USER_ID           IS '수정자';

"""
}

}