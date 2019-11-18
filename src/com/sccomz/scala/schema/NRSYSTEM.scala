package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object NRSYSTEM{
final val schema : StructType= StructType( Array(
 StructField("SYSTEM_ID"                      , IntegerType)  
,StructField("FA_SEQ"                         , IntegerType)  
,StructField("BANDWIDTH_PER_CC"               , IntegerType)  
,StructField("NUMBER_OF_CC"                   , IntegerType)  
,StructField("NUMBER_OF_SC_PER_RB"            , IntegerType)  
,StructField("TOTALBANDWIDTH"                 , IntegerType)  
,StructField("SUBCARRIERSPACING"              , IntegerType)  
,StructField("RB_PER_CC"                      , FloatType)    
,StructField("RADIOFRAMELENGTH"               , FloatType)    
,StructField("SUBFRAMELENGTH"                 , FloatType)    
,StructField("NO_SLOTPERRADIOFRAME"           , IntegerType)  
,StructField("SLOTLENGTH"                     , FloatType)    
,StructField("NO_OFDMSYMBOLPERSUBFRAME"       , IntegerType)  
,StructField("FRAMECONFIGURATION"             , IntegerType)  
,StructField("DLDATARATIO"                    , FloatType)    
,StructField("ULDATARATIO"                    , FloatType)    
,StructField("DLBLER"                         , FloatType)    
,StructField("ULBLER"                         , FloatType)    
,StructField("DIVERSITYGAINRATIO"             , FloatType)    
,StructField("DLINTRACELL"                    , FloatType)    
,StructField("DLINTERCELL"                    , FloatType)    
,StructField("ULINTRACELL"                    , FloatType)    
,StructField("ULINTERCELL"                    , FloatType)    
,StructField("DLCOVERAGELIMITRSRP"            , FloatType)    
,StructField("INTERFERENCEMARGIN"             , FloatType)    
,StructField("NRGAINOVERLTE"                  , FloatType)    
,StructField("PENETRATIONLOSS"                , FloatType)    
,StructField("SLOT_CONFIGURATION"             , IntegerType)  
,StructField("DLCOVERAGELIMITRSRPLOS"         , FloatType)    
,StructField("DLOH"                           , FloatType)    
,StructField("ULOH"                           , FloatType)    
,StructField("DLSINROFFSET"                   , FloatType)    
,StructField("TECHTYPE"                       , IntegerType)  
,StructField("RAYTRACING_REFLECTION"          , IntegerType)  
,StructField("RAYTRACING_DIFFRACTION"         , IntegerType)  
,StructField("RAYTRACING_SCATTERING"          , FloatType)    
,StructField("RELATED_ANALYSIS_COVLIMITRSRP"  , FloatType)    
,StructField("ENV_ROADSIDE_TREE_YN"           , StringType )  
,StructField("DLCOVLIMITRSRP_YN"              , IntegerType)  
,StructField("ANT_CATEGORY"                   , StringType )  
,StructField("ANT_NM"                         , StringType )  
,StructField("SCENARIO_ID"                    , IntegerType)  
))
}