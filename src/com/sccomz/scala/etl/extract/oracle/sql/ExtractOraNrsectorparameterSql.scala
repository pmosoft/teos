package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraNrsectorparameterSql {
  
def selectNrsectorparameterCsv(scheduleId:String) = {
s"""
SELECT 
       NVL(ENB_ID,0)                    ||'|'||
       NVL(PCI,0)                       ||'|'||
       NVL(PCI_PORT,0)                  ||'|'||
       RU_ID                            ||'|'||
       NVL(TXPWRWATT,0)                 ||'|'||
       NVL(TXPWRDBM,0)                  ||'|'||
       NVL(TXEIRPWATT,0)                ||'|'||
       NVL(TXEIRPDBM,0)                 ||'|'||
       NVL(RETXEIRPWATT,0)              ||'|'||
       NVL(RETXEIRPDBM,0)               ||'|'||
       NVL(TRXCOUNT,0)                  ||'|'||
       NVL(DLMODULATION,0)              ||'|'||
       NVL(DLMIMOTYPE,0)                ||'|'||
       NVL(DLMIMOGAIN,0)                ||'|'||
       NVL(ULMODULATION,0)              ||'|'||
       NVL(ULMIMOTYPE,0)                ||'|'||
       NVL(ULMIMOGAIN,0)                ||'|'||
       NVL(LOSBEAMFORMINGLOSS,0)        ||'|'||
       NVL(NLOSBEAMFORMINGLOSS,0)       ||'|'||
       NVL(HANDOVERCALLDROPTHRESHOLD,0) ||'|'||
       NVL(POWERCOMBININGGAIN,0)        ||'|'||
       NVL(BEAMMISMATCHMARGIN,0)        ||'|'||
       NVL(ANTENNAGAIN,0)               ||'|'||
       NVL(FOLIAGELOSS,0)               ||'|'||
       NVL(TXLAYER,0)                   ||'|'||
       NVL(RXLAYER,0)                   ||'|'||
       NVL(SCENARIO_ID,0)               ||'|'
FROM   NRSECTORPARAMETER
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectNrsectorparameterIns(scheduleId:String) = {
s"""
"""
}



}