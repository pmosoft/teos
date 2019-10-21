package com.sccomz.scala.job.spark

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.sql.{ DataFrame, Row, SparkSession }

/*
 * 설    명 :
 * 입    력 :
           SCHEDULE
           SCENARIO

 * 출    력 : Los
 * 수정내역 :
 * 2019-02-09 | 피승현 | 최초작성

import com.sccomz.scala.job.spark.Los
Los.execute();

 */

object Los {

var spark: SparkSession = null
var objNm = "Los"

//var objNm = "TB_CATE_PATH_STAT";var statisDate = "20190311"; var statisType = "D"

def execute() = {
  //------------------------------------------------------
  println(objNm + ".executeDaily() 일배치 시작");
  //------------------------------------------------------
  spark = SparkSession.builder().appName("Los").getOrCreate();
  var scheduleId = ""
  loadTables(); excuteSql(); saveToParqeut();
}

def loadTables() = {
  //LoadTable.lodAllColTable(spark,"TB_NCATE_URL_MAP_FRONT"     ,statisDate,statisType,"",true)
  //LoadTable.lodAllColTable(spark,"TB_ACCESS_SESSION2"       ,statisDate,statisType,"",true)
}

def excuteSql() = {
var qry = ""
qry =
s"""
SELECT *
FROM   M_SCENARIO"""
//spark.sql(qry).take(100).foreach(println);

//--------------------------------------
println(qry);
//--------------------------------------
val sqlDf = spark.sql(qry)
//sqlDf.cache.createOrReplaceTempView(objNm); sqlDf.count()

}

def saveToParqeut() {
  //MakeParquet.dfToParquet(objNm, true, statisDate)
}

}
