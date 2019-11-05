package com.sccomz.scala.job.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.sql.{ DataFrame, Row, SparkSession }
import com.sccomz.scala.load.LoadTable

/*
 * 설    명 :
 * 입    력 :
           SCHEDULE
           SCENARIO

 * 출    력 : Los
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute("8459967");

 */

object Los2 {

var spark: SparkSession = null
var objNm = "LOS"
var scheduleId = ""
//var scheduleId = "8459967"

def execute(scheduleId:String) = {
  //------------------------------------------------------
  println(objNm + " 시작");
  //------------------------------------------------------
  this.scheduleId = scheduleId;
  
  spark = SparkSession.builder().appName("Los").getOrCreate();
  loadTables(); excuteSql(); saveToParqeut();
}

def loadTables() = {
  LoadTable.lodTable(spark,"SCHEDULE",scheduleId,"*","",true)
  LoadTable.lodTable(spark,"SCENARIO",scheduleId,"*","",true)
}

def excuteSql() = {
var qry = ""
qry = s"""
SELECT *
FROM   M_SCENARIO
"""
spark.sql(s"""
INSERT INTO TABLE I_SCHEDULE PARTITION (SCHEDULE_ID=8459968)
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
FROM I_SCHEDULE WHERE SCHEDULE_ID=8459967
""").take(100).foreach(println);

spark.sql(s"""
SELECT *       
FROM I_SCHEDULE I
--WHERE SCHEDULE_ID=8460062
""").take(100).foreach(println);

spark.sql(s""" SELECT SUM(SCHEDULE_ID) FROM SCHEDULE """).take(100).foreach(println);

spark.sql(s""" SELECT distinct SCENARIO_ID FROM result_nr_2d_los """).take(100).foreach(println);

spark.sql(s""" SELECT distinct SCENARIO_ID FROM scenario_nr_ru """).take(100).foreach(println);

spark.sql(s""" SELECT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS WHERE scenario_id = 5104573  ORDER BY X_POINT, Y_POINT """).take(100).foreach(println);

spark.sql(s"""
WITH RU AS
(
SELECT B.SCHEDULE_ID, A.ENB_ID, A.PCI, A.PCI_PORT, A.RU_ID,
       A.X_BIN_CNT, A.Y_BIN_CNT,
       INT(A.SITE_STARTX) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTX,
       INT(A.SITE_STARTY) DIV (A.RESOLUTION * A.RESOLUTION) AS SITE_STARTY,
       A.RESOLUTION
  FROM SCENARIO_NR_RU A, SCHEDULE B
 WHERE B.SCHEDULE_ID = 8460062
   AND A.SCENARIO_ID = B.SCENARIO_ID
)
SELECT A.SCHEDULE_ID,
       B.ENB_ID, B.PCI, B.PCI_PORT, B.RU_ID,  B.X_BIN_CNT, B.Y_BIN_CNT,
       (((A.RX_TM_XPOS DIV (A.RESOLUTION * A.RESOLUTION)) - SITE_STARTX) DIV B.RESOLUTION) AS X_POINT,
       (((A.RX_TM_YPOS DIV (A.RESOLUTION * A.RESOLUTION)) - SITE_STARTY) DIV B.RESOLUTION) AS Y_POINT,
       VALUE
 FROM  RESULT_NR_2D_LOS_RU A, RU B
 WHERE A.SCHEDULE_ID = 8460062
   AND A.SCHEDULE_ID = B.SCHEDULE_ID
   AND A.RU_ID = B.RU_ID
   AND A.RU_ID = 1012242300
 ORDER BY X_POINT, Y_POINT
""").take(100).foreach(println);


spark.sql(s""" SELECT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS WHERE scenario_id = 5104573  ORDER BY X_POINT, Y_POINT """).take(100).foreach(println);

spark.sql(s""" SELECT DISTINCT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS WHERE scenario_id = 5108566 ORDER BY X_POINT, Y_POINT """).take(100).foreach(println);
//spark.sql(qry).take(100).foreach(println);

//--------------------------------------
println(qry);
//---------------------------------------
spark.sql(qry).take(100).foreach(println);
//val sqlDf = spark.sql(qry)
//sqlDf.cache.createOrReplaceTempView(objNm); sqlDf.count()





}

def saveToParqeut() {
  //MakeParquet.dfToParquet(objNm, true, statisDate)
}

}
