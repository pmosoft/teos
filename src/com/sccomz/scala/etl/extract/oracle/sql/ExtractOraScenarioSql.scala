package com.sccomz.scala.etl.extract.sql

object ExtractOraScenarioSql {

def selectScenarioCsv(scheduleId:String) = {
s"""
SELECT
       SCENARIO_ID                       ||'|'||
       SCENARIO_NM                       ||'|'||
       USER_ID                           ||'|'||
       SYSTEM_ID                         ||'|'||
       NETWORK_TYPE                      ||'|'||
       SIDO_CD                           ||'|'||
       SIGUGUN_CD                        ||'|'||
       DONG_CD                           ||'|'||
       SIDO                              ||'|'||
       SIGUGUN                           ||'|'||
       DONG                              ||'|'||
       STARTX                            ||'|'||
       STARTY                            ||'|'||
       ENDX                              ||'|'||
       ENDY                              ||'|'||
       FA_MODEL_ID                       ||'|'||
       FA_SEQ                            ||'|'||
       SCENARIO_DESC                     ||'|'||
       USE_BUILDING                      ||'|'||
       USE_MULTIFA                       ||'|'||
       PRECISION                         ||'|'||
       PWRCTRLCHECKPOINT                 ||'|'||
       MAXITERATIONPWRCTRL               ||'|'||
       RESOLUTION                        ||'|'||
       MODEL_RADIUS                      ||'|'||
       REG_DT                            ||'|'||
       MODIFY_DT                         ||'|'||
       UPPER_SCENARIO_ID                 ||'|'||
       FLOORBUILDING                     ||'|'||
       FLOOR                             ||'|'||
       FLOORLOSS                         ||'|'||
       SCENARIO_SUB_ID                   ||'|'||
       SCENARIO_SOLUTION_NUM             ||'|'||
       LOSS_TYPE                         ||'|'||
       RU_CNT                            ||'|'||
       MODIFY_YN                         ||'|'||
       BATCH_YN                          ||'|'||
       TM_STARTX                         ||'|'||
       TM_STARTY                         ||'|'||
       TM_ENDX                           ||'|'||
       TM_ENDY                           ||'|'||
       REAL_DATE                         ||'|'||
       REAL_DRM_FILE                     ||'|'||
       REAL_COMP                         ||'|'||
       REAL_CATT                         ||'|'||
       REAL_CATY                         ||'|'||
       BLD_TYPE                          ||'|'||
       RET_PERIOD                        ||'|'||
       RET_END_DATETIME                  ||'|'||
       BUILDINGANALYSIS3D_YN             ||'|'||
       BUILDINGANALYSIS3D_RESOLUTION     ||'|'||
       AREA_ID                           ||'|'||
       BUILDINGANALYSIS3D_RELATED_YN     ||'|'||
       RELATED_ANALYSIS_COVLIMITRSRP     ||'|'
FROM   SCENARIO
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectScenarioIns(scheduleId:String) = {
s"""
SELECT
       SCHEDULE_ID                                ||'|'||
       TYPE_CD                                    ||'|'||
       SCENARIO_ID                                ||'|'||
       USER_ID                                    ||'|'||
       PRIORITIZE                                 ||'|'||
       PROCESS_CD                                 ||'|'||
       PROCESS_MSG                                ||'|'||
       SCENARIO_PATH                              ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS')    ||'|'||
       TO_CHAR(MODIFY_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       RETRY_CNT                                  ||'|'||
       SERVER_ID                                  ||'|'||
       BIN_X_CNT                                  ||'|'||
       BIN_Y_CNT                                  ||'|'||
       RU_CNT                                     ||'|'||
       ANALYSIS_WEIGHT                            ||'|'||
       PHONE_NO                                   ||'|'||
       RESULT_TIME                                ||'|'||
       TILT_PROCESS_TYPE                          ||'|'||
       GEOMETRYQUERY_SCHEDULE_ID                  ||'|'||
       RESULT_BIT                                 ||'|'||
       INTERWORKING_INFO                          ||'|'
FROM   SCHEDULE
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}



}