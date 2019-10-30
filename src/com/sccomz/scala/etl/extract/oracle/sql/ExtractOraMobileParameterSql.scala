package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraMobileParameterSql {
  
def selectMobileParameterCsv(scheduleId:String) = {
s"""
SELECT 
       NVL(SCENARIO_ID,0)   ||'|'||
       NVL(MOBILE_ID,0)     ||'|'||
       NVL(TYPE,0)          ||'|'||
       MOBILENAME           ||'|'||
       MAKER                ||'|'||
       NVL(MINPOWER,0)      ||'|'||
       NVL(MAXPOWER,0)      ||'|'||
       NVL(MOBILEGAIN,0)    ||'|'||
       NVL(NOISEFLOOR,0)    ||'|'||
       NVL(HEIGHT,0)        ||'|'||
       NVL(BODYLOSS,0)      ||'|'||
       NVL(BUILDINGLOSS,0)  ||'|'||
       NVL(CARLOSS,0)       ||'|'||
       NVL(FEEDERLOSS,0)    ||'|'||
       NVL(NOISEFIGURE,0)   ||'|'||
       NVL(DIVERSITYGAIN,0) ||'|'||
       NVL(ANTENNAGAIN,0)   ||'|'||
       NVL(RX_LAYER,0)      ||'|'||
       ${scheduleId}        ||'|'
FROM   DU
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectMobileParameterIns(scheduleId:String) = {
s"""
SELECT 
'INSERT INTO I_MOBILE_PARAMETER VALUES ('
||' '  ||NVL(SCENARIO_ID,0)   
||','  ||NVL(MOBILE_ID,0)     
||','  ||NVL(TYPE,0)          
||','''||MOBILENAME           ||''''
||','''||MAKER                ||''''
||','  ||NVL(MINPOWER,0)      
||','  ||NVL(MAXPOWER,0)      
||','  ||NVL(MOBILEGAIN,0)    
||','  ||NVL(NOISEFLOOR,0)    
||','  ||NVL(HEIGHT,0)        
||','  ||NVL(BODYLOSS,0)      
||','  ||NVL(BUILDINGLOSS,0)  
||','  ||NVL(CARLOSS,0)       
||','  ||NVL(FEEDERLOSS,0)    
||','  ||NVL(NOISEFIGURE,0)   
||','  ||NVL(DIVERSITYGAIN,0) 
||','  ||NVL(ANTENNAGAIN,0)   
||','  ||NVL(RX_LAYER,0)      
||');'
FROM   MOBILE_PARAMETER
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}



}