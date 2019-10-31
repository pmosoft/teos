package com.sccomz.scala.etl.load

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import com.sccomz.scala.comm.App
import com.sccomz.scala.schema.SCENARIO
import scala.io.Source

/*
import com.sccomz.scala.etl.load.LoadManager
LoadManager.samToParquetPartition(spark,"SCHEDULE","8459967");
LoadManager.samToParquetPartition(spark,"SCENARIO","8459967");

//LoadManager.impalaDropPartition("SCHEDULE","8459967","impala");
//LoadManager.impalaAddPartition("SCHEDULE","8459967","impala");

 * */
object LoadPostManager {

  Class.forName(App.dbDriverPost);

  def main(args: Array[String]): Unit = {
    oracleToPostgreAll("");
  }

  def oracleToPostgreAll(scheduleId:String) = {
    oracleToPostgre(scheduleId,1); oracleToPostgre(scheduleId,2); oracleToPostgre(scheduleId,3); oracleToPostgre(scheduleId,4);
  }

  def oracleToPostgre(scheduleId:String,num:Integer) = {
    var dbUrl = if(num==1) App.dbUrlPost else if(num==2) App.dbUrlPost else if(num==3) App.dbUrlPost else if(num==4) App.dbUrlPost else App.dbUrlPost;
    var con = DriverManager.getConnection(dbUrl,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();

    try {
        for(qry <- Source.fromFile(App.extJavaPath+"/SCHEDULE_"        +scheduleId+".sql").getLines()){stat.execute(qry);}
        for(qry <- Source.fromFile(App.extJavaPath+"/SCENARIO_"        +scheduleId+".sql").getLines()){stat.execute(qry);}
        for(qry <- Source.fromFile(App.extJavaPath+"/DU_"              +scheduleId+".sql").getLines()){stat.execute(qry);}
        for(qry <- Source.fromFile(App.extJavaPath+"/RU_"              +scheduleId+".sql").getLines()){stat.execute(qry);}
        for(qry <- Source.fromFile(App.extJavaPath+"/MOBILE_PARAMETER_"+scheduleId+".sql").getLines()){stat.execute(qry);}
        for(qry <- Source.fromFile(App.extJavaPath+"/SCENARIO_NR_RU_"  +scheduleId+".sql").getLines()){stat.execute(qry);}
    } catch {
        case e: Exception => println(e)
    }
    con.close();
  }

}
