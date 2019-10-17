package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosBldResultDisSql {
  
def selectLosBldResultDisCsv(scheduleId:String) = {
s"""
SELECT
       JOB_ID                            ||'|'||
       SECTOR_ID                         ||'|'||
       TBD                               ||'|'||
       FLOOR_X                           ||'|'||
       FLOOR_Y                           ||'|'||
       FLOOR_Z                           ||'|'||
       LOS                               ||'|'||
       '${scheduleId}'                   ||'|'
FROM LOS_BLD_RESULT_DIS
"""
}
  
}