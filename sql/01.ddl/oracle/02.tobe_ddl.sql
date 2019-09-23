-- 스케줄 분석종류 
CREATE TABLE CELLPLAN.SCHEDULE_TY
(
  TYPE_CD     VARCHAR2(10) NOT NULL -- 분석종류코드  
, TYPE_CD_NM  VARCHAR2(1)           -- 분석종류코드명
, TYPE_CD_YN  VARCHAR2(1)           -- 분석종류코드사용유무(Y/N)
, TYPE_CD_SEQ INT                   -- 분석종류코드순서
, REG_DT      DATE  
, MODIFY_DT   DATE
, CONSTRAINT  SCHEDULE_POL_TY PRIMARY KEY(TYPE_CD)
);

-- 스케줄 분석종류별 수행내역
CREATE TABLE CELLPLAN.SCHEDULE_TY_PROC
(
  TYPE_CD        VARCHAR2(10)  NOT NULL -- 분석종류  
, PROC_SEQ       VARCHAR2(2)   NOT NULL -- 분석종류별 처리순서
, PROC_KIND      VARCHAR2(2)   NOT NULL -- 프로세스종류(etl,postgre,hadoop)
, PROC_PKG_NM    VARCHAR2(100) NOT NULL -- 프로세스종류(etl,postgre,hadoop)
, PROC_CLASS_NM  VARCHAR2(100) NOT NULL -- 프로세스종류(etl,postgre,hadoop)
, PROC_METHOD_NM VARCHAR2(100) NOT NULL -- 프로세스종류(etl,postgre,hadoop)
, REG_DT      DATE  
, MODIFY_DT   DATE
, CONSTRAINT  SCHEDULE_POL_TY PRIMARY KEY(TYPE_CD)
);
