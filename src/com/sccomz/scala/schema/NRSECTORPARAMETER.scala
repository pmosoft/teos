package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object NRSECTORPARAMETER{
final val schema : StructType= StructType( Array(
 StructField("ENB_ID"                     , IntegerType)  
,StructField("PCI"                        , IntegerType)  
,StructField("PCI_PORT"                   , IntegerType)  
,StructField("RU_ID"                      , StringType )  
,StructField("TXPWRWATT"                  , FloatType)    
,StructField("TXPWRDBM"                   , FloatType)    
,StructField("TXEIRPWATT"                 , FloatType)    
,StructField("TXEIRPDBM"                  , FloatType)    
,StructField("RETXEIRPWATT"               , FloatType)    
,StructField("RETXEIRPDBM"                , FloatType)    
,StructField("TRXCOUNT"                   , IntegerType)  
,StructField("DLMODULATION"               , IntegerType)  
,StructField("DLMIMOTYPE"                 , IntegerType)  
,StructField("DLMIMOGAIN"                 , FloatType)    
,StructField("ULMODULATION"               , IntegerType)  
,StructField("ULMIMOTYPE"                 , IntegerType)  
,StructField("ULMIMOGAIN"                 , FloatType)    
,StructField("LOSBEAMFORMINGLOSS"         , FloatType)    
,StructField("NLOSBEAMFORMINGLOSS"        , FloatType)    
,StructField("HANDOVERCALLDROPTHRESHOLD"  , IntegerType)  
,StructField("POWERCOMBININGGAIN"         , FloatType)    
,StructField("BEAMMISMATCHMARGIN"         , FloatType)    
,StructField("ANTENNAGAIN"                , FloatType)    
,StructField("FOLIAGELOSS"                , FloatType)    
,StructField("TXLAYER"                    , IntegerType)  
,StructField("RXLAYER"                    , IntegerType)  
,StructField("SCENARIO_ID"                , IntegerType)
))
}
