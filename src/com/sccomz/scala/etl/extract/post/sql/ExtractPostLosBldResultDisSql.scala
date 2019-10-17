package com.sccomz.scala.etl.extract.post.sql

object ExtractPostLosBldResultDisSql {
  
def selectLosBldResultDisCsv(scenarioId:String) = {
s"""
SELECT
       JOB_ID                            ||'|'||
       SCENARIO_ID                       ||'|'||
       SECTOR_ID                         ||'|'||
       TBD                               ||'|'||
       FLOOR_X                           ||'|'||
       FLOOR_Y                           ||'|'||
       FLOOR_Z                           ||'|'||
       LOS                               ||'|'||
       '${scenarioId}'                   ||'|'
FROM LOS_BLD_RESULT_DIS
"""
}
  
}