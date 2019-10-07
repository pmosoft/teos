package com.sccomz.scala.sql

import org.apache.spark.sql.SparkSession

import com.sccomz.scala.load.LoadTable
import com.sccomz.scala.schedule.control.RealJob

/*
 * 설    명 : PassLoss
 * 입    력 :

SCHEDULE

 * 출    력 : TB_RESULT01
 * 수정내역 :
 * 2019-09-30 | 피승현 | 최초작성
 */
object PassLoss {

  val spark = RealJob.spark
  var objNm = "TB_RESULT01"

  var scheduleId = ""
  //var scheduleId = "8443705";

  def executeReal(scheduleId : String) = {
    //------------------------------------------------------
    println("PassLoss : "+scheduleId+" 시작")
    //------------------------------------------------------
    this.scheduleId = scheduleId
    loadTables(); excuteSql(); saveToParqeut(); ettToOracle()
  }

  def loadTables() = {
    LoadTable.lodAllColTable(spark,"SCHEDULE" ,scheduleId,"",true)
  }

  def excuteSql() = {
    var qry = ""
    qry =
    s"""
    SELECT
           '${scheduleId}'                                                                  AS STATIS_TYPE
         , CASE WHEN TA.GVHOST IS NULL THEN 'TOTAL' ELSE TA.GVHOST END                      AS GVHOST
         , 'ALL'                                                                            AS ATTR_ID
         , SUM(CASE WHEN TA.VISIT_CNT         IS NULL THEN 0 ELSE TA.VISIT_CNT         END) AS VISIT_CNT
         , SUM(CASE WHEN TE.TOT_VISITOR_CNT   IS NULL THEN 0 ELSE TE.TOT_VISITOR_CNT   END) AS TOT_VISITOR_CNT
         , SUM(CASE WHEN TE.NEW_VISITOR_CNT   IS NULL THEN 0 ELSE TE.NEW_VISITOR_CNT   END) AS NEW_VISITOR_CNT
         , SUM(CASE WHEN TE.RE_VISITOR_CNT    IS NULL THEN 0 ELSE TE.RE_VISITOR_CNT    END) AS RE_VISITOR_CNT
         , SUM(CASE WHEN TB.LOGIN_CNT         IS NULL THEN 0 ELSE TB.LOGIN_CNT         END) AS LOGIN_CNT
         , SUM(CASE WHEN TB.LOGIN_VISITOR_CNT IS NULL THEN 0 ELSE TB.LOGIN_VISITOR_CNT END) AS LOGIN_VISITOR_CNT
         , SUM(CASE WHEN TC.DUR_TIME          IS NULL THEN 0 ELSE TC.DUR_TIME          END) AS DUR_TIME
         , SUM(CASE WHEN TC.PAGE_CNT          IS NULL THEN 0 ELSE TC.PAGE_CNT          END) AS PAGE_CNT
         , SUM(CASE WHEN TC.PAGE_VIEW         IS NULL THEN 0 ELSE TC.PAGE_VIEW         END) AS PAGE_VIEW
         , SUM(CASE WHEN TD.BOUNCE_CNT        IS NULL THEN 0 ELSE TD.BOUNCE_CNT        END) AS BOUNCE_CNT
    FROM
           (
           -- 방문수
           SELECT
                  GVHOST               AS GVHOST
                , COUNT(*)             AS VISIT_CNT
           FROM   TB_REFERER_SESSION
           GROUP BY GVHOST
           ) TA
           LEFT OUTER JOIN
           (
           -- 로그인방문자수, 로그인수
           SELECT GVHOST                          AS GVHOST
                , COUNT(DISTINCT T_ID)            AS LOGIN_VISITOR_CNT
                , COUNT(*)                        AS LOGIN_CNT
           FROM   TB_MEMBER_CLASS_SESSION
           GROUP BY GVHOST
           ) TB
           ON  TA.GVHOST = TB.GVHOST
           LEFT OUTER JOIN
           (
           -- 페이지뷰, 페이지수, 체류시간
           SELECT
                  GVHOST          AS GVHOST
                , SUM(PAGE_VIEW)  AS PAGE_VIEW
                , SUM(PAGE_CNT)   AS PAGE_CNT
                , SUM(DUR_TIME)   AS DUR_TIME
           FROM   TB_ACCESS_SESSION2
           GROUP BY GVHOST
           ) TC
           ON  TA.GVHOST = TC.GVHOST
           LEFT OUTER JOIN
           (
           -- 이탈수
           SELECT
                  GVHOST          AS GVHOST
                , COUNT(CASE WHEN PAGE_VIEW = 1 THEN 1 ELSE NULL END) AS BOUNCE_CNT
           FROM   TB_ACCESS_SESSION
           GROUP BY GVHOST
           ) TD
           ON  TA.GVHOST = TD.GVHOST
           LEFT OUTER JOIN
           (
           -- 방문자수,신규방문자수,재방문자수
           SELECT
                  GVHOST               AS GVHOST
                , COUNT(DISTINCT V_ID) AS TOT_VISITOR_CNT
                , COUNT(DISTINCT(CASE WHEN '20'||SUBSTR(V_ID, 2, 6)  LIKE '${scheduleId}%' THEN V_ID ELSE NULL END)) AS NEW_VISITOR_CNT
                , COUNT(DISTINCT(CASE WHEN '20'||SUBSTR(V_ID, 2, 6) NOT LIKE '${scheduleId}%' THEN V_ID ELSE NULL END)) AS RE_VISITOR_CNT
           FROM   TB_REFERER_SESSION3
           GROUP BY GVHOST
           ) TE
           ON  TA.GVHOST = TE.GVHOST
    GROUP BY ROLLUP(TA.GVHOST)
    """
    //spark.sql(qry).take(100).foreach(println);

    /*
    qry =
    """
           SELECT *
           FROM   SCHEDULE
    """
    spark.sql(qry).take(100).foreach(println);


    qry =
    """
           SELECT
                  GVHOST               AS GVHOST
                , COUNT(*)             AS VISIT_CNT
                , COUNT(DISTINCT V_ID) AS TOT_VISITOR_CNT
                , COUNT(DISTINCT (CASE WHEN '20'||SUBSTR(V_ID, 2, 6)  = '${scheduleId}' THEN V_ID ELSE NULL END)) AS NEW_VISITOR_CNT
                , COUNT(DISTINCT (CASE WHEN '20'||SUBSTR(V_ID, 2, 6) != '${scheduleId}' THEN V_ID ELSE NULL END)) AS RE_VISITOR_CNT
           FROM   TB_REFERER_SESSION
           GROUP BY GVHOST
    """
    spark.sql(qry).take(100).foreach(println);

           SELECT
                  GVHOST               AS GVHOST
                ,(CASE WHEN DEVICE_ID IS NULL OR DEVICE_ID = '' THEN 'NA' ELSE DEVICE_ID END) DEVICE_ID
                , COUNT(*)             AS VISIT_CNT
                , COUNT(DISTINCT V_ID) AS TOT_VISITOR_CNT
           FROM   TB_REFERER_SESSION
           GROUP BY GVHOST, DEVICE_ID





    qry =
    """
           SELECT
                  GVHOST               AS GVHOST
                , V_ID
                , '20'||SUBSTR(V_ID, 2, 6)
           FROM   TB_REFERER_SESSION
           WHERE  GVHOST = 'TMOW'
    """
    spark.sql(qry).take(100).foreach(println);

    qry =
    """
           SELECT
                  SESSION_ID, DUR_TIME
           FROM   TB_ACCESS_SESSION
           WHERE  GVHOST = 'MW'
           AND    SESSION_ID = 'A190130023104244737'
    """
    spark.sql(qry).take(100).foreach(println);

    qry =
    """
                  SELECT GVHOST                 AS GVHOST
                       , SESSION_ID             AS SESSION_ID
                       , MIN(C_TIME)            AS START_TIME
                       , MAX(C_TIME)            AS END_TIME
                  FROM   TB_WL_URL_ACCESS
                  WHERE  GVHOST = 'MW'
                  AND    SESSION_ID = 'A190130023104244737'
                  GROUP  BY GVHOST, SESSION_ID
                  ORDER BY GVHOST, SESSION_ID
    """
    spark.sql(qry).take(100).foreach(println);

    */

    //--------------------------------------
    println(qry);
    //--------------------------------------
    val sqlDf = spark.sql(qry)
    sqlDf.cache.createOrReplaceTempView(objNm); sqlDf.count()
  }

  def saveToParqeut() {
    //MakeParquet.dfToParquet(objNm, true, scheduleId)
  }

  def ettToOracle() {
    //OJDBC.deleteTable(spark, "DELETE FROM " + objNm + " WHERE STATIS_DATE='" + scheduleId + "' AND STATIS_TYPE='" + statisType + "'")
    //OJDBC.insertTable(spark, objNm)
  }

}
