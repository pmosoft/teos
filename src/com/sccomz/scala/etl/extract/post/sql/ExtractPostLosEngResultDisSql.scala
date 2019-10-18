package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosEngResultDisSql {
  
def selectLosEngResultDisCsv(scheduleId:String) = {
s"""
SELECT
       JOB_ID                            ||'|'||
       SCHEDULE_ID                       ||'|'||
       BIN_ID                            ||'|'||
       BIN_X                             ||'|'||
       BIN_Y                             ||'|'||
       BIN_Z                             ||'|'||
       BIN_SIZE                          ||'|'||
       LOS                               ||'|'||
       THETA                             ||'|'||
       PHI                               ||'|'||
       SECTOR_ID                         ||'|'||
       SECTOR_X                          ||'|'||
       SECTOR_Y                          ||'|'||
       SECTOR_Z                          ||'|'||
       '${scheduleId}'                   ||'|'
FROM   LOS_ENG_RESULT_DIS
LIMIT  100
"""
}

}