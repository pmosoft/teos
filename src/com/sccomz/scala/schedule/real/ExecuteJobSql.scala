package com.sccomz.scala.schedule.real

object ExecuteJobSql {

def insertScheduleStep(scheduleId:String, typeStepCd:String) = {
s"""
INSERT INTO SCHEDULE_STEP
SELECT 
       A.SCHEDULE_ID   AS SCHEDULE_ID 
     , A.TYPE_CD       AS TYPE_CD    
     , '${typeStepCd}' AS TYPE_STEP_CD
     , SYSDATE         AS START_DT    
     , SYSDATE         AS END_DT      
     , 0               AS PROCESS_TIME
     , '진행중'          AS PROCESS_LOG 
     , SYSDATE         AS REG_DT	    
     , 'ADMIN'         AS REG_USER_ID 
     , SYSDATE         AS MOD_DT      
     , 'ADMIN'         AS MOD_USER_ID 
FROM   SCHEDULE A
WHERE  A.SCHEDULE_ID = ${scheduleId}
"""
}

def selectScheduleStep(scheduleId:String) = {
s"""
SELECT MIN(TYPE_STEP_CD) AS MIN_TYPE_STEP_CD
     , MAX(TYPE_STEP_CD) AS MAX_TYPE_STEP_CD
FROM   SCHEDULE_STEP 
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

def updateScheduleStep(scheduleId:String, typeStepCd:String, processLog:String) = {
s"""
UPDATE SCHEDULE_STEP
SET    END_DT       = SYSDATE
     , PROCESS_TIME = TRUNC(ROUND((SYSDATE-START_DT)*24*60*60))
     , PROCESS_LOG  ='${processLog}'   
     , MOD_DT       = SYSDATE
WHERE  SCHEDULE_ID  = ${scheduleId}
AND    TYPE_STEP_CD ='${typeStepCd}'
""" 
}

def updateScheduleProcessCd(scheduleId:String, processCd:String, processLog:String) = {
s"""
UPDATE SCHEDULE
SET    PROCESS_CD   = '${processCd}'
     , PROCESS_LOG  ='${processLog}'   
     , MODIFY_DT    = SYSDATE
WHERE  SCHEDULE_ID  = ${scheduleId}
""" 
}



def deleteScheduleStep(scheduleId:String, typeStepCd:String) = {
s"""
DELETE FROM SCHEDULE_STEP
WHERE  SCHEDULE_ID = ${scheduleId}
AND    TYPE_CD     ='${typeStepCd}'
"""
}

def deleteScheduleStepAll(scheduleId:String) = {
s"""
DELETE FROM SCHEDULE_STEP
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}


}