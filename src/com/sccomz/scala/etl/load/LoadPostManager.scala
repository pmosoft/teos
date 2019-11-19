package com.sccomz.scala.etl.load

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import com.sccomz.scala.comm.App
import com.sccomz.scala.schema.SCENARIO
import scala.io.Source

/*
import com.sccomz.scala.etl.load.LoadPostManager
LoadPostManager.oracleToPostgreAll("8463234");
LoadPostManager.oracleToPostgreAll("8463235");

LoadPostManager.oracleToPostgreAll("8459967");

 * */
object LoadPostManager {

  Class.forName(App.dbDriverPost);

  def main(args: Array[String]): Unit = {
    //oracleToPostgreAll("8460064");
    //oracleToPostgre("8459967",1);

    //8463233	5113566
    
    println("LoadPostManager start");    


    oracleToPostgreAll("8463234");
    oracleToPostgreAll("8463235");
    
    //oracleToPostgreAll("8463233");
    //oracleToPostgreAll("8460064");
    
    //oracleToPostgre("8460178",1);
    //oracleToPostgre("8460179",1);
    //oracleToPostgre("8460062",1);
    //oracleToPostgre("8460063",1);

    //oracleToPostgreAll("8460178");
    //oracleToPostgreAll("8460179");
    //oracleToPostgreAll("8460062");
    //oracleToPostgreAll("8460063");
    
    println("LoadPostManager end");    
    
  }

  def oracleToPostgreAll(scheduleId:String) = {
    oracleToPostgre(scheduleId,1); oracleToPostgre(scheduleId,2); oracleToPostgre(scheduleId,3); oracleToPostgre(scheduleId,4);
  }

  def oracleToPostgre(scheduleId:String,num:Integer) = {
    var dbUrl = if(num==1) App.dbUrlPost else if(num==2) App.dbUrlPost2 else if(num==3) App.dbUrlPost3 else if(num==4) App.dbUrlPost4 else App.dbUrlPost;
    var con = DriverManager.getConnection(dbUrl,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement(); var qry = ""; var filePathNm = "";
    // DELETE
    qry = s"""DELETE FROM SCENARIO         WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})"""; println(qry); stat.execute(qry);
    qry = s"""DELETE FROM MOBILE_PARAMETER WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})"""; println(qry); stat.execute(qry);
    qry = s"""DELETE FROM SCENARIO_NR_RU   WHERE SCENARIO_ID IN (SELECT SCENARIO_ID||'' FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})"""; println(qry); stat.execute(qry);
    qry = s"""DELETE FROM SCHEDULE         WHERE SCHEDULE_ID=${scheduleId}"""                                                          ; println(qry); stat.execute(qry);

    // INSERT    
    try {
        filePathNm = App.extJavaPath+"/SCHEDULE_"        +scheduleId+".sql"; println(filePathNm); for(qry <- Source.fromFile(filePathNm).getLines()){stat.execute(qry);}
        filePathNm = App.extJavaPath+"/SCENARIO_"        +scheduleId+".sql"; println(filePathNm); for(qry <- Source.fromFile(filePathNm).getLines()){stat.execute(qry);}
        filePathNm = App.extJavaPath+"/MOBILE_PARAMETER_"+scheduleId+".sql"; println(filePathNm); for(qry <- Source.fromFile(filePathNm).getLines()){stat.execute(qry);}
        filePathNm = App.extJavaPath+"/SCENARIO_NR_RU_"  +scheduleId+".sql"; println(filePathNm); for(qry <- Source.fromFile(filePathNm).getLines()){stat.execute(qry);}
    } catch {
        case e: Exception => println(e)
    }
    con.close();
  }

}