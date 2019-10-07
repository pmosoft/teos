package com.sccomz.scala.load

import org.apache.spark.sql.SparkSession
import scala.sys.process

object LoadTable {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("LoadTable").getOrCreate() //.config("spark.master","local")

  }

  def lodTable(
                spark      : SparkSession
              , objNm      : String
              , scheduleId : String // scheduleId or ALL:전체
              , colInfo    : String
              , whereCond  : String
              , reuseTf    : Boolean
              ) =  {

      val parquetPath = "parquet.`/user/teos/parquet/entity/"
      var qry = ""

      if(scheduleId=="ALL") {
          qry = "SELECT "+colInfo+" FROM "+parquetPath+objNm+"` "+whereCond
      } else {
          qry = "SELECT "+colInfo+" FROM "+parquetPath+objNm+"/"+objNm+ "_" + scheduleId+"` "+whereCond
      }

      println("-------------------------------------------------------");
      println(qry);
      println("-------------------------------------------------------");

      if(reuseTf) {
          if(spark.catalog.tableExists(objNm)) {
              println("reuse is true && table["+objNm+"] is already exist")
          } else {
              println("reuse is true && table["+objNm+"] is not exist && create table")
              val tDF = spark.sql(qry);tDF.cache.createOrReplaceTempView(objNm);tDF.count()
          }
      } else {
          println("reuse is false && && recreate table ["+objNm+"]")
          spark.sql("DROP TABLE IF EXISTS "+objNm)
          val tDF = spark.sql(qry);tDF.cache.createOrReplaceTempView(objNm);tDF.count()
      }
  }

  def lodPartColTable(spark:SparkSession,objNm:String, scheduleId:String, colInfo:String, whereCond : String, reuseTf:Boolean) = {
      lodTable(spark,objNm, scheduleId, colInfo, whereCond, reuseTf)
  }

  def lodAllColTable(spark:SparkSession,objNm:String, scheduleId:String, whereCond : String, reuseTf:Boolean) = {
      var colInfo = "*"
      lodTable(spark,objNm, scheduleId, colInfo, whereCond, reuseTf)
  }

  def lodAllColAllDataTable(spark:SparkSession,objNm:String, whereCond : String, reuseTf:Boolean) = {
      var scheduleId = "ALL"
      var colInfo = "*"
      lodTable(spark,objNm, scheduleId, colInfo, whereCond, reuseTf)
  }


  def lodBasicTables(spark:SparkSession, scheduleId:String) = {

    //------------------------------------------------
        println("lodBasicTables 시작 : "+scheduleId);
    //------------------------------------------------
    lodRefererTable(spark,scheduleId)

  }

  def lodRefererTable(spark:SparkSession, scheduleId:String) = {
      var objNm     = "TB_WL_REFERER"
      var colInfo   = ""
      colInfo += "DATE_FORMAT(TO_DATE(C_TIME),'yyyyMMdd') AS STATIS_DATE"
      colInfo += ", GVHOST        "
      colInfo += ", VHOST         "
      colInfo += ",(CASE WHEN SUBSTR(URL,1,150)         IS NULL THEN '' ELSE URL         END) URL         "
      colInfo += ",(CASE WHEN HOST        IS NULL THEN '' ELSE HOST        END) HOST        "
      colInfo += ",(CASE WHEN DIR_CGI     IS NULL THEN '' ELSE DIR_CGI     END) DIR_CGI     "
      colInfo += ",(CASE WHEN KEYWORD     IS NULL THEN '' ELSE KEYWORD     END) KEYWORD     "
      colInfo += ", V_ID          "
      colInfo += ", U_ID          "
      colInfo += ", T_ID          "
      colInfo += ", C_TIME        "
      colInfo += ",(CASE WHEN DOMAIN      IS NULL THEN '' ELSE DOMAIN      END) DOMAIN    "
      colInfo += ",(CASE WHEN CATEGORY    IS NULL THEN '' ELSE CATEGORY    END) CATEGORY    "
      colInfo += ",(CASE WHEN V_IP IS NULL THEN '' ELSE V_IP END) V_IP "
      colInfo += ",(CASE WHEN SESSION_ID  IS NULL THEN '' ELSE SESSION_ID  END) SESSION_ID  "
      //colInfo += ", REF_URL       "
      //colInfo += ", REF_PARAM     "
      //colInfo += ", USER_AGENT    "
      colInfo += ",(CASE WHEN MOBILE_YN   IS NULL THEN '' ELSE MOBILE_YN   END) MOBILE_YN   "
      colInfo += ",(CASE WHEN OS          IS NULL THEN '' ELSE OS          END) OS          "
      colInfo += ",(CASE WHEN BROWSER     IS NULL THEN '' ELSE BROWSER     END) BROWSER     "
      colInfo += ",(CASE WHEN OS_VER      IS NULL THEN '' ELSE OS_VER      END) OS_VER      "
      colInfo += ",(CASE WHEN BROWSER_VER IS NULL THEN '' ELSE BROWSER_VER END) BROWSER_VER "
      colInfo += ",(CASE WHEN XLOC        IS NULL THEN '' ELSE XLOC        END) XLOC        "
      colInfo += ",(CASE WHEN LANG        IS NULL THEN '' ELSE LANG        END) LANG        "
      colInfo += ",(CASE WHEN DEVICE_ID   IS NULL THEN '' ELSE DEVICE_ID   END) DEVICE_ID   "
      //colInfo += ", URL_PARAM     "
      //colInfo += ", AREA_CODE     "
      //colInfo += ", CAMPAIGN_ID   "
      colInfo += ", LOGIN_TYPE    "
      colInfo += ", OPT2    "
      var whereCond = if(scheduleId=="D") "WHERE DATE_FORMAT(TO_DATE(C_TIME),'yyyyMMdd') = '"+scheduleId+"'" else /*M*/ ""
      lodPartColTable(spark,objNm,scheduleId,colInfo,whereCond,true)
      //수동 배치 작업시 아래 명령어 수행
      //lodPartColTable(spark,"TB_WL_REFERER",statisDate,statisType,colInfo,whereCond,false)
  }


}