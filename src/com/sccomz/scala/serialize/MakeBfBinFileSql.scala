package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;
import com.sccomz.scala.comm.App

object MakeBfBinFileSql {

def main(args: Array[String]): Unit = {
}

def selectResultNrBfScenHeader(scheduleId:String) = {
    // int    buildingIndex; // Building Index
    // char   cTBDKey[20];	 // TBD Key
    // UCha   xyz[4];			   // 0 : X Bin Count
    // 				               // 1 : Y Bin count
    //                       // 2 : Z floor
    //                       // 3 : Padding
    // float  startX, startY;	// Building Border letf bottom
    // ULLong	startPointBin;	// start point of BIN data
s"""
SELECT BUILDING_INDEX
     , TBD_KEY
     , NX
     , NY
     , FLOORZ
     , EXT_SX
     , EXT_SY
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = ${scheduleId}
ORDER BY BUILDING_INDEX
"""
}

def selectResultNrBfRuHeader(scheduleId:String) = {
s"""
SELECT RU_ID
     , TBD_KEY
     , BUILDING_INDEX
     , NX
     , NY
     , FLOORZ
     , EXT_SX
     , EXT_SY
     , SCHEDULE_ID
FROM   RESULT_NR_BF_RU_HEADER
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

def selectBldCount() = {
s"""
SELECT COUNT(DISTINCT TBD_KEY) AS bldCount
FROM   M_RESULT_NR_BF_SCEN_HEADER
"""
}

// RESOLUTION
def selectResolution(scheduleId:String) = {
s"""
SELECT RESOLUTION
FROM   SCENARIO
WHERE  SCENARIO_ID = (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}


// Value Setting..
def selectSectorResult(scheduleId:String,tabNm:String,colNm:String) = {
s"""
SELECT DISTINCT
       X_POINT
     , Y_POINT
     , ${colNm}
FROM   ${tabNm}
WHERE  SCHEDULE_ID = ${scheduleId}
ORDER BY X_POINT, Y_POINT
"""
}

def selectRuResultAll(scheduleId:String, tabNm:String, colNm:String) = {
s"""
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = ${scheduleId}
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT RU_ID, X_POINT, Y_POINT, ${colNm} AS VALUE
FROM
(
SELECT DISTINCT
       A.RU_ID, B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       ${colNm}
 FROM  ${tabNm} A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID       = B.RU_ID
   AND A.SCHEDULE_ID = ${scheduleId}
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
)
WHERE X_POINT < X_BIN_CNT
AND   Y_POINT < Y_BIN_CNT
"""
}


def selectRuResult2(ruId:String) = {
s"""
SELECT X_POINT, Y_POINT, VALUE
FROM   ENG_RU
WHERE  RU_ID = ${ruId}
"""
}

def selectRuResult(scheduleId:String, tabNm:String, colNm:String, ruId:String) = {
s"""
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = ${scheduleId}
   AND A.SCENARIO_ID = B.SCENARIO_ID
   AND A.RU_ID       = ${ruId}
)
SELECT X_POINT, Y_POINT, ${colNm}
FROM
(
SELECT DISTINCT
       B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       ${colNm}
 FROM  ${tabNm} A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID       = B.RU_ID
   AND A.SCHEDULE_ID = ${scheduleId}
   AND A.RU_ID       = ${ruId}
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
)
WHERE X_POINT < X_BIN_CNT
AND   Y_POINT < Y_BIN_CNT
"""
}

}