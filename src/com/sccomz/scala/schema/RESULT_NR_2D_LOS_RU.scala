package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.FloatType
import org.apache.spark.sql.types.IntegerType

object RESULT_NR_2D_LOS_RU {
final val schema : StructType= StructType( Array(
 StructField("RX_TM_XPOS"          ,IntegerType)
,StructField("RX_TM_YPOS"          ,IntegerType)
,StructField("RZ"                  ,FloatType)
,StructField("VALUE"               ,IntegerType)
,StructField("THETA"               ,FloatType)
,StructField("PHI"                 ,FloatType)
,StructField("IS_BLD"              ,StringType)
,StructField("SCHEDULE_ID"         ,IntegerType)
,StructField("RU_ID"               ,StringType)
))
}
