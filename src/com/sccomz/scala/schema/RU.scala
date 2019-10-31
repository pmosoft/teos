package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType

object RU {
final val schema : StructType= StructType( Array(
 StructField("SCENARIO_ID"           ,IntegerType)
,StructField("ENB_ID"                ,StringType)
,StructField("PCI"                   ,IntegerType)
,StructField("PCI_PORT"              ,IntegerType)
,StructField("RU_ID"                 ,StringType)
,StructField("MAKER"                 ,StringType)
,StructField("SITE_TYPE"             ,StringType)
,StructField("PAIR_ENODEB"           ,IntegerType)
,StructField("REPEATERATTENUATION"   ,IntegerType)
,StructField("REPEATERPWRRATIO"      ,IntegerType)
,StructField("RU_NM"                 ,StringType)
,StructField("FA_SEQ"                ,IntegerType)
,StructField("SECTOR_ORD"            ,IntegerType)
,StructField("RU_SEQ"                ,IntegerType)
,StructField("RRH_SEQ"               ,IntegerType)
,StructField("REG_DT"                ,StringType)
,StructField("SWING_YN"              ,IntegerType)
,StructField("ANT_CHK_YN"            ,IntegerType)
,StructField("TILT_YN"               ,IntegerType)
,StructField("FA_SEQ_ORG"            ,IntegerType)
))
}