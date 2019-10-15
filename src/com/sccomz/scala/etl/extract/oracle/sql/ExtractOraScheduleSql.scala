package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraScheduleSql {

def selectScheduleCsv(scheduleId:String) = {
s"""
SELECT
       TYPE_CD                                    ||'|'||
       SCENARIO_ID                                ||'|'||
       USER_ID                                    ||'|'||
       PRIORITIZE                                 ||'|'||
       PROCESS_CD                                 ||'|'||
       PROCESS_MSG                                ||'|'||
       SCENARIO_PATH                              ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')    ||'|'||
       TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       RETRY_CNT                                  ||'|'||
       SERVER_ID                                  ||'|'||
       BIN_X_CNT                                  ||'|'||
       BIN_Y_CNT                                  ||'|'||
       RU_CNT                                     ||'|'||
       ANALYSIS_WEIGHT                            ||'|'||
       PHONE_NO                                   ||'|'||
       RESULT_TIME                                ||'|'||
       TILT_PROCESS_TYPE                          ||'|'||
       GEOMETRYQUERY_SCHEDULE_ID                  ||'|'||
       RESULT_BIT                                 ||'|'||
       INTERWORKING_INFO                          ||'|'||
       SCHEDULE_ID                                ||'|'
FROM   SCHEDULE
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

def selectScheduleIns(scheduleId:String) = {
s"""
SELECT
  || "'" +  SCHEDULE_ID
  || "'" +  TYPE_CD
  || "'" +  SCENARIO_ID
  || "'" +  USER_ID
  || "'" +  PRIORITIZE
  || "'" +  PROCESS_CD
  || "'" +  PROCESS_MSG
  || "'" +  SCENARIO_PATH
  || "'" +  TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')
  || "'" +  TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS')
  || "'" +  RETRY_CNT
  || "'" +  SERVER_ID
  || "'" +  BIN_X_CNT
  || "'" +  BIN_Y_CNT
  || "'" +  RU_CNT
  || "'" +  ANALYSIS_WEIGHT
  || "'" +  PHONE_NO
  || "'" +  RESULT_TIME
  || "'" +  TILT_PROCESS_TYPE
  || "'" +  GEOMETRYQUERY_SCHEDULE_ID
  || "'" +  RESULT_BIT
  || "'" +  INTERWORKING_INFO
FROM   SCHEDULE
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}



}