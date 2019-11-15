package com.sccomz.scala.etl.extract.post.sql

object ExtractJobDisSql {

def selectRuCnt(scheduleId:String) = {
s"""
SELECT A.RU_CNT , A.EXT_CNT, B.EXT_ING_CNT, B.EXT_DONE_CNT
FROM  (
		   SELECT COUNT(A.RU_ID)                              AS RU_CNT
		        , COUNT(CASE WHEN A.STAT=3 THEN 1 ELSE NULL END) AS EXT_CNT
		   FROM   JOB_DIS A
		   WHERE  A.SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
       ) A,
      (		
		   SELECT COUNT(CASE WHEN B.STAT=4 THEN 1 ELSE NULL END) AS EXT_ING_CNT
		        , COUNT(CASE WHEN B.STAT=5 THEN 1 ELSE NULL END) AS EXT_DONE_CNT
		   FROM   JOB_DIS_ETL B
		   WHERE  B.SCHEDULE_ID = '${scheduleId}'
	     ) B
"""
}

def selectExtRu(scheduleId:String) = {
s"""
SELECT RU_ID
     , CLUSTER_NAME
FROM   JOB_DIS
--WHERE  SCHEDULE_ID = ${scheduleId}
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
AND    STAT = 3
"""
}  

def updateJobDisExt(scheduleId:String, ruId:String, stat:String) = {
s"""
UPDATE JOB_DIS_ETL
SET    STAT = ${stat}
WHERE  SCHEDULE_ID = '${scheduleId}'
AND    RU_ID = '${ruId}'
"""
}

def insertJobDisExt(scheduleId:String, ruId:String, stat:String) = {
s"""
INSERT INTO JOB_DIS_ETL VALUES ('${scheduleId}','${ruId}',${stat},NULL);
"""
}

}