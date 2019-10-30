package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraScheduleSql {

def selectScheduleCsv(scheduleId:String) = {
s"""
SELECT
       TYPE_CD                                    ||'|'||
       NVL(SCENARIO_ID,0)                         ||'|'||
       USER_ID                                    ||'|'||
       PRIORITIZE                                 ||'|'||
       PROCESS_CD                                 ||'|'||
       PROCESS_MSG                                ||'|'||
       SCENARIO_PATH                              ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')    ||'|'||
       TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       NVL(RETRY_CNT,0)                           ||'|'||
       SERVER_ID                                  ||'|'||
       NVL(BIN_X_CNT,0)                           ||'|'||
       NVL(BIN_Y_CNT,0)                           ||'|'||
       NVL(RU_CNT,0)                              ||'|'||
       NVL(ANALYSIS_WEIGHT,0)                     ||'|'||
       PHONE_NO                                   ||'|'||
       NVL(RESULT_TIME,0)                         ||'|'||
       NVL(TILT_PROCESS_TYPE,0)                   ||'|'||
       NVL(GEOMETRYQUERY_SCHEDULE_ID,0)           ||'|'||
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
'INSERT INTO SCHEDULE VALUES ('
||' '  ||NVL(SCHEDULE_ID,0)                         
||','''||TYPE_CD                                    ||''''
||','  ||NVL(SCENARIO_ID,0)                         
||','''||USER_ID                                    ||''''
||','''||PRIORITIZE                                 ||''''
||','''||PROCESS_CD                                 ||''''
||','''||PROCESS_MSG                                ||''''
||','''||SCENARIO_PATH                              ||''''
||','''||TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')    ||''''
||','''||TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') ||''''
||','  ||NVL(RETRY_CNT,0)                           
||','''||SERVER_ID                                  ||''''
||','  ||NVL(BIN_X_CNT,0)                           
||','  ||NVL(BIN_Y_CNT,0)                           
||','  ||NVL(RU_CNT,0)                              
||','  ||NVL(ANALYSIS_WEIGHT,0)                     
||','''||PHONE_NO                                   ||''''
||','  ||NVL(RESULT_TIME,0)                         
||','  ||NVL(TILT_PROCESS_TYPE,0)                   
||','  ||NVL(GEOMETRYQUERY_SCHEDULE_ID,0)           
||','''||RESULT_BIT                                 ||''''
||','''||INTERWORKING_INFO                          ||''''
||')'
FROM   SCHEDULE
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}



}