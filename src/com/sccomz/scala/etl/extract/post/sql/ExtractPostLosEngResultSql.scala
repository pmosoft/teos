package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosEngResultSql {

def selectLosEngResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       BIN_X                                          ||'|'||
       BIN_Y                                          ||'|'||
       BIN_Z                                          ||'|'||
       CASE WHEN LOS IS TRUE THEN 1 ELSE 0 END        ||'|'||
       THETA_DEG                                      ||'|'||
       PHI_DEG                                        ||'|'||
       CASE WHEN IN_BLD IS TRUE THEN 'T' ELSE 'F' END ||'|'||
       '${scheduleId}'                                ||'|'||
       '${ruId}'                                      ||'|'
FROM   LOS_ENG_RESULT
WHERE  SCHEDULE_ID = ${scheduleId}
AND    RU_ID  = '${ruId}'
"""
}

}