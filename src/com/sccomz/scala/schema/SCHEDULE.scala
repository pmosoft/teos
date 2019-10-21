package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.FloatType

object SCHEDULE {
final val schema : StructType= StructType( Array(
 StructField("TYPE_CD"                   , StringType )  
,StructField("SCENARIO_ID"               , IntegerType)
,StructField("USER_ID"                   , StringType )
,StructField("PRIORITIZE"                , StringType )
,StructField("PROCESS_CD"                , StringType )
,StructField("PROCESS_MSG"               , StringType )
,StructField("SCENARIO_PATH"             , StringType )
,StructField("REG_DT"                    , StringType )
,StructField("MODIFY_DT"                 , StringType )
,StructField("RETRY_CNT"                 , IntegerType)
,StructField("SERVER_ID"                 , StringType )
,StructField("BIN_X_CNT"                 , IntegerType)
,StructField("BIN_Y_CNT"                 , IntegerType)
,StructField("RU_CNT"                    , IntegerType)
,StructField("ANALYSIS_WEIGHT"           , IntegerType)
,StructField("PHONE_NO"                  , StringType )
,StructField("RESULT_TIME"               , FloatType  )
,StructField("TILT_PROCESS_TYPE"         , IntegerType)
,StructField("GEOMETRYQUERY_SCHEDULE_ID" , IntegerType)
,StructField("RESULT_BIT"                , StringType )
,StructField("INTERWORKING_INFO"         , StringType )
,StructField("SCHEDULE_ID"               , IntegerType)
))
}

