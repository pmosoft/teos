package com.sccomz.scala.test

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType
import scala.reflect.runtime.universe

object SparkSqlTest {
val spark = SparkSession.builder().appName("SparkSqlTest").getOrCreate()

def main(args: Array[String]): Unit = {
  //this.samToParquet(spark)
  //spark.stop();
}


def test11(spark: SparkSession) = {

spark.sql(s""" DROP TABLE M_RESULT_NR_BF_SCEN_HEADER """).take(1000).foreach(println);
  
spark.sql(s"""
SELECT BUILDING_INDEX -- 0
     , TBD_KEY        -- 1
     , NX             -- 2
     , NY             -- 3
     , FLOORZ         -- 4
     , EXT_SX         -- 5
     , EXT_SY         -- 7
     , NX*NY*FLOORZ      AS BIN_CNT
     , SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ      AS START_POINT_BIN
     ,(SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ) * 4 AS START_POINT_4BIN
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = 8460965
ORDER BY BUILDING_INDEX
""").cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER")
;

spark.sql(s"""
SELECT *
FROM   RESULT_NR_BF_SCEN_HEADER
""").take(1000).foreach(println)
;


//""").cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER")


spark.sql(s""" DROP TABLE M_BF_LOS_HEADER """).take(1000).foreach(println);

spark.sql(s"""
SELECT A.TBD_KEY                                  AS TBD_KEY          -- 1               
     , A.RX_FLOORZ                                AS RX_FLOORZ        -- 2
     , A.RX_TM_YPOS                               AS RX_TM_YPOS       -- 3
     , A.RX_TM_XPOS                               AS RX_TM_XPOS       -- 4
     , CAST(LOS AS INTEGER)                  AS VALUE            -- 5
     , CAST(LOS AS FLOAT)                    AS VALUE2           -- 6
     , CAST(
       B.START_POINT_BIN                                                
     + (B.NX*B.NY*A.RX_FLOORZ)
     + (B.NX*A.RX_TM_YPOS)
     + (A.RX_TM_XPOS)
       AS INTEGER)                                AS POS              -- 7
     , B.START_POINT_BIN
     , B.NX
     , B.NY
FROM   RESULT_NR_BF_LOS A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = 8460965
AND    A.TBD_KEY = B.TBD_KEY
AND    A.RX_FLOORZ < B.FLOORZ
AND    A.RX_TM_YPOS < B.NY
AND    A.RX_TM_XPOS < B.NX
ORDER BY A.RX_FLOORZ, A.RX_TM_YPOS, A.RX_TM_XPOS  
""").take(1000).foreach(println)
;

spark.sql(s"""
SELECT A.RX_FLOORZ                                AS RX_FLOORZ        -- 2
     , A.RX_TM_YPOS                               AS RX_TM_YPOS       -- 3
     , A.RX_TM_XPOS                               AS RX_TM_XPOS       -- 4
     , B.FLOORZ
     , B.NX
     , B.NY
FROM   RESULT_NR_BF_LOS A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = 8460965
AND    A.TBD_KEY = B.TBD_KEY
--AND    A.RX_FLOORZ < B.FLOORZ
--AND    A.RX_TM_YPOS < B.NY
--AND    A.RX_TM_XPOS < B.NX
ORDER BY A.RX_FLOORZ, A.RX_TM_YPOS, A.RX_TM_XPOS  
""").take(1000).foreach(println)
;

//""").cache.createOrReplaceTempView("M_BF_LOS_HEADER")

spark.sql(s"""
SELECT * FROM M_RESULT_NR_BF_SCEN_HEADER
""").take(1000).foreach(println)
;

spark.sql(s"""
SELECT * FROM M_BF_LOS_HEADER
""").take(1000).foreach(println)
;


spark.sql(s"""
SELECT DISTINCT TBD_KEY FROM RESULT_NR_BF_LOS WHERE SCHEDULE_ID = 8460965
""").take(1000).foreach(println)
;





}  


def test01(spark: SparkSession) = {

  
spark.sql(s"""
SELECT BUILDING_INDEX -- 0
     , TBD_KEY        -- 1
     , NX             -- 2
     , NY             -- 3
     , FLOORZ         -- 4
     , EXT_SX         -- 5
     , EXT_SY         -- 7
     , NX*NY*FLOORZ      AS BIN_CNT
     , SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ      AS START_POINT_BIN
     ,(SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ) * 4 AS START_POINT_4BIN
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = 8463235
ORDER BY BUILDING_INDEX
""").cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER")
;
  
  
spark.sql(s"""
SELECT A.TBD_KEY                                  AS TBD_KEY          -- 1               
     , A.RX_FLOORZ                                AS RX_FLOORZ        -- 2
     , A.RX_TM_YPOS                               AS RX_TM_YPOS       -- 3
     , A.RX_TM_XPOS                               AS RX_TM_XPOS       -- 4
     , CAST(LOS AS INTEGER)                  AS VALUE            -- 5
     , CAST(LOS AS FLOAT)                    AS VALUE2           -- 6
     , CAST(
       B.START_POINT_BIN                                                
     + (B.NX*B.NY*A.RX_FLOORZ)
     + (B.NX*A.RX_TM_YPOS)
     + (A.RX_TM_XPOS)
       AS INTEGER)                                AS POS              -- 7
     , B.START_POINT_BIN
     , B.NX
     , B.NY
FROM   RESULT_NR_BF_LOS A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = 8460965
AND    A.TBD_KEY = B.TBD_KEY
ORDER BY RX_FLOORZ, RX_TM_YPOS, RX_TM_XPOS  
""").cache.createOrReplaceTempView("M_BF_LOS_HEADER")
;
  

spark.sql(s"""
SELECT A.TBD_KEY
     , A.RX_FLOORZ
     , A.RX_TM_YPOS
     , A.RX_TM_XPOS
     , LOS AS VALUE
     , B.START_POINT_4BIN
     + (A.RX_FLOORZ*A.RX_TM_YPOS*A.RX_TM_XPOS*4)
     + (A.RX_TM_YPOS*A.RX_TM_XPOS*4)
     + (A.RX_TM_XPOS*4) AS POS
FROM   RESULT_NR_BF_LOS A
     , M_RESULT_NR_BF_SCEN_HEADER B
WHERE  A.SCHEDULE_ID = 8463235
AND    A.TBD_KEY = B.TBD_KEY
ORDER BY RX_FLOORZ, RX_TM_YPOS, RX_TM_XPOS
""").take(1000).foreach(println)
;


 
spark.sql(s"""
SELECT SCHEDULE_ID, COUNT(*) FROM RESULT_NR_BF_SCEN_HEADER GROUP BY SCHEDULE_ID 
""").take(1000).foreach(println);


}

def test212(spark: SparkSession) = {

spark.sql(s"""
SELECT BUILDING_INDEX
     , TBD_KEY
     , NX
     , NY
     , FLOORZ
     , EXT_SX
     , EXT_SY
     , NX*NY*FLOORZ      AS BIN_CNT
     , SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ AS START_POINT_BIN
FROM   RESULT_NR_BF_SCEN_HEADER
WHERE  SCHEDULE_ID = 8460965
ORDER BY BUILDING_INDEX
""").cache.createOrReplaceTempView("M_RESULT_NR_BF_SCEN_HEADER")
;



}  




def test03(spark: SparkSession) = {

  
  
spark.sql(s"""
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
 WHERE B.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242300
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT X_POINT,VALUE, COUNT(*)
FROM (
SELECT A.SCHEDULE_ID,
       B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       A.VALUE
  FROM RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242300
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
)
GROUP BY X_POINT,VALUE
ORDER BY X_POINT,VALUE
""").take(1000).foreach(println);


spark.sql(s"""
SELECT *
 FROM  RESULT_NR_2D_LOS_RU A
 WHERE A.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242308
""").take(100).foreach(println);




spark.sql(s"""
SELECT COUNT(*)
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.SCENARIO_ID = B.SCENARIO_ID
""").take(100).foreach(println);

spark.sql(s"""
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV A.RESOLUTION * A.RESOLUTION AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242300
   AND A.SCENARIO_ID = B.SCENARIO_ID
""").take(100).foreach(println);



spark.sql(s"""
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
 WHERE B.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242300
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT A.SCHEDULE_ID,
       B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       A.VALUE
  FROM RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.SCHEDULE_ID = 8460062
   AND A.RU_ID = 1012242300
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
""").take(1000).foreach(println);


spark.sql(s"""
SELECT * 
FROM   SCENARIO_NR_RU 
WHERE  SCENARIO_ID = (SELECT SCENARIO_ID FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
AND    RU_ID = 1012242300 
""").take(1000).foreach(println);

spark.sql(s"""
SELECT RX_TM_XPOS, RX_TM_YPOS, VALUE 
FROM   RESULT_NR_2D_LOS_RU 
WHERE  SCENARIO_ID = (SELECT SCENARIO_ID FROM I_SCHEDULE WHERE SCHEDULE_ID = 8460062)
AND    RU_ID = 1012242300
ORDER BY RX_TM_XPOS, RX_TM_YPOS 
""").take(1000).foreach(println);



spark.sql(s"""
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
		WHERE B.SCHEDULE_ID = 8460062
		AND A.SCENARIO_ID = B.SCENARIO_ID
		AND A.RU_ID = 1012242300
		)
		SELECT A.SCHEDULE_ID,
		B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
		((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
		((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
		A.VALUE
		FROM RESULT_NR_2D_LOS_RU A, RU B
		WHERE A.SCHEDULE_ID = 8460062
		AND A.SCHEDULE_ID = B.SCHEDULE_ID
		AND A.RU_ID = B.RU_ID
		AND A.RU_ID = 1012242300
		AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
		AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
		ORDER BY X_POINT, Y_POINT
		""").take(2000).foreach(println);
}  


  
}

