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
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTY,
       INT(A.SITE_ENDX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDX,
       INT(A.SITE_ENDY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_ENDY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT A.SCHEDULE_ID,
       B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
       (((A.RX_TM_XPOS DIV (B.RESOLUTION * B.RESOLUTION)) - SITE_STARTX) DIV B.RESOLUTION) AS X_POINT,
       (((A.RX_TM_YPOS DIV (B.RESOLUTION * B.RESOLUTION)) - SITE_STARTY) DIV B.RESOLUTION) AS Y_POINT,
       value
 FROM  RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = 8460062
   AND A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.RU_ID = 1012242300
   AND (A.RX_TM_XPOS DIV (B.RESOLUTION * B.RESOLUTION)) BETWEEN SITE_STARTX AND SITE_ENDX
   AND (A.RX_TM_YPOS DIV (B.RESOLUTION * B.RESOLUTION)) BETWEEN SITE_STARTY AND SITE_ENDY
 ORDER BY X_POINT, Y_POINT
""").take(100).foreach(println);

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
       case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los,
--       case when sum(case when upper(RSLT.is_bld) = 'T' THEN 1 else 0 end) > 0 then 1 else 0 end as los, -- PLB Check Only
       max(AREA.schedule_id) as schedule_id
  from AREA, RESULT_NR_2D_LOS_RU RSLT
 where RSLT.schedule_id = AREA.schedule_id
   and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx
   and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy
  group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
""").take(100).foreach(println);


}




  
}

