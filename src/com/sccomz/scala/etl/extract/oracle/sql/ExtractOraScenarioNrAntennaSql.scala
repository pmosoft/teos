package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraScenarioNrAntennaSql {

def selectScenarioNrAntennaCsv(scheduleId:String) = {
s"""
SELECT
       SCENARIO_ID                     ||'|'||
       ANTENA_SEQ                      ||'|'||
       RU_ID                           ||'|'||
       ANTENA_NM                       ||'|'||
       ORIENTATION                     ||'|'||
       TILTING                         ||'|'||
       ANTENA_ORD                      ||'|'||
       LIMIT_TILTING                   ||'|'||
       RU_SEQ                          ||'|'||
       ${scheduleId}                   ||'|'
FROM
(
SELECT ANTENA.SCENARIO_ID         AS SCENARIO_ID
     , ANTENA.ANTENA_SEQ          AS ANTENA_SEQ
     , ANTENA.RU_ID               AS RU_ID
     , ANTENA.ANTENA_NM           AS ANTENA_NM
     , NVL(ANTENA.ORIENTATION, 0) AS ORIENTATION
     , NVL(ANTENA.TILTING, 0)     AS TILTING
     , ANTENA.ANTENA_ORD          AS ANTENA_ORD
     , ANTENA.LIMIT_TILTING       AS LIMIT_TILTING
     , TRU.RU_SEQ                 AS RU_SEQ
FROM   RU_ANTENA  ANTENA
     , ANTENABASE BASE
     , RU         TRU
WHERE  ANTENA.SCENARIO_ID = ${scheduleId}
AND    TRU.SCENARIO_ID    = ${scheduleId}
AND    ANTENA.ANTENA_SEQ  = BASE.ANTENA_SEQ 
AND    TRU.RU_ID          = ANTENA.RU_ID
)
"""
}


def selectScenarioNrAntennaIns(scheduleId:String) = {
s"""
"""
}

}