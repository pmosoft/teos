package com.sccomz.scala.etl.extract.oracle.sql

object ExtractOraNrsystemSql {
  
  
  
def selectNrsystemCsv(scheduleId:String) = {
s"""
SELECT 
       NVL(SYSTEM_ID,0)                     ||'|'||
       NVL(FA_SEQ,0)                        ||'|'||
       NVL(BANDWIDTH_PER_CC,0)              ||'|'||
       NVL(NUMBER_OF_CC,0)                  ||'|'||
       NVL(NUMBER_OF_SC_PER_RB,0)           ||'|'||
       NVL(TOTALBANDWIDTH,0)                ||'|'||
       NVL(SUBCARRIERSPACING,0)             ||'|'||
       NVL(RB_PER_CC,0)                     ||'|'||
       NVL(RADIOFRAMELENGTH,0)              ||'|'||
       NVL(SUBFRAMELENGTH,0)                ||'|'||
       NVL(NO_SLOTPERRADIOFRAME,0)          ||'|'||
       NVL(SLOTLENGTH,0)                    ||'|'||
       NVL(NO_OFDMSYMBOLPERSUBFRAME,0)      ||'|'||
       NVL(FRAMECONFIGURATION,0)            ||'|'||
       NVL(DLDATARATIO,0)                   ||'|'||
       NVL(ULDATARATIO,0)                   ||'|'||
       NVL(DLBLER,0)                        ||'|'||
       NVL(ULBLER,0)                        ||'|'||
       NVL(DIVERSITYGAINRATIO,0)            ||'|'||
       NVL(DLINTRACELL,0)                   ||'|'||
       NVL(DLINTERCELL,0)                   ||'|'||
       NVL(ULINTRACELL,0)                   ||'|'||
       NVL(ULINTERCELL,0)                   ||'|'||
       NVL(DLCOVERAGELIMITRSRP,0)           ||'|'||
       NVL(INTERFERENCEMARGIN,0)            ||'|'||
       NVL(NRGAINOVERLTE,0)                 ||'|'||
       NVL(PENETRATIONLOSS,0)               ||'|'||
       NVL(SLOT_CONFIGURATION,0)            ||'|'||
       NVL(DLCOVERAGELIMITRSRPLOS,0)        ||'|'||
       NVL(DLOH,0)                          ||'|'||
       NVL(ULOH,0)                          ||'|'||
       NVL(DLSINROFFSET,0)                  ||'|'||
       NVL(TECHTYPE,0)                      ||'|'||
       NVL(RAYTRACING_REFLECTION,0)         ||'|'||
       NVL(RAYTRACING_DIFFRACTION,0)        ||'|'||
       NVL(RAYTRACING_SCATTERING,0)         ||'|'||
       NVL(RELATED_ANALYSIS_COVLIMITRSRP,0) ||'|'||
       ENV_ROADSIDE_TREE_YN                 ||'|'||
       DLCOVLIMITRSRP_YN                    ||'|'||
       ANT_CATEGORY                         ||'|'||
       ANT_NM                               ||'|'||
       NVL(SCENARIO_ID,0)                   ||'|'
FROM   NRSYSTEM
WHERE  SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

def selectNrsystemIns(scheduleId:String) = {
s"""
"""
}



}