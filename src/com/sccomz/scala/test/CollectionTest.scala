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



object TestSparkSql01 {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("MakeParquet").getOrCreate()
    //this.samToParquet(spark)
    spark.stop();
  }

  def test01(spark: SparkSession) = {



  }
}

