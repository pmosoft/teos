package com.sccomz.scala.schedule.control.sql

object ScheduleDaemonTestData {


def insertSCHEDULE_WEIGHT(scheduleId:String) = {
"""
DELETE FROM SCHEDULE_WEIGHT
;

INSERT INTO SCHEDULE_WEIGHT
SELECT
       SYSDATE AS BASE_DT              -- 기준일시
     ,  101000 AS 2D_W5                -- 2D임계치5(RU갯수*BIN갯수)
     ,  101000 AS 2D_W4                -- 2D임계치4(RU갯수*BIN갯수)
     ,  101000 AS 2D_W3                -- 2D임계치3(RU갯수*BIN갯수)
     ,  101000 AS 2D_W2                -- 2D임계치2(RU갯수*BIN갯수)
     ,  101000 AS 2D_W1                -- 2D임계치1(RU갯수*BIN갯수)
     ,  101000 AS 3D_W5                -- 3D임계치5(RU갯수*BIN갯수+빌딩)
     ,  101000 AS 3D_W4                -- 3D임계치4(RU갯수*BIN갯수+빌딩)
     ,  101000 AS 3D_W3                -- 3D임계치3(RU갯수*BIN갯수+빌딩)
     ,  101000 AS 3D_W2                -- 3D임계치2(RU갯수*BIN갯수+빌딩)
     ,  101000 AS 3D_W1                -- 3D임계치1(RU갯수*BIN갯수+빌딩)
     ,      4  AS JOB_H_THRESHOLD      -- 하이잡임계치(1-5 사이의 값)
     ,      3  AS JOB_M_THRESHOLD      -- 미들잡임계치(1-5 사이의 값)
     ,      2  AS JOB_L_THRESHOLD      -- 로우잡임계치(1-5 사이의 값)
     ,      2  AS JOB_H_MAX_CNT        -- 하이잡 실행 최대 갯수
     ,      3  AS JOB_M_MAX_CNT        -- 미들잡 실행 최대 갯수
     ,      5  AS JOB_L_MAX_CNT        -- 로우잡 실행 최대 갯수
     , SYSDATE AS REG_DT               -- 등록일시
     , 'ADMIN' AS REG_USER_ID          -- 등록자
     , SYSDATE AS MOD_DT               -- 수정일시
     , 'ADMIN' AS MOD_USER_ID          -- 수정자
FROM   DUAL
;

SELECT * FROM SCHEDULE_WEIGHT
;
"""
}



}