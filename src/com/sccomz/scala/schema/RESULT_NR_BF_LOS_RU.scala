package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.FloatType
import org.apache.spark.sql.types.IntegerType

object RESULT_NR_BF_LOS_RU {
final val schema : StructType= StructType( Array(
 StructField("TBD_KEY"        ,StringType)
,StructField("RX_TM_XPOS"     ,FloatType)
,StructField("RX_TM_YPOS"     ,FloatType)
,StructField("RX_FLOORZ"      ,IntegerType)
,StructField("RX_GBH"         ,FloatType)
,StructField("THETA"          ,FloatType)
,StructField("PHI"            ,FloatType)
,StructField("VALUE"          ,IntegerType)
,StructField("SCHEDULE_ID"    ,IntegerType)
,StructField("RU_ID"          ,StringType)
))
}
