package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType

object SITE {
final val schema : StructType= StructType( Array( 
 StructField("SCENARIO_ID"           ,IntegerType)
,StructField("ENB_ID"                ,StringType)
,StructField("PCI"                   ,IntegerType)
,StructField("PCI_PORT"              ,IntegerType)
,StructField("SITE_NM"               ,StringType)
,StructField("XPOSITION"             ,StringType)
,StructField("YPOSITION"             ,StringType)
,StructField("HEIGHT"                ,IntegerType)
,StructField("BLT_HEIGHT"            ,IntegerType)
,StructField("TOWER_HEIGHT"          ,IntegerType)
,StructField("SITE_ADDR"             ,StringType)
,StructField("TYPE"                  ,StringType)
,StructField("CORRECTION_VALUE"      ,IntegerType)
,StructField("FEEDER_LOSS"           ,IntegerType)
,StructField("FADE_MARGIN"           ,IntegerType)
,StructField("STATUS"                ,StringType)
,StructField("MSC"                   ,IntegerType)
,StructField("BSC"                   ,IntegerType)
,StructField("NETWORKID"             ,IntegerType)
,StructField("USABLETRAFFICCH"       ,IntegerType)
,StructField("SYSTEMID"              ,IntegerType)
,StructField("STRYPOS"               ,StringType)
,StructField("STRXPOS"               ,StringType)
,StructField("ATTENUATION"           ,IntegerType)
,StructField("SITE_GUBUN"            ,StringType)
,StructField("RU_ID"                 ,StringType)
,StructField("RADIUS"                ,IntegerType)
,StructField("NOISEFLOOR"            ,IntegerType)
,StructField("FA_SEQ"                ,StringType)
,StructField("RU_TYPE"               ,IntegerType)
,StructField("REG_DT"                ,StringType)
,StructField("SISUL_CD"              ,StringType)
,StructField("TM_XPOSITION"          ,StringType)
,StructField("TM_YPOSITION"          ,StringType)
,StructField("RU_DIV_CD"             ,IntegerType)
))
}