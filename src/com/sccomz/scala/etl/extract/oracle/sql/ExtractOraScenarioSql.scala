package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraScenarioSql {

def selectScenarioCsv(scheduleId:String) = {
s"""
SELECT
       NVL(SCENARIO_ID,0)                          ||'|'||
       SCENARIO_NM                                 ||'|'||
       USER_ID                                     ||'|'||
       NVL(SYSTEM_ID,0)                            ||'|'||
       NVL(NETWORK_TYPE,0)                         ||'|'||
       SIDO_CD                                     ||'|'||
       SIGUGUN_CD                                  ||'|'||
       DONG_CD                                     ||'|'||
       SIDO                                        ||'|'||
       SIGUGUN                                     ||'|'||
       DONG                                        ||'|'||
       NVL(STARTX,0)                               ||'|'||
       NVL(STARTY,0)                               ||'|'||
       NVL(ENDX,0)                                 ||'|'||
       NVL(ENDY,0)                                 ||'|'||
       NVL(FA_MODEL_ID,0)                          ||'|'||
       NVL(FA_SEQ,0)                               ||'|'||
       SCENARIO_DESC                               ||'|'||
       NVL(USE_BUILDING,0)                         ||'|'||
       NVL(USE_MULTIFA,0)                          ||'|'||
       NVL(PRECISION,0)                            ||'|'||
       NVL(PWRCTRLCHECKPOINT,0)                    ||'|'||
       NVL(MAXITERATIONPWRCTRL,0)                  ||'|'||
       NVL(RESOLUTION,0)                           ||'|'||
       NVL(MODEL_RADIUS,0)                         ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')     ||'|'||
       TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS')  ||'|'||
       NVL(UPPER_SCENARIO_ID,0)                    ||'|'||
       NVL(FLOORBUILDING,0)                        ||'|'||
       NVL(FLOOR,0)                                ||'|'||
       NVL(FLOORLOSS,0)                            ||'|'||
       NVL(SCENARIO_SUB_ID,0)                      ||'|'||
       NVL(SCENARIO_SOLUTION_NUM,0)                ||'|'||
       NVL(LOSS_TYPE,0)                            ||'|'||
       NVL(RU_CNT,0)                               ||'|'||
       MODIFY_YN                                   ||'|'||
       BATCH_YN                                    ||'|'||
       NVL(TM_STARTX,0)                            ||'|'||
       NVL(TM_STARTY,0)                            ||'|'||
       NVL(TM_ENDX,0)                              ||'|'||
       NVL(TM_ENDY,0)                              ||'|'||
       REAL_DATE                                   ||'|'||
       REAL_DRM_FILE                               ||'|'||
       REAL_COMP                                   ||'|'||
       REAL_CATT                                   ||'|'||
       REAL_CATY                                   ||'|'||
       REPLACE(BLD_TYPE,'|','#')                   ||'|'||
       NVL(RET_PERIOD,0)                           ||'|'||
       RET_END_DATETIME                            ||'|'||
       BUILDINGANALYSIS3D_YN                       ||'|'||
       NVL(BUILDINGANALYSIS3D_RESOLUTION,0)        ||'|'||
       NVL(AREA_ID,0)                              ||'|'||
       BUILDINGANALYSIS3D_RELATED_YN               ||'|'||
       NVL(RELATED_ANALYSIS_COVLIMITRSRP,0)        ||'|'||
       ${scheduleId}                               ||'|'
FROM   SCENARIO
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectScenarioIns(scheduleId:String) = {
s"""
SELECT 
'INSERT INTO I_SCENARIO VALUES ('
||' '  ||NVL(SCENARIO_ID,0)                         
||','''||SCENARIO_NM                                ||''''
||','''||USER_ID                                    ||''''
||','  ||NVL(SYSTEM_ID,0)                           
||','  ||NVL(NETWORK_TYPE,0)                        
||','''||SIDO_CD                                    ||''''
||','''||SIGUGUN_CD                                 ||''''
||','''||DONG_CD                                    ||''''
||','''||SIDO                                       ||''''
||','''||SIGUGUN                                    ||''''
||','''||DONG                                       ||''''
||','  ||NVL(STARTX,0)                              
||','  ||NVL(STARTY,0)                              
||','  ||NVL(ENDX,0)                                
||','  ||NVL(ENDY,0)                                
||','  ||NVL(FA_MODEL_ID,0)                         
||','  ||NVL(FA_SEQ,0)                              
||','''||SCENARIO_DESC                              ||''''
||','  ||NVL(USE_BUILDING,0)                        
||','  ||NVL(USE_MULTIFA,0)                         
||','  ||NVL(PRECISION,0)                           
||','  ||NVL(PWRCTRLCHECKPOINT,0)                   
||','  ||NVL(MAXITERATIONPWRCTRL,0)                 
||','  ||NVL(RESOLUTION,0)                          
||','  ||NVL(MODEL_RADIUS,0)                        
||','''||TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')    ||''''
||','''||TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') ||''''
||','  ||NVL(UPPER_SCENARIO_ID,0)                   
||','  ||NVL(FLOORBUILDING,0)                       
||','  ||NVL(FLOOR,0)                               
||','  ||NVL(FLOORLOSS,0)                           
||','  ||NVL(SCENARIO_SUB_ID,0)                     
||','  ||NVL(SCENARIO_SOLUTION_NUM,0)               
||','  ||NVL(LOSS_TYPE,0)                           
||','  ||NVL(RU_CNT,0)                              
||','''||MODIFY_YN                                  ||''''
||','''||BATCH_YN                                   ||''''
||','  ||NVL(TM_STARTX,0)                           
||','  ||NVL(TM_STARTY,0)                           
||','  ||NVL(TM_ENDX,0)                             
||','  ||NVL(TM_ENDY,0)                             
||','''||REAL_DATE                                  ||''''
||','''||REAL_DRM_FILE                              ||''''
||','''||REAL_COMP                                  ||''''
||','''||REAL_CATT                                  ||''''
||','''||REAL_CATY                                  ||''''
||','''||BLD_TYPE                                   ||''''
||','  ||NVL(RET_PERIOD,0)                          
||','''||RET_END_DATETIME                           ||''''
||','''||BUILDINGANALYSIS3D_YN                      ||''''
||','  ||NVL(BUILDINGANALYSIS3D_RESOLUTION,0)       
||','  ||NVL(AREA_ID,0)                             
||','''||BUILDINGANALYSIS3D_RELATED_YN              ||''''
||','  ||NVL(RELATED_ANALYSIS_COVLIMITRSRP,0)       
||');'
FROM   SCENARIO
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

}