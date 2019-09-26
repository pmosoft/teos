package com.sccomz.scala.parquet

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.types.StructType
import scala.reflect.runtime.universe
import org.apache.hadoop.fs.permission.FsAction
import org.apache.hadoop.fs.permission.FsPermission
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

import com.sccomz.scala.schema._


/*
 * 로컬(Linux) 혹은 HDFS의 샘파일을 HDFS의 parquet으로 저장
 * 사용방법
   MakeParquet local objNm scheduleId : 스케줄별파티션 로컬샘파일     to parquet
   MakeParquet local objNm all        : 전체           로컬샘파일     to parquet
   MakeParquet hdfs  objNm scheduleId : 스케줄별파티션 HDFS로그파일 to parquet
   MakeParquet hdfs  objNm all        : 전체           HDFS로그파일 to parquet
 * */
object MakeParquet {

  val spark  = SparkSession.builder().appName("MakeParquet").getOrCreate()

  def main(args: Array[String]): Unit = {

    //--------------------------------------
        println("입력파라미터 처리");
    //--------------------------------------
    if (args.length < 3) {
      System.err.println("Usage: MakeParquet local objNm scheduleId")
      System.err.println("Usage: MakeParquet local objNm all   ")
      System.err.println("Usage: MakeParquet hdfs  objNm scheduleId")
      System.err.println("Usage: MakeParquet hdfs  objNm all   ")
      System.exit(1)
    }

    val arg1 = args(0)
    val arg2 = args(1)
    val arg3 = args(2)

    this.samToParquet(arg1,arg2,arg3)

  }

  def samToParquet(arg1:String,arg2:String,arg3:String) = {

    val localEntityPath       = "file:////home/teos/entity"
    val localPcEntityPath     = "file:////c:/pony/excel"
    val hdfsSamEntityPath     = "/user/teos/parquet/entity"
    val hdfsParquetEntityPath = "/user/teos/parquet/entity"
    
    var srcEntityPath = if     (arg1=="local")   localEntityPath
                        else if(arg1=="localPc") localPcEntityPath
                        else if(arg1=="hdfs")    hdfsSamEntityPath
    var objNm = arg2
    var scheduleId = arg3
    var isPartion = if(arg3=="all") false else true

    //--------------------------------------
        println("입출력 변수 세팅");
    //--------------------------------------
    var objPartNm = if(isPartion) objNm + "_" + scheduleId else ""
    var source = if(isPartion) srcEntityPath+"/"+objNm+"/"+objPartNm+".dat"
                 else          srcEntityPath+"/"+objNm+"/"+objNm+".dat"
    var target = if(isPartion) hdfsParquetEntityPath +"/" + objNm +"/"+ objPartNm
                 else          hdfsParquetEntityPath +"/" + objNm

    //--------------------------------------
        println("스키마 세팅");
    //--------------------------------------
    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
    val module = runtimeMirror.staticModule(s"com.nexweb.teos.dw.stat.spark.schema.${objNm}")
    val im = runtimeMirror.reflectModule(module)
    val method = im.symbol.info.decl(universe.TermName("schema")).asMethod
    val objMirror = runtimeMirror.reflect(im.instance)
    val schema = objMirror.reflectMethod(method)().asInstanceOf[StructType]

    println("source ="+source);
    println("target ="+target);

    //--------------------------------------
        println("SparkSession 생성");
    //--------------------------------------
    val spark = SparkSession
    .builder()
    .appName("MakeParquet")
    //.config("spark.master","local")
    .getOrCreate()

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
    spark.read
    .format("csv")               //파일포맷
    .option("delimiter", "|")    //구분자
    //.option("nullValue", "")     //null
    .schema(schema)              //파일스키마
    .load(source)                //읽을 파일
    .write
    .parquet(target)             //parquet

  }

//  def dfToParquet(objNm:String,isPartion:Boolean,scheduleId:String) = {
//    //--------------------------------------
//        println("HDFS 세션 생성");
//    //--------------------------------------
//    val conf = new Configuration()
//    val fs = FileSystem.get(conf)
//
//    //--------------------------------------
//        println("입출력 변수 세팅");
//    //--------------------------------------
//    var objPartNm = if(isPartion) objNm + "_" + scheduleId else ""
//    var target = if(isPartion) Batch.hdfsParquetEntityPath +"/" + objNm +"/"+ objPartNm
//                 else          Batch.hdfsParquetEntityPath +"/" + objNm +"/"+ objNm
//
//    fs.mkdirs(new Path(Batch.hdfsParquetEntityPath+"/"+objNm),new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL))
//    fs.delete(new Path(target),true)
//
//    val sqlDf = this.spark.sql("select * from " + objNm);
//    sqlDf.write.format("parquet").save(target)
//  }
//
//  def dfToParquet(objNm:String,isPartion:Boolean,scheduleId:String,qry:String) = {
//    //--------------------------------------
//        println("HDFS 세션 생성");
//    //--------------------------------------
//    val conf = new Configuration()
//    val fs = FileSystem.get(conf)
//
//    //--------------------------------------
//        println("입출력 변수 세팅");
//    //--------------------------------------
//    var objPartNm = if(isPartion) objNm + "_" + scheduleId else ""
//    var target = if(isPartion) Batch.hdfsParquetEntityPath +"/" + objNm +"/"+ objPartNm
//                 else          Batch.hdfsParquetEntityPath +"/" + objNm +"/"+ objNm
//
//    fs.mkdirs(new Path(Batch.hdfsParquetEntityPath+"/"+objNm),new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL))
//    fs.delete(new Path(target),true)
//
//    val sqlDf = this.spark.sql(qry);
//    sqlDf.write.format("parquet").save(target)
//  }
//
//  def samToHdfs(arg1:String,arg2:String,arg3:String) = {
//
//    // arg1 = "local"
//    // arg2 = "TB_WL_URL_ACCESS"
//    // arg3 = "20181219"
//    // local TB_WL_URL_ACCESS 20181219
//
//    var srcEntityPath = if     (arg1=="local")   Batch.localEntityPath
//                       else if(arg1=="localPc") Batch.localPcEntityPath
//    var objNm = arg2
//    var scheduleId = arg3
//    var isPartion = if(arg3=="all") false else true
//
//    //--------------------------------------
//        println("입출력 변수 세팅");
//    //--------------------------------------
//    //  val localEntityPath       = "file:////data/teos/entity"
//    //  val hdfsSamEntityPath     = "/user/teos/log/entity"
//    //  val hdfsParquetEntityPath = "/user/teos/parquet/entity"
//    var objPartNm = if(isPartion) objNm + "_" + scheduleId else ""
//    var source = if(isPartion) srcEntityPath+"/"+objNm+"/"+objPartNm+".dat"
//                 else          srcEntityPath+"/"+objNm+"/"+objNm+".dat"
//    var target = if(isPartion) Batch.hdfsParquetEntityPath +"/" + objNm +"/"+ objPartNm
//                 else          Batch.hdfsParquetEntityPath +"/" + objNm
//
//    val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
//    val module = runtimeMirror.staticModule(s"com.nexweb.teos.dw.stat.spark.schema.${objNm}")
//    val im = runtimeMirror.reflectModule(module)
//    val method = im.symbol.info.decl(universe.TermName("schema")).asMethod
//    val objMirror = runtimeMirror.reflect(im.instance)
//    val schema = objMirror.reflectMethod(method)().asInstanceOf[StructType]
//    //println("obj ="+obj.instance);
//    //val s001 = module.asInstanceOf[{def schema() : StructType}]
//    //println("schema ="+schema);
//    //println("schema ="+schema.isInstanceOf[org.apache.spark.sql.types.StructType]);
//    //val schema2 : StructType = schema
//    //println("schema ="+schema.schema());
//    println("source ="+source);
//    println("target ="+target);
//
//    //--------------------------------------
//        println("SparkSession 생성");
//    //--------------------------------------
//    //val spark = SparkSession.builder().getOrCreate()
//    val spark = SparkSession
//    .builder()
//    .appName("MakeParquet")
//    //.config("spark.master","local")
//    .getOrCreate()
//
//    //--------------------------------------
//        println("target 파일 생성");
//    //--------------------------------------
//    spark.read
//    .format("csv")               //파일포맷
//    .option("delimiter", "|")    //구분자
//    .schema(schema)              //파일스키마
//    .load(source)                //읽을 파일
//    .write
//    .parquet(target)             //parquet
//
//  }


}