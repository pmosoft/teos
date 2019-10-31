package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraScenarioNrRuSql {

def selectScenarioNrRuCsv(scheduleId:String) = {
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



SELECT 
FROM
( 
SELECT T_DU.SCENARIO_ID                 AS SCENARIO_ID
     , T_DU.ENB_ID                      AS ENB_ID     
     , T_RU.PCI                         AS PCI        
     , T_RU.PCI_PORT                    AS PCI_PORT   
     , T_RU.RU_ID                       AS RU_ID      
     , T_RU.MAKER                       AS MAKER      
     , T_RU.SECTOR_ORD                  AS SECTOR_ORD 
     , NVL(T_RU.REPEATERATTENUATION, 0) AS REPEATERATTENUATION
     , NVL(T_RU.REPEATERPWRRATIO, 0)    AS REPEATERPWRRATIO
     , T_RU.RU_SEQ                      AS RU_SEQ 
     , T_SITE.RADIUS                    AS RADIUS                
     , T_SITE.FEEDER_LOSS               AS FEEDER_LOSS           
     , T_SITE.NOISEFLOOR                AS NOISEFLOOR            
     , T_SITE.CORRECTION_VALUE          AS CORRECTION_VALUE      
     , T_SITE.FADE_MARGIN               AS FADE_MARGIN           
     , T_SITE.TM_XPOSITION              AS XPOSITION       
     , T_SITE.TM_YPOSITION              AS YPOSITION             
     , T_SITE.HEIGHT                    AS HEIGHT             
     , T_SITE.SITE_ADDR                 AS SITE_ADDR             
     , T_SITE.TYPE                      AS TYPE                  
     , T_SITE.STATUS                    AS STATUS                
     , T_SITE.SISUL_CD                  AS SISUL_CD              
     , T_SITE.MSC                       AS MSC                   
     , T_SITE.BSC                       AS BSC                   
     , T_SITE.NETWORKID                 AS NETWORKID             
     , T_SITE.USABLETRAFFICCH           AS USABLETRAFFICCH       
     , T_SITE.SYSTEMID                  AS SYSTEMID              
     , NVL(T_SITE.RU_TYPE, -1)          AS RU_TYPE
     , T_SCENARIO.FA_MODEL_ID           AS FA_MODEL_ID  
     , T_SCENARIO.NETWORK_TYPE          AS NETWORK_TYPE 
     , T_SCENARIO.RESOLUTION            AS RESOLUTION   
     , NVL(T_SCENARIO.FA_SEQ, 0)        AS FA_SEQ
     , CASE WHEN T_SITE.TM_XPOSITION - T_SITE.RADIUS < T_SCENARIO.TM_STARTX THEN  T_SCENARIO.TM_STARTX ELSE T_SITE.TM_XPOSITION - T_SITE.RADIUS END AS SITE_STARTX
     , CASE WHEN T_SITE.TM_YPOSITION - T_SITE.RADIUS < T_SCENARIO.TM_STARTY THEN  T_SCENARIO.TM_STARTY ELSE T_SITE.TM_YPOSITION - T_SITE.RADIUS END AS SITE_STARTY
     , CASE WHEN T_SITE.TM_XPOSITION + T_SITE.RADIUS > T_SCENARIO.TM_ENDX THEN  T_SCENARIO.TM_ENDX ELSE T_SITE.TM_XPOSITION + T_SITE.RADIUS END     AS SITE_ENDX
     , CASE WHEN T_SITE.TM_YPOSITION + T_SITE.RADIUS > T_SCENARIO.TM_ENDY THEN  T_SCENARIO.TM_ENDY ELSE T_SITE.TM_YPOSITION + T_SITE.RADIUS END     AS SITE_ENDY
     , FLOOR ((
       TRUNC(CASE WHEN T_SITE.TM_XPOSITION + T_SITE.RADIUS > T_SCENARIO.TM_ENDX THEN  T_SCENARIO.TM_ENDX ELSE T_SITE.TM_XPOSITION + T_SITE.RADIUS END)
       - TRUNC(CASE WHEN T_SITE.TM_XPOSITION - T_SITE.RADIUS < T_SCENARIO.TM_STARTX THEN  T_SCENARIO.TM_STARTX ELSE T_SITE.TM_XPOSITION - T_SITE.RADIUS END)
       ) / T_SCENARIO.RESOLUTION )                                                                                                                  AS X_BIN_CNT
     , FLOOR ((
       TRUNC(CASE WHEN T_SITE.TM_YPOSITION + T_SITE.RADIUS > T_SCENARIO.TM_ENDY THEN  T_SCENARIO.TM_ENDY ELSE T_SITE.TM_YPOSITION + T_SITE.RADIUS END )
       - TRUNC(CASE WHEN T_SITE.TM_YPOSITION - T_SITE.RADIUS < T_SCENARIO.TM_STARTY THEN  T_SCENARIO.TM_STARTY ELSE T_SITE.TM_YPOSITION - T_SITE.RADIUS END )
       ) / T_SCENARIO.RESOLUTION )                                                                                                                  AS Y_BIN_CNT
FROM   SCENARIO T_SCENARIO
     , DU       T_DU
     , SITE     T_SITE
     ,(SELECT SCENARIO_ID
            , ENB_ID
            , PCI
            , PCI_PORT
            , RU_ID
            , SECTOR_ORD
            , MAX(MAKER) AS MAKER
            , MAX(REPEATERATTENUATION) AS REPEATERATTENUATION
            , MAX(REPEATERPWRRATIO) AS REPEATERPWRRATIO
            , MAX(RU_SEQ) AS RU_SEQ
       FROM   RU
       WHERE  SCENARIO_ID = 5103982
       GROUP BY SCENARIO_ID, ENB_ID, PCI, PCI_PORT, RU_ID, SECTOR_ORD
       ) T_RU
WHERE  T_DU.SCENARIO_ID       = T_SCENARIO.SCENARIO_ID
AND    T_DU.SCENARIO_ID       = T_RU.SCENARIO_ID
AND    T_DU.ENB_ID            = T_RU.ENB_ID
AND    T_RU.SCENARIO_ID       = T_SITE.SCENARIO_ID
AND    T_RU.ENB_ID            = T_SITE.ENB_ID
AND    T_RU.PCI               = T_SITE.PCI
AND    T_RU.PCI_PORT          = T_SITE.PCI_PORT
AND    T_RU.RU_ID             = T_SITE.RU_ID
AND    T_SITE.TYPE            IN ('RU', 'RU_N')
AND    T_SITE.STATUS          = 1
AND    T_SCENARIO.SCENARIO_ID =  5103982
ORDER BY T_RU.ENB_ID, T_RU.PCI, T_RU.PCI_PORT, T_RU.RU_ID
)

"""
}

def selectScenarioNrRuIns(scheduleId:String) = {
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