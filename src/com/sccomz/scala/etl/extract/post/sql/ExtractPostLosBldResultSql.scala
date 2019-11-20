package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosBldResultSql {

def selectLosBldResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       TBD                                       ||'|'||
       FLOOR_X                                   ||'|'||
       FLOOR_Y                                   ||'|'||
       FLOOR_Z                                   ||'|'||
       FLOOR_DEM                                 ||'|'||
       THETA_DEG                                 ||'|'||
       PHI_DEG                                   ||'|'||
       CASE WHEN LOS IS TRUE THEN 1 ELSE 0 END   ||'|'||
       '${scheduleId}'                           ||'|'||
       '${ruId}'                                 ||'|'
FROM   LOS_BLD_RESULT
WHERE  SCHEDULE_ID = ${scheduleId}
AND    RU_ID  = '${ruId}'
"""
}

}
