package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraRUSql {

def selectRUCsv(scenarioId:String) = {
s"""
SELECT
       SCENARIO_ID                      ||'|'||
       ENB_ID                           ||'|'||
       PCI                              ||'|'||
       PCI_PORT                         ||'|'||
       RU_ID                            ||'|'||
       MAKER                            ||'|'||
       SITE_TYPE                        ||'|'||
       PAIR_ENODEB                      ||'|'||
       REPEATERATTENUATION              ||'|'||
       REPEATERPWRRATIO                 ||'|'||
       RU_NM                            ||'|'||
       FA_SEQ                           ||'|'||
       SECTOR_ORD                       ||'|'||
       RU_SEQ                           ||'|'||
       RRH_SEQ                          ||'|'||
       REG_DT                           ||'|'||
       SWING_YN                         ||'|'||
       ANT_CHK_YN                       ||'|'||
       TILT_YN                          ||'|'||
       FA_SEQ_ORG                       ||'|'||
       ${scenarioId}                    ||'|'
FROM   RU
"""
}

}