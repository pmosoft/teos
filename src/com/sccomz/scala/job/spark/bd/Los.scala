package com.sccomz.scala.job.spark.bd

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement

object Los {

  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    execute(scheduleId);
  }

  def execute(scheduleId: String) = {
    executeSql(scheduleId);
  }

  //  * * * Hive Query * * *
  def executeSql(scheduleId: String) = {
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();

    //var scheduleId = "8463233";

    var objNm = "RESULT_NR_BF_LOS"
    //------------------------------------------------------
    println(objNm + " 시작");
    //------------------------------------------------------

    var qry = s"""ALTER TABLE ${objNm} DROP PARTITION(SCHEDULE_ID=${scheduleId})"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
    //---------------------------------------------------
    val conf = new Configuration()
    val fs = FileSystem.get(conf)
    fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""), true)
    qry = s"""set hive.exec.dynamic.partition.mode=nonstrict"""; println(qry); stat.execute(qry);

    //---------------------------------------------------
    println("insert partition table");
    //---------------------------------------------------

    qry = s"""
insert into ${objNm} partition (schedule_id)
select tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz, 
       case when sum(case when value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los,
       max(schedule_id) as schedule_id
  from RESULT_NR_BF_LOS_RU
 where schedule_id=${scheduleId}  
 group by tbd_key, rx_tm_xpos, rx_tm_ypos, rx_floorz
"""
    println(qry); stat.execute(qry);
    
  }
  
}