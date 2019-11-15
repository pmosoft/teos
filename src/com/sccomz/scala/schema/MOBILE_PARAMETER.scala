package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object MOBILE_PARAMETER{
final val schema : StructType= StructType( Array(
 StructField("MOBILE_ID"      , IntegerType)
,StructField("TYPE"           , IntegerType)
,StructField("MOBILENAME"     , StringType)
,StructField("MAKER"          , StringType)
,StructField("MINPOWER"       , FloatType)
,StructField("MAXPOWER"       , FloatType)
,StructField("MOBILEGAIN"     , FloatType)
,StructField("NOISEFLOOR"     , FloatType)
,StructField("HEIGHT"         , FloatType)
,StructField("BODYLOSS"       , FloatType)
,StructField("BUILDINGLOSS"   , FloatType)
,StructField("CARLOSS"        , FloatType)
,StructField("FEEDERLOSS"     , FloatType)
,StructField("NOISEFIGURE"    , FloatType)
,StructField("DIVERSITYGAIN"  , FloatType)
,StructField("ANTENNAGAIN"    , FloatType)
,StructField("RX_LAYER"       , IntegerType)
,StructField("SCENARIO_ID"    , IntegerType)
))
}