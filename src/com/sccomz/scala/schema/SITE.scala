package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField

object SITE {
final val schema : StructType= StructType( Array( 
 StructField("SCENARIO_ID"           ,StringType)
,StructField("ENB_ID"                ,StringType)
,StructField("PCI"                   ,StringType)
,StructField("PCI_PORT"              ,StringType)
,StructField("SITE_NM"               ,StringType)
,StructField("XPOSITION"             ,StringType)
,StructField("YPOSITION"             ,StringType)
,StructField("HEIGHT"                ,StringType)
,StructField("BLT_HEIGHT"            ,StringType)
,StructField("TOWER_HEIGHT"          ,StringType)
,StructField("SITE_ADDR"             ,StringType)
,StructField("TYPE"                  ,StringType)
,StructField("CORRECTION_VALUE"      ,StringType)
,StructField("FEEDER_LOSS"           ,StringType)
,StructField("FADE_MARGIN"           ,StringType)
,StructField("STATUS"                ,StringType)
,StructField("MSC"                   ,StringType)
,StructField("BSC"                   ,StringType)
,StructField("NETWORKID"             ,StringType)
,StructField("USABLETRAFFICCH"       ,StringType)
,StructField("SYSTEMID"              ,StringType)
,StructField("STRYPOS"               ,StringType)
,StructField("STRXPOS"               ,StringType)
,StructField("ATTENUATION"           ,StringType)
,StructField("SITE_GUBUN"            ,StringType)
,StructField("RU_ID"                 ,StringType)
,StructField("RADIUS"                ,StringType)
,StructField("NOISEFLOOR"            ,StringType)
,StructField("FA_SEQ"                ,StringType)
,StructField("RU_TYPE"               ,StringType)
,StructField("REG_DT"                ,StringType)
,StructField("SISUL_CD"              ,StringType)
,StructField("TM_XPOSITION"          ,StringType)
,StructField("TM_YPOSITION"          ,StringType)
,StructField("RU_DIV_CD"             ,StringType)
))
}