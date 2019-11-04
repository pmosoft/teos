package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosEngResultSql {

def selectLosEngResultCsv(scheduleId:String, ruId:String) = {
s"""
SELECT
       COALESCE(BIN_ID,'')        ||'|'||
       BIN_X                      ||'|'||
       BIN_Y                      ||'|'||
       BIN_Z                      ||'|'||
       COALESCE(BLD_ID,0)         ||'|'||
       LOS                        ||'|'||
       IN_BLD                     ||'|'||
       THETA_DEG                  ||'|'||
       PHI_DEG                    ||'|'||
       SECTOR_X                   ||'|'||
       SECTOR_Y                   ||'|'||
       SECTOR_Z                   ||'|'||
       '${scheduleId}'            ||'|'||
       '${ruId}'                  ||'|'
FROM   LOS_ENG_RESULT
--WHERE  SCHEDULE_ID = ${scheduleId}
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM I_SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
AND    SECTOR_ID   = '${ruId}'
"""
}

}