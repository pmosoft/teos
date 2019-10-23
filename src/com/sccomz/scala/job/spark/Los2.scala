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
--WHERE SCHEDULE_ID=8459968
""").take(100).foreach(println);

spark.sql(s""" SELECT SUM(SCHEDULE_ID) FROM SCHEDULE """).take(100).foreach(println);

spark.sql(s""" SELECT distinct SCENARIO_ID FROM result_nr_2d_los """).take(100).foreach(println);

spark.sql(s""" SELECT distinct SCENARIO_ID FROM scenario_nr_ru """).take(100).foreach(println);

spark.sql(s""" SELECT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS WHERE scenario_id = 5104573  ORDER BY X_POINT, Y_POINT """).take(100).foreach(println);

spark.sql(s"""
SELECT * 
FROM scenario_nr_ru 
where scenario_id = 5104573
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
