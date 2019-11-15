package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object SCENARIO_NR_RU {
final val schema : StructType= StructType(Array(
 StructField("ENB_ID"                        ,StringType)
,StructField("PCI"                           ,StringType)
,StructField("PCI_PORT"                      ,StringType)
,StructField("RU_ID"                         ,StringType)
,StructField("MAKER"                         ,StringType)
,StructField("SECTOR_ORD"                    ,StringType)
,StructField("REPEATERATTENUATION"           ,StringType)
,StructField("REPEATERPWRRATIO"              ,StringType)
,StructField("RU_SEQ"                        ,StringType)
,StructField("RADIUS"                        ,IntegerType)
,StructField("FEEDER_LOSS"                   ,StringType)
,StructField("NOISEFLOOR"                    ,StringType)
,StructField("CORRECTION_VALUE"              ,StringType)
,StructField("FADE_MARGIN"                   ,StringType)
,StructField("XPOSITION"                     ,StringType)                                                                                                                        
,StructField("YPOSITION"                     ,StringType)                                                                                                                        
,StructField("HEIGHT"                        ,IntegerType)                                                                                                                        
,StructField("SITE_ADDR"                     ,StringType)                                                                                                                        
,StructField("TYPE"                          ,StringType)                                                                                                                        
,StructField("STATUS"                        ,StringType)                                                                                                                        
,StructField("SISUL_CD"                      ,StringType)                                                                                                                        
,StructField("MSC"                           ,StringType)                                                                                                                        
,StructField("BSC"                           ,StringType)                                                                                                                        
,StructField("NETWORKID"                     ,StringType)                                                                                                                        
,StructField("USABLETRAFFICCH"               ,StringType)                                                                                                                        
,StructField("SYSTEMID"                      ,StringType)                                                                                                                        
,StructField("RU_TYPE"                       ,StringType)                                                                                                                        
,StructField("FA_MODEL_ID"                   ,StringType)                                                                                                                        
,StructField("NETWORK_TYPE"                  ,StringType)                                                                                                                        
,StructField("RESOLUTION"                    ,IntegerType)                                                                                                                        
,StructField("FA_SEQ"                        ,StringType)                                                                                                                        
,StructField("SITE_STARTX"                   ,FloatType)                                                                                                                        
,StructField("SITE_STARTY"                   ,FloatType)                                                                                                                        
,StructField("SITE_ENDX"                     ,FloatType)                                                                                                                        
,StructField("SITE_ENDY"                     ,FloatType)                                                                                                                        
,StructField("X_BIN_CNT"                     ,IntegerType)                                                                                                                        
,StructField("Y_BIN_CNT"                     ,IntegerType)
,StructField("SCENARIO_ID"                   ,IntegerType)
))
}