package com.sccomz.scala.schedule.control.sql

object ScheduleDaemonTestData {


def insertSCHEDULE_WEIGHT(scheduleId:String) = {
"""
DELETE FROM SCHEDULE_WEIGHT
;

INSERT INTO SCHEDULE_WEIGHT
SELECT
       SYSDATE AS BASE_DT               -- 기준일시
     ,  10000  AS RU_CNT_W5             -- RU갯수가중치5값
     ,   5000  AS RU_CNT_W4             -- RU갯수가중치4값
     ,   1000  AS RU_CNT_W3             -- RU갯수가중치3값
     ,    100  AS RU_CNT_W2             -- RU갯수가중치2값
     ,     10  AS RU_CNT_W1             -- RU갯수가중치1값
     , 100000  AS AREA_W5               -- 면적가중치5값
     ,  50000  AS AREA_W4               -- 면적가중치4값
     ,  10000  AS AREA_W3               -- 면적가중치3값
     ,   1000  AS AREA_W2               -- 면적가중치2값
     ,    100  AS AREA_W1               -- 면적가중치1값
     ,    0.8  AS JOB_RU_CNT_WEIGHT     -- 잡RU갯수가중치(0..1 RU갯수가중치+면접가중치=1)
     ,    0.2  AS JOB_AREA_WEIGHT       -- 잡면적가중치(0..1 RU갯수가중치+면접가중치=1)
     ,      4  AS JOB_H_THRESHOLD       -- 하이잡임계치(1-5 사이의 값)
     ,      3  AS JOB_M_THRESHOLD       -- 미들잡임계치(1-5 사이의 값)
     ,      2  AS JOB_L_THRESHOLD       -- 로우잡임계치(1-5 사이의 값)
     ,      2  AS JOB_H_MAX_CNT         -- 하이잡 실행 최대 갯수
     ,      3  AS JOB_M_MAX_CNT         -- 미들잡 실행 최대 갯수
     ,      5  AS JOB_L_MAX_CNT         -- 로우잡 실행 최대 갯수
     , SYSDATE AS REG_DT                -- 등록일시
     , 'ADMIN' AS REG_USER_ID           -- 등록자
     , SYSDATE AS MOD_DT                -- 수정일시
     , 'ADMIN' AS MOD_USER_ID           -- 수정자
FROM   DUAL
;

SELECT * FROM SCHEDULE_WEIGHT
;
"""
}



}