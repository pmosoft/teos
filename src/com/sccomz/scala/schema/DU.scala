package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.IntegerType

object DU {
final val schema : StructType= StructType( Array(
 StructField("SCENARIO_ID"            ,IntegerType)
,StructField("ENB_ID"                 ,StringType)
,StructField("E_NODEB_NM"             ,StringType)
,StructField("PCI_CNT"                ,IntegerType)
,StructField("STRMAKER"               ,StringType)
,StructField("REG_DT"                 ,StringType)
))
}