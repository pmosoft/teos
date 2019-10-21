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

object Los {

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
//spark.sql(qry).take(100).foreach(println);

//--------------------------------------
println(qry);
//--------------------------------------
spark.sql(qry).take(100).foreach(println);
//val sqlDf = spark.sql(qry)
//sqlDf.cache.createOrReplaceTempView(objNm); sqlDf.count()

}

def saveToParqeut() {
  //MakeParquet.dfToParquet(objNm, true, statisDate)
}

}
