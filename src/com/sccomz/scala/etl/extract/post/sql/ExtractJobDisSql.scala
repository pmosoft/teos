package com.sccomz.scala.etl.extract.post.sql

object ExtractJobDisSql {

def selectRuCnt(scheduleId:String) = {
s"""
SELECT COUNT(RU_ID)                              AS RU_CNT
     , COUNT(CASE WHEN STAT=3 THEN 1 ELSE 0 END) AS EXT_CNT
     , COUNT(CASE WHEN STAT=4 THEN 1 ELSE 0 END) AS EXT_ING_CNT
     , COUNT(CASE WHEN STAT=5 THEN 1 ELSE 0 END) AS EXT_DONE_CNT
FROM   JOB_DIS
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

def selectExtRu(scheduleId:String) = {
s"""
SELECT RU_ID
     , CLUSTER_NAME
FROM   JOB_DIS
WHERE  SCHEDULE_ID = ${scheduleId}
AND    STAT = 3
"""
}

def updateRuStat(scheduleId:String, ruId:String, stat:String) = {
s"""
UPDATE JOB_DIS
SET    STAT = ${stat}
WHERE  SCHEDULE_ID = ${scheduleId}
AND    RU_ID = ${ruId}
"""
}


}