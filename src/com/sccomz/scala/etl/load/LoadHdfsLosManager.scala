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


import com.sccomz.scala.comm.App
import com.sccomz.scala.schema.SCENARIO
import com.sccomz.java.comm.util.FileUtil

/*
import com.sccomz.scala.etl.load.LoadHdfsLosManager
LoadHdfsLosManager.samToParquetPartition("LOS_ENG_RESULT","8460062","1012242284")
spark.sql("SELECT * FROM parquet.`/teos/warehouse/LOS_ENG_RESULT`").take(100).foreach(println);

ALTER TABLE I_LOS_ENG_RESULT DROP IF EXISTS PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284)

sql("ALTER TABLE I_LOS_ENG_RESULT ADD PARTITION (SCHEDULE_ID=8460062,RU_ID=1012242284) LOCATION '/teos/warehouse/LOS_ENG_RESULT/SCHEDULE_ID=8460062/RU_ID='1012242284''");

LOAD DATA INPATH '/disk2/etl/LOS_ENG_RESULT_8460062_1012242284.dat' INTO TABLE I_LOS_ENG_RESULT PARTITION (SCHEDULE_ID=8460062, RU_ID='1012242284');

 * */
object LoadHdfsLosManager {
  
  val spark = SparkSession.builder().master("local[*]").appName("LoadHdfsLosManager").config("spark.sql.warehouse.dir","/teos/warehouse").enableHiveSupport().getOrCreate()

  def main(args: Array[String]): Unit = {
    samToParquetPartition("RESULT_NR_2D_LOS_RU","8460062","1012242284")
    
  }

  def executeRealPostToHdfs(scheduleId:String,ruId:String,bdYn:String) = {
    samToParquetPartition("RESULT_NR_2D_LOS_RU",scheduleId,ruId);
    //samToParquetPartition("RESULT_NR_2D_TREE_RU",scheduleId,ruId);
    if(bdYn=="Y") {
      samToParquetPartition("RESULT_NR_BF_LOS_RU",scheduleId,ruId);
      //samToParquetPartition("RESULT_NR_BF_TREE_RU",scheduleId,ruId);
    }
  }
  
  def samToParquetPartition(objNm:String,scheduleId:String,ruId:String) = {
    
    //--------------------------------------
        println("samToParquet 시작");
    //--------------------------------------
    var srcEntityPath = App.hdfsLinuxEtlPath;
/*
    var objNm = "RESULT_NR_2D_LOS_RU"
    var scheduleId = "8463234"
    var cd = "local"
    var schema = RESULT_NR_2D_LOS_RU.schema;
* * */

    //--------------------------------------
        println("입출력 변수 세팅");
    //--------------------------------------
    var source = srcEntityPath+"/"+objNm+"_"+scheduleId+"_"+ruId+".dat";
    var target = App.hdfsWarehousePath+"/"+objNm+"/SCHEDULE_ID="+scheduleId+"/RU_ID="+ruId;

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
    //schema = SCENARIO.schema;
    val conf = new Configuration()
    val fs = FileSystem.get(conf)

    //--------------------------------------
        println("target 파일 삭제");
    //--------------------------------------
    fs.delete(new Path(target),true)

    //--------------------------------------
        println("target 파일 생성");
    //--------------------------------------
    spark.read.format("csv").option("delimiter", "|").schema(schema).load(source).write.parquet(target)

    //--------------------------------------
        println("Hive partition 생성");
    //--------------------------------------
    var qry = "";
    //println(s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""");
    //println(s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId}) LOCATION '/teos/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}'""");
    import spark.implicits._
    import spark.sql
    qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId},RU_ID=${ruId})"""; println(qry); sql(qry)
    qry = s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId},RU_ID=${ruId}) LOCATION '/TEOS/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}/RU_ID=${ruId}'"""; println(qry); sql(qry)

    //sql(s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId})""")
    
    //--------------------------------------
        println("Source 파일 삭제");
    //--------------------------------------
    new File(App.linuxEtlPath+"/"+objNm+"_"+scheduleId+"_"+ruId+".dat").delete();

    //--------------------------------------
        println("samToParquet 종료");
    //--------------------------------------
    
  }
  
  def sparkClose() = { spark.close(); }  
        
  
}
