package com.nexweb.xtractor.dw.stat.spark.common

import java.util.Calendar
import java.text.SimpleDateFormat
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object App {

  /**********************************************************
   * 전역 변수 세팅
   **********************************************************/
  //val localBasicPath       = "file:////data/xtractor/entity"
  val localBasicPath       = "file:////home/xtractor/entity"

  val hdfsLogBasicPath     = "/user/xtractor/data/entity"
  val hdfsParquetBasicPath = "/user/xtractor/parquet/entity"

  val localPcBasicPath     = "file:////c:/Users/P136391/Documents/data"

  val spark = SparkSession.builder().appName("Batch").getOrCreate() //.config("spark.master","local")

}