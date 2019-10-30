package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraSiteSql {

def selectSiteCsv(scheduleId:String) = {
s"""
SELECT 
       NVL(SCENARIO_ID,0)                      ||'|'||
       ENB_ID                                  ||'|'||
       NVL(PCI,0)                              ||'|'||
       NVL(PCI_PORT,0)                         ||'|'||
       SITE_NM                                 ||'|'||
       XPOSITION                               ||'|'||
       YPOSITION                               ||'|'||
       NVL(HEIGHT,0)                           ||'|'||
       NVL(BLT_HEIGHT,0)                       ||'|'||
       NVL(TOWER_HEIGHT,0)                     ||'|'||
       SITE_ADDR                               ||'|'||
       TYPE                                    ||'|'||
       NVL(CORRECTION_VALUE,0)                 ||'|'||
       NVL(FEEDER_LOSS,0)                      ||'|'||
       NVL(FADE_MARGIN,0)                      ||'|'||
       STATUS                                  ||'|'||
       NVL(MSC,0)                              ||'|'||
       NVL(BSC,0)                              ||'|'||
       NVL(NETWORKID,0)                        ||'|'||
       NVL(USABLETRAFFICCH,0)                  ||'|'||
       NVL(SYSTEMID,0)                         ||'|'||
       STRYPOS                                 ||'|'||
       STRXPOS                                 ||'|'||
       NVL(ATTENUATION,0)                      ||'|'||
       SITE_GUBUN                              ||'|'||
       RU_ID                                   ||'|'||
       NVL(RADIUS,0)                           ||'|'||
       NVL(NOISEFLOOR,0)                       ||'|'||
       FA_SEQ                                  ||'|'||
       NVL(RU_TYPE,0)                          ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       SISUL_CD                                ||'|'||
       TM_XPOSITION                            ||'|'||
       TM_YPOSITION                            ||'|'||
       NVL(RU_DIV_CD,0)                        ||'|'||
       ${scheduleId}                           ||'|'
FROM   SITE
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectSiteIns(scheduleId:String) = {
s"""
SELECT 
'INSERT INTO SITE VALUES ('
||' '  ||NVL(SCENARIO_ID,0)                      
||','''||ENB_ID                                  ||''''
||','  ||NVL(PCI,0)                              
||','  ||NVL(PCI_PORT,0)                         
||','''||SITE_NM                                 ||''''
||','''||XPOSITION                               ||''''
||','''||YPOSITION                               ||''''
||','  ||NVL(HEIGHT,0)                           
||','  ||NVL(BLT_HEIGHT,0)                       
||','  ||NVL(TOWER_HEIGHT,0)                     
||','''||SITE_ADDR                               ||''''
||','''||TYPE                                    ||''''
||','  ||NVL(CORRECTION_VALUE,0)                 
||','  ||NVL(FEEDER_LOSS,0)                      
||','  ||NVL(FADE_MARGIN,0)                      
||','''||STATUS                                  ||''''
||','  ||NVL(MSC,0)                              
||','  ||NVL(BSC,0)                              
||','  ||NVL(NETWORKID,0)                        
||','  ||NVL(USABLETRAFFICCH,0)                  
||','  ||NVL(SYSTEMID,0)                         
||','''||STRYPOS                                 ||''''
||','''||STRXPOS                                 ||''''
||','  ||NVL(ATTENUATION,0)                      
||','''||SITE_GUBUN                              ||''''
||','''||RU_ID                                   ||''''
||','  ||NVL(RADIUS,0)                           
||','  ||NVL(NOISEFLOOR,0)                       
||','''||FA_SEQ                                  ||''''
||','  ||NVL(RU_TYPE,0)                          
||','''||TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||''''
||','''||SISUL_CD                                ||''''
||','''||TM_XPOSITION                            ||''''
||','''||TM_YPOSITION                            ||''''
||','  ||NVL(RU_DIV_CD,0)                        
||')'
FROM   SITE
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

}