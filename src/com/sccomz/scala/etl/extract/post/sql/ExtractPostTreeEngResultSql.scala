package com.sccomz.scala.etl.extract.post.sql

object ExtractPostTreeEngResultSql {

def selectTreeEngResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       BIN_X                                          ||'|'||
       BIN_Y                                          ||'|'||
       DISTANCE                                       ||'|'||
       '${scheduleId}'                                ||'|'||
       '${ruId}'                                      ||'|'
FROM   TREE_ENG_RESULT
WHERE  SCHEDULE_ID = ${scheduleId}
AND    RU_ID  = '${ruId}'
"""
}

}
