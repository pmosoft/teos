package com.sccomz.scala.etl.extract.post.sql

object ExtractPostTreeBldResultSql {

def selectTreeBldResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       TBD                                       ||'|'||
       FLOOR_X                                   ||'|'||
       FLOOR_Y                                   ||'|'||
       FLOOR_Z                                   ||'|'||
       DISTANCE                                  ||'|'||
       '${scheduleId}'                           ||'|'||
       '${ruId}'                                 ||'|'
FROM   TREE_ENG_RESULT
WHERE  SCHEDULE_ID = ${scheduleId}
AND    RU_ID  = '${ruId}'
"""
}

}

