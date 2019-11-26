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

def test01(spark: SparkSession) = {

spark.sql(s"""
with AREA as
(
select a.scenario_id, b.schedule_id,
       a.tm_startx div a.resolution * a.resolution as tm_startx,
       a.tm_starty div a.resolution * a.resolution as tm_starty,
       a.tm_endx div a.resolution * a.resolution as tm_endx,
       a.tm_endy div a.resolution * a.resolution as tm_endy,
       a.resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = 8463189
   and a.scenario_id = b.scenario_id
)
select max(AREA.scenario_id) as scenario_id,
       RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,
       RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,
       (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,
       (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,       
       max(rsrp) as rsrp
  from AREA, RESULT_NR_2D_RSRP_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and RSLT.schedule_id = 8463189
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
""").take(1000).foreach(println);

}

def test02(spark: SparkSession) = {

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


spark.sql(s"""
SELECT BUILDING_INDEX
--     , TBD_KEY
     , NX
     , NY
     , FLOORZ
--     , EXT_SX
--     , EXT_SY
     , NX*NY*FLOORZ AS BINCNT
     , SUM(NX*NY*FLOORZ) OVER (ORDER BY BUILDING_INDEX) - NX*NY*FLOORZ AS STARTPOINTBIN
FROM   M_RESULT_NR_BF_SCEN_HEADER
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
 WHERE B.SCHEDULE_ID = 8463233
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT RU_ID                                --0
     , X_BIN_CNT                            --1
     , Y_BIN_CNT                            --2
     , CAST(X_POINT AS INTEGER) AS X_POINT  --3
     , CAST(Y_POINT AS INTEGER) AS Y_POINT  --4
     , VALUE                                --5
FROM
(
SELECT DISTINCT
       A.RU_ID, B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       VALUE
 FROM  RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID       = B.RU_ID
   AND A.RU_ID       = '1012220451'
   AND A.SCHEDULE_ID = 8463233
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
)
WHERE X_POINT < X_BIN_CNT
AND   Y_POINT < Y_BIN_CNT
""").take(100).foreach(println);


spark.sql(s"""
SELECT CAST(1 AS INTEGER) AS X_POINT 
""").take(100).foreach(println);


spark.sql(s"""
SELECT RU_ID FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8463233
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
 WHERE B.SCHEDULE_ID = 8463233
   AND A.SCENARIO_ID = B.SCENARIO_ID
""").take(100).foreach(println)
;


spark.sql(s"""
SELECT * FROM RESULT_NR_BF_SCEN_HEADER WHERE SCHEDULE_ID = 8460965
""").take(100).foreach(println);

spark.sql(s"""
SELECT COUNT(*) FROM RESULT_NR_BF_LOS_RU WHERE SCHEDULE_ID = 8463235
""").take(100).foreach(println);

spark.sql(s"""
SELECT DISTINCT RU_ID FROM RESULT_NR_BF_LOS_RU WHERE SCHEDULE_ID = 8463235
""").take(100).foreach(println);

spark.sql(s"""
SELECT COUNT(DISTINCT RU_ID) FROM RESULT_NR_BF_LOS_RU WHERE SCHEDULE_ID = 8463235
""").take(100).foreach(println);

// 5,156,492,021


//RESULT_NR_BF_LOS_RU_8463235_1012138268



val sqlDf = spark.sql(s"""
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
 WHERE B.SCHEDULE_ID = 8463233
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT RU_ID                                --0
     , X_BIN_CNT                            --1
     , Y_BIN_CNT                            --2
     , CAST(X_POINT AS INTEGER) AS X_POINT  --3
     , CAST(Y_POINT AS INTEGER) AS Y_POINT  --4
     , VALUE                                --5
     , RU_PATH                              --6  
FROM
(
SELECT DISTINCT
       A.RU_ID, B.X_BIN_CNT, B.Y_BIN_CNT,
       ((A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTX) DIV B.RESOLUTION AS X_POINT,
       ((A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) - SITE_STARTY) DIV B.RESOLUTION AS Y_POINT,
       VALUE AS VALUE,
       B.RU_PATH
 FROM  RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID       = B.RU_ID
   AND A.SCHEDULE_ID = 8463233
   AND (A.RX_TM_XPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV B.RESOLUTION * B.RESOLUTION) BETWEEN SITE_STARTY AND SITE_ENDY
   ORDER BY X_POINT, Y_POINT
)
WHERE X_POINT < X_BIN_CNT
AND   Y_POINT < Y_BIN_CNT
"""); sqlDf.cache.createOrReplaceTempView("RU_BIN"); sqlDf.count();

spark.sql("DROP TABLE IF EXISTS M_RU_BIN2");

spark.sql(s"""
SELECT RU_ID 
     , MAX(X_BIN_CNT) AS MAX_X_BIN_CNT  
     , MAX(Y_BIN_CNT) AS MAX_Y_BIN_CNT 
     , MIN(X_POINT)   AS MIN_X_POINT
     , MAX(X_POINT)   AS MAX_X_POINT
     , MIN(Y_POINT)   AS MIN_Y_POINT
     , MAX(Y_POINT)   AS MAX_Y_POINT 
FROM M_RU_BIN
GROUP BY RU_ID
""").cache.createOrReplaceTempView("M_RU_BIN2")

spark.sql(s"""
SELECT * FROM M_RU_BIN2
""").take(100).foreach(println);

spark.sql(s"""
SELECT * 
FROM   M_RU_BIN2
WHERE  MAX_X_POINT >= MAX_X_BIN_CNT 
""").take(100).foreach(println);

spark.sql(s"""
SELECT * 
FROM   M_RU_BIN2
WHERE  MAX_Y_POINT >= MAX_Y_BIN_CNT 
""").take(100).foreach(println);


spark.sql(s"""
SELECT COUNT(*) FROM   NRSECTORPARAMETER
WHERE  SCENARIO_ID  = 5113566
""").take(100).foreach(println);






spark.sql(s"""
INSERT INTO SCHEDULE PARTITION (SCHEDULE_ID=8460062) 
SELECT 
  TYPE_CD                  
, SCENARIO_ID              
, USER_ID                  
, PRIORITIZE               
, PROCESS_CD               
, PROCESS_MSG              
, SCENARIO_PATH            
, REG_DT                   
, MODIFY_DT                
, RETRY_CNT                
, SERVER_ID                
, BIN_X_CNT                
, BIN_Y_CNT                
, RU_CNT                   
, ANALYSIS_WEIGHT          
, PHONE_NO                 
, RESULT_TIME              
, TILT_PROCESS_TYPE        
, GEOMETRYQUERY_SCHEDULE_ID
, RESULT_BIT               
, INTERWORKING_INFO        
FROM SCHEDULE_B
""").take(100).foreach(println);



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

