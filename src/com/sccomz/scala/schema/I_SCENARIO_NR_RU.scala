package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object I_SCENARIO_NR_RU {
final val schema : StructType= StructType(Array(
 StructField("SCENARIO_ID"                   ,IntegerType)
,StructField("ENB_ID"                        ,StringType)
,StructField("PCI"                           ,IntegerType)
,StructField("PCI_PORT"                      ,IntegerType)
,StructField("RU_ID"                         ,StringType)
,StructField("MAKER"                         ,StringType)
,StructField("SECTOR_ORD"                    ,IntegerType)
,StructField("REPEATERATTENUATION"           ,IntegerType)
,StructField("REPEATERPWRRATIO"              ,IntegerType)
,StructField("RU_SEQ"                        ,IntegerType)
,StructField("RADIUS"                        ,IntegerType)
,StructField("FEEDER_LOSS"                   ,IntegerType)
,StructField("NOISEFLOOR"                    ,IntegerType)
,StructField("CORRECTION_VALUE"              ,IntegerType)
,StructField("FADE_MARGIN"                   ,IntegerType)
,StructField("XPOSITION"                     ,StringType)                                                                                                                        
,StructField("YPOSITION"                     ,StringType)                                                                                                                        
,StructField("HEIGHT"                        ,IntegerType)                                                                                                                        
,StructField("SITE_ADDR"                     ,StringType)                                                                                                                        
,StructField("TYPE"                          ,StringType)                                                                                                                        
,StructField("STATUS"                        ,StringType)                                                                                                                        
,StructField("SISUL_CD"                      ,StringType)                                                                                                                        
,StructField("MSC"                           ,IntegerType)                                                                                                                        
,StructField("BSC"                           ,IntegerType)                                                                                                                        
,StructField("NETWORKID"                     ,IntegerType)                                                                                                                        
,StructField("USABLETRAFFICCH"               ,IntegerType)                                                                                                                        
,StructField("SYSTEMID"                      ,IntegerType)                                                                                                                        
,StructField("RU_TYPE"                       ,IntegerType)                                                                                                                        
,StructField("FA_MODEL_ID"                   ,IntegerType)                                                                                                                        
,StructField("NETWORK_TYPE"                  ,IntegerType)                                                                                                                        
,StructField("RESOLUTION"                    ,IntegerType)                                                                                                                        
,StructField("FA_SEQ"                        ,IntegerType)                                                                                                                        
,StructField("SITE_STARTX"                   ,FloatType)                                                                                                                        
,StructField("SITE_STARTY"                   ,FloatType)                                                                                                                        
,StructField("SITE_ENDX"                     ,FloatType)                                                                                                                        
,StructField("SITE_ENDY"                     ,FloatType)                                                                                                                        
,StructField("X_BIN_CNT"                     ,IntegerType)                                                                                                                        
,StructField("Y_BIN_CNT"                     ,IntegerType)
))
}