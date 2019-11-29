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


/***************************************************************
 * Sector
 ***************************************************************/
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
SELECT BUILDING_INDEX                        -- 0
     , TBD_KEY                               -- 1
     , NX                                    -- 2
     , NY                                    -- 3
     , FLOORZ                                -- 4
     , CAST(EXT_SX AS FLOAT)      AS EXT_SX  -- 5
     , CAST(EXT_SY AS FLOAT)      AS EXT_SY  -- 6
     , CAST(NX*NY*FLOORZ AS LONG) AS BIN_CNT -- 7
     , CAST(SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ       AS INTEGER) AS START_POINT_BIN  -- 8
     , CAST((SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ) * 4 AS INTEGER) AS START_POINT_4BIN -- 9
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = ${scheduleId}
ORDER BY BUILDING_INDEX
"""
}


def selectSumBinCnt(scheduleId:String) = {
s"""
SELECT CAST(SUM(BIN_CNT) AS LONG)    AS SUM_BIN_CNT
     , CAST(SUM(BIN_CNT) AS INTEGER) AS SUM_BIN_CNT2
FROM   M_RESULT_NR_BF_SCEN_HEADER
"""
}

def selectBldCount() = {
s"""
SELECT CAST(COUNT(DISTINCT TBD_KEY) AS INTEGER) AS bldCount
FROM   M_RESULT_NR_BF_SCEN_HEADER
"""
}

// RESOLUTION
def selectResolution(scheduleId:String) = {
s"""
SELECT CAST(BUILDINGANALYSIS3D_RESOLUTION AS INTEGER) AS RESOLUTION
FROM   SCENARIO
WHERE  SCENARIO_ID = (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

// Value Setting..
def selectSectorResult(scheduleId:String,tabNm:String,colNm:String) = {
        // 요청한 인덱스에 대한 값위치
        //        int iPosition = startPosition
        //                      + ( rtHead.getX()*rtHead.getY()*iZ*4 ) //층
        //                      + ( rtHead.getX()*iY*4 )               //X
        //                      + ( iX*4 );                            //Y
  
//     , NX                                    -- 2
//     , NY                                    -- 3
//     , FLOORZ                                -- 4
s"""
SELECT A.TBD_KEY                                  AS TBD_KEY          -- 1               
     , A.RX_FLOORZ                                AS RX_FLOORZ        -- 2
     , A.RX_TM_YPOS                               AS RX_TM_YPOS       -- 3
     , A.RX_TM_XPOS                               AS RX_TM_XPOS       -- 4
     , CAST(${colNm} AS INTEGER)                  AS VALUE            -- 5
     , CAST(${colNm} AS FLOAT)                    AS VALUE2           -- 6
     , CAST(
       B.START_POINT_BIN                                                
     + (B.NX*B.NY*A.RX_FLOORZ)
     + (B.NX*A.RX_TM_YPOS)
     + (A.RX_TM_XPOS)
       AS INTEGER)                                AS POS              -- 7
FROM   ${tabNm} A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = ${scheduleId}
AND    A.TBD_KEY = B.TBD_KEY
AND    A.RX_FLOORZ < B.FLOORZ
AND    A.RX_TM_YPOS < B.NY
AND    A.RX_TM_XPOS < B.NX
ORDER BY RX_FLOORZ, RX_TM_YPOS, RX_TM_XPOS
"""
}

/***************************************************************
 * Ru
 ***************************************************************/

def selectResultNrBfRuHeader(scheduleId:String) = {
s"""
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDY,
       A.RESOLUTION,
       CONCAT(B.USER_ID,'/',A.SCENARIO_ID) AS SECTOR_PATH,
       CONCAT(B.USER_ID,'/',A.SCENARIO_ID,'/ENB_',A.ENB_ID,'/PCI_',A.PCI,'_PORT_',A.PCI_PORT,'_',A.RU_ID) AS RU_PATH
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = ${scheduleId}
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT A.RU_ID                -- 0
     , A.BUILDING_INDEX       -- 1
     , A.TBD_KEY              -- 2
     , A.NX                   -- 3
     , A.NY                   -- 4
     , A.FLOORZ               -- 5
     , A.EXT_SX               -- 6
     , A.EXT_SY               -- 7
     , A.BIN_CNT              -- 8
     , A.START_POINT_BIN      -- 9
     , A.START_POINT_4BIN     -- 10
     , B.RU_PATH              -- 11  
FROM 
      (SELECT RU_ID                                 -- 0
            , BUILDING_INDEX                        -- 1
            , TBD_KEY                               -- 2
            , NX                                    -- 3
            , NY                                    -- 4
            , FLOORZ                                -- 5
            , CAST(EXT_SX AS FLOAT)      AS EXT_SX  -- 6
            , CAST(EXT_SY AS FLOAT)      AS EXT_SY  -- 7
            , CAST(NX*NY*FLOORZ AS LONG) AS BIN_CNT -- 8
            , CAST(SUM(NX*NY*FLOORZ) OVER (PARTITION BY RU_ID,BUILDING_INDEX ORDER BY RU_ID,BUILDING_INDEX) - NX*NY*FLOORZ       AS INTEGER) AS START_POINT_BIN  -- 9
            , CAST((SUM(NX*NY*FLOORZ) OVER (PARTITION BY RU_ID,BUILDING_INDEX ORDER BY RU_ID,BUILDING_INDEX) - NX*NY*FLOORZ) * 4 AS INTEGER) AS START_POINT_4BIN -- 10
       FROM   RESULT_NR_BF_RU_HEADER
       WHERE  SCHEDULE_ID = ${scheduleId}
       ORDER BY RU_ID, BUILDING_INDEX) A, RU B
WHERE  A.RU_ID       = B.RU_ID
"""
}

def selectBfSumBinCnt(ruId:String) = {
s"""
SELECT CAST(SUM(BIN_CNT) AS LONG)    AS SUM_BIN_CNT
     , CAST(SUM(BIN_CNT) AS INTEGER) AS SUM_BIN_CNT2
FROM   M_RESULT_NR_BF_RU_HEADER
WHERE  RU_ID = ${ruId}
"""
}

def selectBfBldCount(ruId:String) = {
s"""
SELECT CAST(COUNT(DISTINCT TBD_KEY) AS INTEGER) AS bldCount
FROM   M_RESULT_NR_BF_RU_HEADER
WHERE  RU_ID = ${ruId}
"""
}

// RESOLUTION
def selectBfResolution(scheduleId:String) = {
s"""
SELECT CAST(BUILDINGANALYSIS3D_RESOLUTION AS INTEGER) AS RESOLUTION
FROM   SCENARIO
WHERE  SCENARIO_ID = (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})
"""
}

// Value Setting..
def selectBfResult(scheduleId:String,tabNm:String,colNm:String) = {
        // 요청한 인덱스에 대한 값위치
        //        int iPosition = startPosition
        //                      + ( rtHead.getX()*rtHead.getY()*iZ*4 ) //층
        //                      + ( rtHead.getX()*iY*4 )               //X
        //                      + ( iX*4 );                            //Y
  
//     , NX                                    -- 2
//     , NY                                    -- 3
//     , FLOORZ                                -- 4
s"""
SELECT A.TBD_KEY                                  AS TBD_KEY          -- 1               
     , A.RX_FLOORZ                                AS RX_FLOORZ        -- 2
     , A.RX_TM_YPOS                               AS RX_TM_YPOS       -- 3
     , A.RX_TM_XPOS                               AS RX_TM_XPOS       -- 4
     , CAST(${colNm} AS INTEGER)                  AS VALUE            -- 5
     , CAST(${colNm} AS FLOAT)                    AS VALUE2           -- 6
     , CAST(
       B.START_POINT_BIN                                                
     + (B.NX*B.NY*A.RX_FLOORZ)
     + (B.NX*A.RX_TM_YPOS)
     + (A.RX_TM_XPOS)
       AS INTEGER)                                AS POS              -- 7
FROM   ${tabNm} A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = ${scheduleId}
AND    A.TBD_KEY = B.TBD_KEY
AND    A.RX_FLOORZ < B.FLOORZ
AND    A.RX_TM_YPOS < B.NY
AND    A.RX_TM_XPOS < B.NX
ORDER BY RX_FLOORZ, RX_TM_YPOS, RX_TM_XPOS

"""
}

/***************************************************************
 * 기타
 ***************************************************************/


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
SELECT B.X_BIN_CNT, B.Y_BIN_CNT,
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