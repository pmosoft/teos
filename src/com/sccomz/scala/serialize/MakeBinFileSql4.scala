package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;

import com.sccomz.scala.comm.App


object MakeBinFileSql4 {


def main(args: Array[String]): Unit = {
}

// X,Y Total Bin Count
def selectBinCnt(scheduleId:String) = {
s"""
SELECT A.BIN_X_CNT
     , A.BIN_Y_CNT
FROM   SCHEDULE A
WHERE  A.SCHEDULE_ID = ${scheduleId}
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

def selectRuResult(scheduleId:String,tabNm:String, ruId:String) = {
s"""
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) / (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTX,
       INT(A.SITE_STARTY) / (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = ${scheduleId}
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT A.SCHEDULE_ID,
       B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
       (((A.RX_TM_XPOS / (A.RESOLUTION * A.RESOLUTION)) - A.SITE_STARTX) / B.RESOLUTION) AS X_POINT,
       (((A.RX_TM_YPOS / (A.RESOLUTION * A.RESOLUTION)) - A.SITE_STARTY) / B.RESOLUTION) AS Y_POINT,
       VALUE
 FROM  ${tabNm} A, RU B
 WHERE A.SCHEDULE_ID = ${scheduleId}
   AND A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.RU_ID = ${ruId}
"""
}

}