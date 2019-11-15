package com.sccomz.scala.etl.extract.post.sql

object ExtractPostScenarioNrRuDemSql {

def selectScenarioNrRuDemCsv(scheduleId:String) = {
s"""
SELECT
	     ENB_ID         ||'|'||
	     PCI            ||'|'||
	     PCI_PORT       ||'|'||
	     RU_ID          ||'|'||
	     DEM_HEGHT      ||'|'||
	     BLD_AVG_HEIGHT ||'|'||
	     SCENARIO_ID    ||'|'
FROM   SCENARIO_NR_RU_DEM
WHERE  SCENARIO_ID IN (SELECT CAST(SCENARIO_ID AS TEXT) FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

}