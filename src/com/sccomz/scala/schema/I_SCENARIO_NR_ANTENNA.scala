package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType

object I_SCENARIO_NR_ANTENNA {  
final val schema: StructType = StructType(Array(
 StructField("SCENARIO_ID"                   ,IntegerType)
,StructField("ANTENA_SEQ"                    ,IntegerType)
,StructField("RU_ID"                         ,StringType)
,StructField("ANTENA_NM"                     ,StringType)
,StructField("ORIENTATION"                   ,IntegerType)
,StructField("TILTING"                       ,IntegerType)
,StructField("ANTENA_ORD"                    ,IntegerType)
,StructField("LIMIT_TILTING"                 ,IntegerType)
,StructField("RU_SEQ"                        ,IntegerType)
))
}