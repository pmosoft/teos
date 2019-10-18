package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object RU {
final val schema : StructType= StructType( Array(
 StructField("SCENARIO_ID"           ,StringType)
,StructField("ENB_ID"                ,StringType)
,StructField("PCI"                   ,StringType)
,StructField("PCI_PORT"              ,StringType)
,StructField("RU_ID"                 ,StringType)
,StructField("MAKER"                 ,StringType)
,StructField("SITE_TYPE"             ,StringType)
,StructField("PAIR_ENODEB"           ,StringType)
,StructField("REPEATERATTENUATION"   ,StringType)
,StructField("REPEATERPWRRATIO"      ,StringType)
,StructField("RU_NM"                 ,StringType)
,StructField("FA_SEQ"                ,StringType)
,StructField("SECTOR_ORD"            ,StringType)
,StructField("RU_SEQ"                ,StringType)
,StructField("RRH_SEQ"               ,StringType)
,StructField("REG_DT"                ,StringType)
,StructField("SWING_YN"              ,StringType)
,StructField("ANT_CHK_YN"            ,StringType)
,StructField("TILT_YN"               ,StringType)
,StructField("FA_SEQ_ORG"            ,StringType)
))
}