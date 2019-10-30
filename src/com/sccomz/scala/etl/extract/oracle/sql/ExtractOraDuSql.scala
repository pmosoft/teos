package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraDuSql {
  
def selectDuCsv(scheduleId:String) = {
s"""
SELECT
       SCENARIO_ID                             ||'|'||
       ENB_ID                                  ||'|'||
       E_NODEB_NM                              ||'|'||
       NVL(PCI_CNT,0)                          ||'|'||
       STRMAKER                                ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       ${scheduleId}                           ||'|'
FROM   DU
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectDuIns(scheduleId:String) = {
s"""
SELECT 
'INSERT INTO I_DU VALUES ('
||' '  ||NVL(SCENARIO_ID,0)                      
||','''||ENB_ID                                  ||''''
||','''||E_NODEB_NM                              ||''''
||','  ||NVL(PCI_CNT,0)                          
||','''||STRMAKER                                ||''''
||','''||TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||''''
||');'
FROM   DU
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}



}