package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosAntGridDisSql {

def selectLosAntGridDisCsv(scheduleId:String) = {
s"""
SELECT
       JOB_ID          ||'|'||
       RU_ID           ||'|'||
       RU_POS          ||'|'||
       GRID_ID         ||'|'||
       GRID_POS        ||'|'||
       R               ||'|'||
       THETA_DEG       ||'|'||
       PHI_DEG         ||'|'
FROM   LOS_ANT_GRID_DIS
"""
}


}