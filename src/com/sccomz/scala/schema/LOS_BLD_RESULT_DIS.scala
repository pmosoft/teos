package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object LOS_BLD_RESULT_DIS {
final val schema : StructType= StructType( Array(
 StructField("JOB_ID"                 ,StringType)
,StructField("SCENARIO_ID"            ,StringType)
,StructField("SECTOR_ID"              ,StringType)
,StructField("TBD"                    ,StringType)
,StructField("FLOOR_X"                ,StringType)
,StructField("FLOOR_Y"                ,StringType)
,StructField("FLOOR_Z"                ,StringType)
,StructField("LOS"                    ,StringType)
))
}