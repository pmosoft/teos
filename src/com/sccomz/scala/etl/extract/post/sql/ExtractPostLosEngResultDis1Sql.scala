package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosEngResultDis1Sql {
  
def selectLosEngResultDis1Csv(scheduleId:String) = {
s"""
SELECT
       JOB_ID                            ||'|'||
       SECTOR_ID                         ||'|'||
       BIN_ID                            ||'|'||
       BIN_X                             ||'|'||
       BIN_Y                             ||'|'||
       BIN_SIZE                          ||'|'||
       LOS                               ||'|'||
       '${scheduleId}'                   ||'|'
FROM LOS_ENG_RESULT_DIS1
"""
}
  
}