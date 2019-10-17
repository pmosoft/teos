package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraSiteSql {

def selectSiteCsv(scenarioId:String) = {
s"""
SELECT
       SCENARIO_ID                      ||'|'||
       ENB_ID                           ||'|'||
       PCI                              ||'|'||
       PCI_PORT                         ||'|'||
       SITE_NM                          ||'|'||
       XPOSITION                        ||'|'||
       YPOSITION                        ||'|'||
       HEIGHT                           ||'|'||
       BLT_HEIGHT                       ||'|'||
       TOWER_HEIGHT                     ||'|'||
       SITE_ADDR                        ||'|'||
       TYPE                             ||'|'||
       CORRECTION_VALUE                 ||'|'||
       FEEDER_LOSS                      ||'|'||
       FADE_MARGIN                      ||'|'||
       STATUS                           ||'|'||
       MSC                              ||'|'||
       BSC                              ||'|'||
       NETWORKID                        ||'|'||
       USABLETRAFFICCH                  ||'|'||
       SYSTEMID                         ||'|'||
       STRYPOS                          ||'|'||
       STRXPOS                          ||'|'||
       ATTENUATION                      ||'|'||
       SITE_GUBUN                       ||'|'||
       RU_ID                            ||'|'||
       RADIUS                           ||'|'||
       NOISEFLOOR                       ||'|'||
       FA_SEQ                           ||'|'||
       RU_TYPE                          ||'|'||
       REG_DT                           ||'|'||
       SISUL_CD                         ||'|'||
       TM_XPOSITION                     ||'|'||
       TM_YPOSITION                     ||'|'||
       RU_DIV_CD                        ||'|'||
       ${scenarioId}                    ||'|'
FROM   SITE
"""
}

}