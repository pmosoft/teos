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
import com.sccomz.scala.etl.load.LoadHdfsManager
import com.sccomz.scala.job.spark.eng.Los



object SparkTest {

  def main(args: Array[String]): Unit = {
    test01();
    test02();
  }

  def test01() = {
    //val spark1 = SparkSession.builder().master("local[*]").appName("MakeParquet").getOrCreate()
    LoadHdfsManager.oracleToHdfs("8463189");
    //spark1.stop();
  }
  
  def test02() = {
    //val spark2 = SparkSession.builder().master("yarn").appName("MakeParquet").getOrCreate()
    Los.execute("8460062");
    //spark2.stop();
  }

  
  
}

