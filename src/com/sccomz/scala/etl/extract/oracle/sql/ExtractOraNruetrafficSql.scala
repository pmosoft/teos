package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraNruetrafficSql {
  
def selectNruetrafficCsv(scheduleId:String) = {
s"""
SELECT 
       NVL(SYSTEM_ID,0)     ||'|'||
       NVL(FA_SEQ,0)        ||'|'||
       NVL(DLUL_TYPE,0)     ||'|'||
       NVL(RX_MODULATION,0) ||'|'||
       NVL(RX_LAYER,0)      ||'|'||
       NVL(MODULATION1,0)   ||'|'||
       NVL(MODULATION2,0)   ||'|'||
       NVL(MODULATION3,0)   ||'|'||
       NVL(MODULATION4,0)   ||'|'||
       NVL(MODULATION5,0)   ||'|'||
       NVL(MODULATION6,0)   ||'|'||
       NVL(MODULATION7,0)   ||'|'||
       NVL(MODULATION8,0)   ||'|'||
       NVL(MODULATION9,0)   ||'|'||
       NVL(MODULATION10,0)  ||'|'||
       NVL(MODULATION11,0)  ||'|'||
       NVL(MODULATION12,0)  ||'|'||
       NVL(MODULATION13,0)  ||'|'||
       NVL(MODULATION14,0)  ||'|'||
       NVL(MODULATION15,0)  ||'|'||
       NVL(LAYER1,0)        ||'|'||
       NVL(LAYER2,0)        ||'|'||
       NVL(LAYER3,0)        ||'|'||
       NVL(LAYER4,0)        ||'|'||
       NVL(LAYER5,0)        ||'|'||
       NVL(LAYER6,0)        ||'|'||
       NVL(LAYER7,0)        ||'|'||
       NVL(LAYER8,0)        ||'|'||
       NVL(LAYER9,0)        ||'|'||
       NVL(LAYER10,0)       ||'|'||
       NVL(LAYER11,0)       ||'|'||
       NVL(LAYER12,0)       ||'|'||
       NVL(LAYER13,0)       ||'|'||
       NVL(LAYER14,0)       ||'|'||
       NVL(LAYER15,0)       ||'|'||
       NVL(CODERATE1,0)     ||'|'||
       NVL(CODERATE2,0)     ||'|'||
       NVL(CODERATE3,0)     ||'|'||
       NVL(CODERATE4,0)     ||'|'||
       NVL(CODERATE5,0)     ||'|'||
       NVL(CODERATE6,0)     ||'|'||
       NVL(CODERATE7,0)     ||'|'||
       NVL(CODERATE8,0)     ||'|'||
       NVL(CODERATE9,0)     ||'|'||
       NVL(CODERATE10,0)    ||'|'||
       NVL(CODERATE11,0)    ||'|'||
       NVL(CODERATE12,0)    ||'|'||
       NVL(CODERATE13,0)    ||'|'||
       NVL(CODERATE14,0)    ||'|'||
       NVL(CODERATE15,0)    ||'|'||
       NVL(SNR1,0)          ||'|'||
       NVL(SNR2,0)          ||'|'||
       NVL(SNR3,0)          ||'|'||
       NVL(SNR4,0)          ||'|'||
       NVL(SNR5,0)          ||'|'||
       NVL(SNR6,0)          ||'|'||
       NVL(SNR7,0)          ||'|'||
       NVL(SNR8,0)          ||'|'||
       NVL(SNR9,0)          ||'|'||
       NVL(SNR10,0)         ||'|'||
       NVL(SNR11,0)         ||'|'||
       NVL(SNR12,0)         ||'|'||
       NVL(SNR13,0)         ||'|'||
       NVL(SNR14,0)         ||'|'||
       NVL(SNR15,0)         ||'|'||
       NVL(SCENARIO_ID,0)   ||'|'
FROM   NRUETRAFFIC 
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectNruetrafficIns(scheduleId:String) = {
s"""
"""
}



}