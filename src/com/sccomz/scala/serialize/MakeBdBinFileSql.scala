package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;
import com.sccomz.scala.comm.App

object MakeBdBinFileSql {

def main(args: Array[String]): Unit = {
}

def selectResultNrBfScenHeader(scheduleId:String) = {
s"""
SELECT TBD_KEY
     , BUILDING_INDEX
     , NX
     , NY
     , FLOORZ
     , EXT_SX
     , EXT_SY
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = ${scheduleId}
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

// X,Y Total Bin 갯수
def selectBinCnt(scheduleId:String) = {
s"""
SELECT BIN_X_CNT
     , BIN_Y_CNT
FROM   SCHEDULE
WHERE  SCHEDULE_ID = ${scheduleId}
"""
}

// Directory Name Create
def selectBinFilePath(scheduleId:String) = {
s"""
SELECT B.SCHEDULE_ID
     , A.X_BIN_CNT, A.Y_BIN_CNT
     --, DATE_FORMAT(CURRENT_DATE(), 'yyyyMMdd') AS TODATE
     , B.USER_ID
     , A.ENB_ID
     , A.PCI
     , A.PCI_PORT
     , A.RU_ID
     , CONCAT(B.USER_ID,'/',A.SCENARIO_ID) AS SECTOR_PATH
     , CONCAT(B.USER_ID,'/',A.SCENARIO_ID,'/ENB_',A.ENB_ID,'/PCI_',A.PCI,'_PORT_',A.PCI_PORT,'_',A.RU_ID) AS RU_PATH
FROM   SCENARIO_NR_RU A, SCHEDULE B
WHERE  B.SCHEDULE_ID = ${scheduleId}
AND    A.SCENARIO_ID = B.SCENARIO_ID
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