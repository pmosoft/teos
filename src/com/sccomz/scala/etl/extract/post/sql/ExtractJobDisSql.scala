package com.sccomz.scala.etl.extract.post.sql

object ExtractJobDisSql {

def selectBdYn(scenarioId:String) = {
s"""
SELECT CASE WHEN COUNT(*) = 1 THEN 'Y' ELSE 'N' END AS BD_YN
FROM   SCHEDULE 
WHERE  SCENARIO_ID = ${scenarioId} 
AND    TYPE_CD     = 'SC051'"""
}
  
  
  
def selectRuCnt(scheduleId:String,stat:Int) = {
s"""
SELECT A.RU_CNT , A.POST_DONE_CNT, A.POST_DONE_CNT - B.EXT_DONE_CNT EXT_CNT, B.EXT_ING_CNT, B.EXT_DONE_CNT
FROM  (
		   SELECT COUNT(A.RU_ID)                                 AS RU_CNT
		        , COUNT(CASE WHEN A.STAT=${stat} THEN 1 ELSE NULL END) AS POST_DONE_CNT
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


def selectExtRu(scheduleId:String,stat:Int) = {
s"""
SELECT A.RU_ID
     , A.CLUSTER_NAME	     
FROM  (SELECT * 
       FROM   JOB_DIS
       WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
       AND    STAT = ${stat}) A
       LEFT OUTER JOIN (
       SELECT DISTINCT SCHEDULE_ID, RU_ID 
       FROM   JOB_DIS_ETL 
       WHERE  SCHEDULE_ID = '${scheduleId}'
       AND    STAT IN (5)   
       ) B
       ON   A.RU_ID       = B.RU_ID
WHERE  B.RU_ID IS NULL
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