package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object DU {
final val schema : StructType= StructType( Array(
 StructField("SCENARIO_ID"            ,StringType)
,StructField("ENB_ID"                 ,StringType)
,StructField("E_NODEB_NM"             ,StringType)
,StructField("PCI_CNT"                ,StringType)
,StructField("STRMAKER"               ,StringType)
,StructField("REG_DT"                 ,StringType)
))
}