package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object LOS_ENG_RESULT_DIS {
final val schema : StructType= StructType( Array(
 StructField("JOB_ID"                 ,StringType)
,StructField("SCENARIO_ID"            ,StringType)
,StructField("SCHEDULE_ID"            ,StringType)
,StructField("BIN_ID"                 ,StringType)
,StructField("BIN_X"                  ,StringType)
,StructField("BIN_Y"                  ,StringType)
,StructField("BIN_Z"                  ,StringType)
,StructField("BIN_SIZE"               ,StringType)
,StructField("LOS"                    ,StringType)
,StructField("THETA"                  ,StringType)
,StructField("PHI"                    ,StringType)
,StructField("SECTOR_ID"              ,StringType)
,StructField("SECTOR_X"               ,StringType)
,StructField("SECTOR_Y"               ,StringType)
,StructField("SECTOR_Z"               ,StringType)
))
}