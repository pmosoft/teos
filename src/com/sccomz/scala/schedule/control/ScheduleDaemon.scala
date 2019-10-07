package com.sccomz.scala.schedule.control

import java.sql.DriverManager
import java.sql.Connection
import com.amazonaws.services.simpleworkflow.flow.core.TryCatch
import org.codehaus.janino.Java.TryStatement
import java.sql.Statement

object ScheduleDaemon {

  val url      = "jdbc:oracle:thin:@192.168.0.6:1521/ORCL";
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

    try {

      println(ScheduleDaemonSql.selectSchedule10001());
      while(true) {

      }

    } catch {
      case e : Exception => {
        println("Exception="+e);
      }
    } finally {
      con.close();
    }
  }

}