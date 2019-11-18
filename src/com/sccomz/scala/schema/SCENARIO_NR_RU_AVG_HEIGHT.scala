package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object SCENARIO_NR_RU_AVG_HEIGHT {
final val schema : StructType= StructType( Array(
 StructField("ENB_ID"            , IntegerType)
,StructField("PCI"               , IntegerType)
,StructField("PCI_PORT"          , IntegerType)
,StructField("RU_ID"             , StringType)
,StructField("TXTOTALHEIGHT"     , FloatType)
,StructField("AVGBUILDINGHEIGHT" , FloatType)
,StructField("SCENARIO_ID"       , IntegerType)
))
}