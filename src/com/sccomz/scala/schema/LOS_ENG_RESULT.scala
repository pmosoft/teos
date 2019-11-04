package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.FloatType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.BooleanType

object LOS_ENG_RESULT {
final val schema : StructType= StructType( Array(
 StructField("BIN_ID"              ,StringType)
,StructField("BIN_X"               ,FloatType)
,StructField("BIN_Y"               ,FloatType)
,StructField("BIN_Z"               ,FloatType)
,StructField("BLD_ID"              ,StringType)
,StructField("LOS"                 ,BooleanType)
,StructField("IN_BLD"              ,BooleanType)
,StructField("THETA_DEG"           ,IntegerType)
,StructField("PHI_DEG"             ,IntegerType)
,StructField("SECTOR_X"            ,FloatType)
,StructField("SECTOR_Y"            ,FloatType)
,StructField("SECTOR_Z"            ,FloatType)
,StructField("SCHEDULE_ID"         ,IntegerType)
,StructField("RU_ID"               ,StringType)
))
}
