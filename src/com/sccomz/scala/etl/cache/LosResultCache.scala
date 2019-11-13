package com.sccomz.scala.etl.cache

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.net.InetAddress

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import scala.reflect.runtime.universe

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType


import com.sccomz.scala.comm.App
import com.sccomz.scala.schema.SCENARIO

/*
import com.sccomz.scala.etl.load.LoadHdfsManager
LoadHdfsManager.oracleToHdfsBatch("20191107");


LoadHdfsManager.oracleToHdfs("8460178");
LoadHdfsManager.oracleToHdfs("8460179");
LoadHdfsManager.oracleToHdfs("8460062");
LoadHdfsManager.oracleToHdfs("8460063");
LoadHdfsManager.oracleToHdfs("8463189");

 * */
object LosResultCache {

  def main(args: Array[String]): Unit = {  
    var scheduleId = if (args.length < 1) "" else args(0);
    execute(scheduleId);
  }   
  
  def execute(scheduleId:String) = {
    val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/teos/warehouse").enableHiveSupport().getOrCreate();
    executeSql(spark, scheduleId);
    spark.close();
  }
  

def executeSql(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8460062";
  
var objNm = "RESULT_NR_2D_LOS"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = "";
 
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/teos/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
import spark.implicits._
import spark.sql
qry = s"""ALTER TABLE I_${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; sql(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------

qry = s"""
           (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

}
