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

/*
import com.sccomz.scala.etl.load.LoadHdfsManager
LoadHdfsManager.oracleToHdfsBatch("20191107");


LoadHdfsManager.oracleToHdfs("8460178");
LoadHdfsManager.oracleToHdfs("8460179");
LoadHdfsManager.oracleToHdfs("8460062");
LoadHdfsManager.oracleToHdfs("8460063");
LoadHdfsManager.oracleToHdfs("8463189");

 * */
object LoadHdfsManager {

  def main(args: Array[String]): Unit = {
    println("LoadHdfsManager start");
    //toParquetPartition(spark,"local","SCHEDULE","8463189");
    //toParquetPartition(spark,"local","DU","8463189");
    //toParquetPartition(spark,"local","RU","8463189");
    //toParquetPartition(spark,"local","SITE","8463189");
    8460064
    oracleToHdfs("8460064");
    //oracleToHdfs("8463189");
    
    println("LoadHdfsManager end");
  }

  def oracleToHdfs(scheduleId:String) = {
    val spark = SparkSession.builder().master("local[*]").appName("LoadHdfsManager").config("spark.sql.warehouse.dir","/teos/warehouse").enableHiveSupport().getOrCreate()
    toParquetPartition(spark,"local","SCHEDULE",scheduleId);
    toParquetPartition(spark,"local","SCENARIO",scheduleId);
    //toParquetPartition(spark,"local","DU",scheduleId);
    //toParquetPartition(spark,"local","RU",scheduleId);
    //toParquetPartition(spark,"local","SITE",scheduleId);
    toParquetPartition(spark,"local","SCENARIO_NR_RU",scheduleId);
    toParquetPartition(spark,"local","SCENARIO_NR_ANTENNA",scheduleId);
    spark.close();
  }

  def oracleToHdfsBatch(workDt:String) = {
    val spark = SparkSession.builder().master("local[*]").appName("LoadHdfsManager").config("spark.sql.warehouse.dir","/teos/warehouse").enableHiveSupport().getOrCreate()
    toParquetPartitionBatch(spark,"local","FABASE",workDt);
    spark.close();    
  }
  
  def toParquetPartition(spark: SparkSession,cd:String,objNm:String,scheduleId:String) = {
    //--------------------------------------
        println("samToParquet 시작");
    //--------------------------------------
    var srcEntityPath = if (cd=="local") App.hdfsLinuxEtlPath else if(cd=="hdfs") App.hdfsEtlPath;
    var isPartion = if(scheduleId=="all") false else true;
/*
    var objNm = "SCENARIO"
    var scheduleId = "8459967"
    var cd = "local"
    var schema = SCENARIO.schema;
* * */

    //--------------------------------------
        println("입출력 변수 세팅");
    //--------------------------------------
    var source = if(isPartion) srcEntityPath+"/"+objNm+"_"+scheduleId+".dat" else srcEntityPath+"/"+objNm+".dat"
    var target = if(isPartion) App.hdfsWarehousePath+"/"+objNm+"/SCHEDULE_ID="+scheduleId else App.hdfsWarehousePath+"/"+objNm+"/"+objNm

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
    //println(s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""");
    //println(s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId}) LOCATION '/teos/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}'""");
    import spark.implicits._
    import spark.sql
    sql(s"""ALTER TABLE I_${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""")
    sql(s"""ALTER TABLE I_${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId}) LOCATION '/teos/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}'""");

    //sql(s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId})""")

    //--------------------------------------
        println("samToParquet 종료");
    //--------------------------------------
  }

  def toParquetPartitionBatch(spark: SparkSession,cd:String,objNm:String,workDt:String) = {
    //--------------------------------------
        println("samToParquet 시작");
    //--------------------------------------
    var srcEntityPath = if (cd=="local") App.hdfsLinuxEtlPath else if(cd=="hdfs") App.hdfsEtlPath;
    var isPartion = false;
/*
    var objNm = "SCENARIO"
    var scheduleId = "8459967"
    var cd = "local"
    var schema = SCENARIO.schema;
* * */

    //--------------------------------------
        println("입출력 변수 세팅");
    //--------------------------------------
    var source = srcEntityPath+"/"+objNm+"_"+workDt+".dat";
    var target = App.hdfsWarehousePath+"/"+objNm;

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

    //
    
    if(isPartion) {
      //--------------------------------------
          println("Hive partition 생성");
      //--------------------------------------
      import spark.implicits._
      import spark.sql
      sql(s"""ALTER TABLE I_${objNm} DROP IF EXISTS PARTITION (WORK_DT=${workDt})""")
      sql(s"""ALTER TABLE I_${objNm} ADD PARTITION (WORK_DT=${workDt}) LOCATION '/teos/warehouse/${objNm}/WORK_DT=${workDt}'""");
    }

    //--------------------------------------
        println("samToParquet 종료");
    //--------------------------------------
  }
  
  
  def impalaDropPartition(objNm:String,scheduleId:String,cdNm:String) = {
    Class.forName(App.dbDriverImpala);
    var con = DriverManager.getConnection(App.dbUrlImpala,App.dbUserImpala,App.dbPwImpala);
    var stat:Statement=con.createStatement();
    var qry=s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""";
    println(qry);stat.execute(qry);
  }

  def impalaAddPartition(objNm:String,scheduleId:String,cdNm:String) = {
    Class.forName(App.dbDriverImpala);
    var con = DriverManager.getConnection(App.dbUrlImpala,App.dbUserImpala,App.dbPwImpala);
    var stat:Statement=con.createStatement();
    var qry=s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId}) LOCATION '/teos/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}'""";
    println(qry);stat.execute(qry);

  }

  def hiveDropPartition(objNm:String,scheduleId:String,cdNm:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (SCHEDULE_ID=${scheduleId})""";
    println(qry);stat.execute(qry);
  }

  def hiveAddPartition(objNm:String,scheduleId:String,cdNm:String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive,App.dbUserHive,App.dbPwHive);
    var stat:Statement=con.createStatement();
    var qry=s"""ALTER TABLE ${objNm} ADD PARTITION (SCHEDULE_ID=${scheduleId}) LOCATION '/teos/warehouse/${objNm}/SCHEDULE_ID=${scheduleId}'""";
    println(qry);stat.execute(qry);

  }

}
