package com.sccomz.scala.schema

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.FloatType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.DoubleType

object SCENARIO {
final val schema : StructType= StructType( Array(
 StructField("SCENARIO_ID"                    , IntegerType)
,StructField("SCENARIO_NM"                    , StringType )
,StructField("USER_ID"                        , StringType )
,StructField("SYSTEM_ID"                      , IntegerType)
,StructField("NETWORK_TYPE"                   , IntegerType)
,StructField("SIDO_CD"                        , StringType )
,StructField("SIGUGUN_CD"                     , StringType )
,StructField("DONG_CD"                        , StringType )
,StructField("SIDO"                           , StringType )
,StructField("SIGUGUN"                        , StringType )
,StructField("DONG"                           , StringType )
,StructField("STARTX"                         , FloatType)
,StructField("STARTY"                         , FloatType)
,StructField("ENDX"                           , FloatType)
,StructField("ENDY"                           , FloatType)
,StructField("FA_MODEL_ID"                    , IntegerType)
,StructField("FA_SEQ"                         , IntegerType)
,StructField("SCENARIO_DESC"                  , StringType )
,StructField("USE_BUILDING"                   , IntegerType)
,StructField("USE_MULTIFA"                    , IntegerType)
,StructField("PRECISION"                      , IntegerType)
,StructField("PWRCTRLCHECKPOINT"              , IntegerType)
,StructField("MAXITERATIONPWRCTRL"            , IntegerType)
,StructField("RESOLUTION"                     , IntegerType)
,StructField("MODEL_RADIUS"                   , IntegerType)
,StructField("REG_DT"                         , StringType )
,StructField("MODIFY_DT"                      , StringType )
,StructField("UPPER_SCENARIO_ID"              , IntegerType)
,StructField("FLOORBUILDING"                  , IntegerType)
,StructField("FLOOR"                          , IntegerType)
,StructField("FLOORLOSS"                      , IntegerType)
,StructField("SCENARIO_SUB_ID"                , IntegerType)
,StructField("SCENARIO_SOLUTION_NUM"          , IntegerType)
,StructField("LOSS_TYPE"                      , IntegerType)
,StructField("RU_CNT"                         , IntegerType)
,StructField("MODIFY_YN"                      , StringType )
,StructField("BATCH_YN"                       , StringType )
,StructField("TM_STARTX"                      , IntegerType)
,StructField("TM_STARTY"                      , IntegerType)
,StructField("TM_ENDX"                        , IntegerType)
,StructField("TM_ENDY"                        , IntegerType)
,StructField("REAL_DATE"                      , StringType )
,StructField("REAL_DRM_FILE"                  , StringType )
,StructField("REAL_COMP"                      , StringType )
,StructField("REAL_CATT"                      , StringType )
,StructField("REAL_CATY"                      , StringType )
,StructField("BLD_TYPE"                       , StringType )
,StructField("RET_PERIOD"                     , IntegerType)
,StructField("RET_END_DATETIME"               , StringType )
,StructField("BUILDINGANALYSIS3D_YN"          , StringType )
,StructField("BUILDINGANALYSIS3D_RESOLUTION"  , IntegerType)
,StructField("AREA_ID"                        , IntegerType)
,StructField("BUILDINGANALYSIS3D_RELATED_YN"  , StringType )
,StructField("RELATED_ANALYSIS_COVLIMITRSRP"  , IntegerType)
,StructField("SCHEDULE_ID"                    , IntegerType)
))
}

