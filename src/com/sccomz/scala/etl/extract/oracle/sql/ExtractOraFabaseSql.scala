package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraFabaseSql {

def selectFabaseCsv() = {
s"""
SELECT 
       NVL(FA_SEQ,0)                           ||'|'||
       NVL(SYSTEMTYPE,0)                       ||'|'||
       NVL(CHNO,0)                             ||'|'||
       NVL(UPLINKFREQ,0)                       ||'|'||
       NVL(DOWNLINKFREQ,0)                     ||'|'||
       NVL(ANALY_CHECK,0)                      ||'|'||
       PASSLOSS_MODEL                          ||'|'||
       FREQ                                    ||'|'||
       UPLINKFREQ_COMMENT                      ||'|'||
       DOWNLINKFREQ_COMMENT                    ||'|'||
       TO_CHAR(REG_DT,'YYYY-MM-DD HH24:MI:SS') ||'|'||
       REG_ID                                  ||'|'
FROM   FABASE
"""
}


}