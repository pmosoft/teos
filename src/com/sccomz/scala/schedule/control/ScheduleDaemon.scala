package com.sccomz.scala.schedule.control

import java.sql.DriverManager
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet

object ScheduleDaemon {

  val url      = "jdbc:oracle:thin:@192.168.0.6:1521/ORCL";
  //val url      = "jdbc:oracle:thin:@localhost:9951/IAMLTE";
  val user     = "cellplan";
  val password = "cell_2012";
  val driver   = "oracle.jdbc.driver.OracleDriver";


  var ru_unit    = 20;    // RU 수량 단위
  var ru_weight  = 5;     // RU 수량 가중치
  var bin_unit   = 50000; // BIN 단위
  var bin_weight = 1;     // BIN 수량


  def main(args: Array[String]): Unit = {
    execute();
  }


  def execute(): Unit = {

    Class.forName(driver);
    var con:Connection = DriverManager.getConnection(url,user,password);
    var stat:Statement=con.createStatement();
    var rs:ResultSet = null;

    var loofCnt = 0; var rowCnt = 0;

    try {

      //while(true) {
      while(loofCnt<3) {
        loofCnt += 1;
        println("loofCnt="+loofCnt+" " +ScheduleDaemonSql.selectSchedule10001());
        rs = stat.executeQuery(ScheduleDaemonSql.selectSchedule10001());
        //rowCnt = rs.getMetaData.getColumnCount;
        while(rs.next()){
          println( "SCHEDULE_ID="+rs.getString(1) );
        }
        //for(i <- 0 to rowCnt) {}

        Thread.sleep(1000*3);
      }

      println("execute end");

    } catch {
      case e : Exception => {
        println("Exception="+e);
      }
    } finally {
      con.close();
    }
  }

}