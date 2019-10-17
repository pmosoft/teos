package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraDUSql {
  
def selectDUCsv(scenarioId:String) = {
s"""
SELECT
       SCENARIO_ID                       ||'|'||
       ENB_ID                            ||'|'||
       E_NODEB_NM                        ||'|'||
       PCI_CNT                           ||'|'||
       STRMAKER                          ||'|'||
       REG_DT                            ||'|'||
       ${scenarioId}                     ||'|'
FROM   DU
"""
}

}