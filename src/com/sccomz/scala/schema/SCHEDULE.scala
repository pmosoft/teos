package com.sccomz.scala.sql

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType

object SCHEDULE {
final val schema : StructType= StructType( Array(
StructField("SCHEDULE_ID"               ,StringType),
StructField("TYPE_CD"                   ,StringType),
StructField("SCENARIO_ID"               ,StringType),
StructField("USER_ID"                   ,StringType),
StructField("PRIORITIZE"                ,StringType),
StructField("PROCESS_CD"                ,StringType),
StructField("PROCESS_MSG"               ,StringType),
StructField("SCENARIO_PATH"             ,StringType),
StructField("REG_DT"                    ,StringType),
StructField("MODIFY_DT"                 ,StringType),
StructField("RETRY_CNT"                 ,StringType),
StructField("SERVER_ID"                 ,StringType),
StructField("BIN_X_CNT"                 ,StringType),
StructField("BIN_Y_CNT"                 ,StringType),
StructField("RU_CNT"                    ,StringType),
StructField("ANALYSIS_WEIGHT"           ,StringType),
StructField("PHONE_NO"                  ,StringType),
StructField("RESULT_TIME"               ,StringType),
StructField("TILT_PROCESS_TYPE"         ,StringType),
StructField("GEOMETRYQUERY_SCHEDULE_ID" ,StringType),
StructField("RESULT_BIT"                ,StringType),
StructField("INTERWORKING_INFO"         ,StringType)
))
}
