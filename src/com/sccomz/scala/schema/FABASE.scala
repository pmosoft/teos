package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType 
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType 
import org.apache.spark.sql.types.FloatType  
import org.apache.spark.sql.types.IntegerType

object FABASE{
final val schema : StructType= StructType( Array(
 StructField("FA_SEQ"                , IntegerType)
,StructField("SYSTEMTYPE"            , IntegerType)
,StructField("CHNO"                  , IntegerType)
,StructField("UPLINKFREQ"            , IntegerType)
,StructField("DOWNLINKFREQ"          , IntegerType)
,StructField("ANALY_CHECK"           , IntegerType)
,StructField("PASSLOSS_MODEL"        , StringType )
,StructField("FREQ"                  , StringType )
,StructField("UPLINKFREQ_COMMENT"    , StringType )
,StructField("DOWNLINKFREQ_COMMENT"  , StringType )
,StructField("REG_DT"                , StringType )
,StructField("REG_ID"                , StringType )
))
}