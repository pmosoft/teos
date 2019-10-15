package com.sccomz.scala.etl.load

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

import com.sccomz.scala.schedule.control.sql.ScheduleDaemonSql
import com.sccomz.scala.comm.App

/*
import com.sccomz.scala.schedule.etl.load.LoadManager
LoadManager.samToParquetPartition(spark,"SCHEDULE","8459967");

 * */
object LoadManager {

  val spark = SparkSession.builder().appName("MakeParquet").getOrCreate()

  def main(args: Array[String]): Unit = {
    //samToParquetPartition(spark,"SCHEDULE","8459967");
  }

  def samToParquetPartition(spark: SparkSession,objNm:String,scheduleId:String) = {
    toParquetPartition(spark,"local",objNm,scheduleId);
  }

  def hdfsToParquetPartition(spark: SparkSession,objNm:String,scheduleId:String) = {
    toParquetPartition(spark,"hdfs",objNm,scheduleId);
  }


  def toParquetPartition(spark: SparkSession,cd:String,objNm:String,scheduleId:String) = {

    //--------------------------------------
        println("samToParquet 시작");
    //--------------------------------------

    var srcEntityPath = if     (cd=="local")   App.hdfsLinuxEtlPath
                        else if(cd=="hdfs")    App.hdfsEtlPath;
    var isPartion = if(scheduleId=="all") false else true;

/*
    var objNm = "SCHEDULE"
    var scheduleId = "8443705"
    var srcEntityPath = "file:////home/teos/entity"
    var isPartion = true
    val hdfsParquetEntityPath = "/user/teos/parquet/entity"
* */

    //--------------------------------------
        println("입출력 변수 세팅");
    //--------------------------------------
    var source = if(isPartion) srcEntityPath+"/"+objNm+"_"+scheduleId+".dat" else srcEntityPath+"/"+objNm+".dat"
    var target = if(isPartion) App.hdfsParquetEntityPath+"/"+objNm+"/SCHEDULE_ID="+scheduleId else App.hdfsParquetEntityPath+"/"+objNm+"/"+objNm

    //--------------------------------------
        println("스키마 세팅");
    //--------------------------------------
    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
    val module = runtimeMirror.staticModule(s"com.sccomz.scala.schema.${objNm}")
    val im = runtimeMirror.reflectModule(module)
    val method = im.symbol.info.decl(universe.TermName("schema")).asMethod
    val objMirror = runtimeMirror.reflect(im.instance)
    val schema = objMirror.reflectMethod(method)().asInstanceOf[StructType]

    println("source ="+source);
    println("target ="+target);

    //--------------------------------------
        println("HDFS 세션 생성");
    //--------------------------------------
    val conf = new Configuration()
    //conf.addResource(new Path("file:////etc/hadoop/conf/core-site.xml"))
    //conf.addResource(new Path("file:////etc/hadoop/conf/hdfs-site.xml"))
    val fs = FileSystem.get(conf)

    //--------------------------------------
        println("target 파일 삭제");
    //--------------------------------------
    fs.delete(new Path(target),true)

    //--------------------------------------
        println("target 파일 생성");
    //--------------------------------------
    spark.read.format("csv").option("delimiter", "|").schema(schema).load(source).write.parquet(target)

    //spark.read
    //.format("csv")               //파일포맷
    //.option("delimiter", "|")    //구분자
    ////.option("nullValue", "")     //null
    //.schema(schema)              //파일스키마
    //.load(source)                //읽을 파일
    //.write
    //.parquet(target)             //parquet

    //--------------------------------------
        println("samToParquet 종료");
    //--------------------------------------
  }

}