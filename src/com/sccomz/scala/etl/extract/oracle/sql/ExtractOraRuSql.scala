package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraRuSql {

def selectRuCsv(scheduleId:String) = {
s"""
SELECT
       NVL(SCENARIO_ID,0)                      ||'|'||
       ENB_ID                                  ||'|'||
       NVL(PCI,0)                              ||'|'||
       NVL(PCI_PORT,0)                         ||'|'||
       RU_ID                                   ||'|'||
       MAKER                                   ||'|'||
       SITE_TYPE                               ||'|'||
       NVL(PAIR_ENODEB,0)                      ||'|'||
       NVL(REPEATERATTENUATION,0)              ||'|'||
       NVL(REPEATERPWRRATIO,0)                 ||'|'||
       RU_NM                                   ||'|'||
       NVL(FA_SEQ,0)                           ||'|'||
       NVL(SECTOR_ORD,0)                       ||'|'||
       NVL(RU_SEQ,0)                           ||'|'||
       NVL(RRH_SEQ,0)                          ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       NVL(SWING_YN,0)                         ||'|'||
       NVL(ANT_CHK_YN,0)                       ||'|'||
       NVL(TILT_YN,0)                          ||'|'||
       NVL(FA_SEQ_ORG,0)                       ||'|'||
       ${scheduleId}                           ||'|'
FROM   RU
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectRuIns(scheduleId:String) = {
s"""
SELECT 
'INSERT INTO I_RU VALUES ('
||' '  ||NVL(SCENARIO_ID,0)                      
||','''||ENB_ID                                  ||''''
||','  ||NVL(PCI,0)                              
||','  ||NVL(PCI_PORT,0)                         
||','''||RU_ID                                   ||''''
||','''||MAKER                                   ||''''
||','''||TRIM(SITE_TYPE)                         ||''''
||','  ||NVL(PAIR_ENODEB,0)                      
||','  ||NVL(REPEATERATTENUATION,0)              
||','  ||NVL(REPEATERPWRRATIO,0)                 
||','''||RU_NM                                   ||''''
||','  ||NVL(FA_SEQ,0)                           
||','  ||NVL(SECTOR_ORD,0)                       
||','  ||NVL(RU_SEQ,0)                           
||','  ||NVL(RRH_SEQ,0)                          
||','''||TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||''''
||','  ||NVL(SWING_YN,0)                         
||','  ||NVL(ANT_CHK_YN,0)                       
||','  ||NVL(TILT_YN,0)                          
||','  ||NVL(FA_SEQ_ORG,0)                       
||');'
FROM   RU
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}






}