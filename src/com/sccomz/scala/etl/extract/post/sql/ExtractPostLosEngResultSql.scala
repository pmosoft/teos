package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosEngResultSql {

def selectLosEngResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       SECTOR_ID                  ||'|'||
       BIN_ID                     ||'|'||
       BIN_X                      ||'|'||
       BIN_Y                      ||'|'||
       BIN_Z                      ||'|'||
       BLD_ID                     ||'|'||
       LOS                        ||'|'||
       IN_BLD                     ||'|'||
       THETA_DEG                  ||'|'||
       PHI_DEG                    ||'|'||
       SECTOR_X                   ||'|'||
       SECTOR_Y                   ||'|'||
       SECTOR_Z                   ||'|'||
       '${scheduleId}'            ||'|'
FROM   LOS_ENG_RESULT_DIS
WHERE  SCHEDULE_ID = ${scheduleId}
AND    SECTOR_ID   = ${ruId}
"""
}

}