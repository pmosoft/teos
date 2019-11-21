package com.sccomz.scala.job.spark.bak.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement



/*
 * 설    명 :
 * 입    력 : SCHEDULE
           SCENARIO
           RESULT_NR_2D_LOS_RU
 * 출    력 : RESULT_NR_2D_LOS
 * 수정내역 :
 * 2019-11-01 | 박준용 | 최초작성

import com.sccomz.scala.job.spark.eng.Los2
Los2.execute("8463233");



spark.sql(s"""
SELECT RU_ID 
FROM   RESULT_NR_2D_LOS_RU 
WHERE SCHEDULE_ID = 8463233 
GROUP BY RU_ID
ORDER BY RU_ID
""").take(100).foreach(println);


spark.sql(s"""
SELECT RU_ID 
FROM   RESULT_NR_2D_LOS_RU
WHERE SCHEDULE_ID = 8463233 
""").take(100).foreach(println);


 */

object Los2{

  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    execute(scheduleId);
  }

  def execute(scheduleId: String) = {
    val spark: SparkSession = SparkSession.builder().master("yarn").appName(this.getClass.getName).config("spark.sql.warehouse.dir", "/TEOS/warehouse").enableHiveSupport().getOrCreate();
    executeSqlSpark(spark, scheduleId);
    spark.close();
    //executeSql(scheduleId);
  }

  //  * * * Spark Query * * *
  def executeSqlSpark(spark: SparkSession, scheduleId: String) = {
    //var scheduleId = "8463233";

    var objNm = "RESULT_NR_BF_LOS"
    //------------------------------------------------------
    println(objNm + " 시작");
    //------------------------------------------------------
    var qry = "";

    //---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
    //---------------------------------------------------
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""), true)
    import spark.implicits._
    import spark.sql
    qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; sql(qry);

    //---------------------------------------------------
    println("insert partition table");
    //---------------------------------------------------

    sql("set hive.exec.dynamic.partition.mode=nonstrict");

    qry = s"""
insert into ${objNm} partition (schedule_id=${scheduleId})
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, 
       case when sum(case when value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los
       from RESULT_NR_BF_LOS_RU
 where schedule_id=${scheduleId}
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
    println(qry); spark.sql(qry).take(100).foreach(println);

  }

}
