package com.sccomz.scala.schedule.control

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import com.sccomz.scala.schedule.control.sql.ScheduleDaemonSql

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
    var qry = "";

    try {
      val list = mutable.MutableList[Map[String,String]]()
      //while(true) {
      while(loofCnt<3) {
        loofCnt += 1;
        println("loofCnt="+loofCnt);

        //------------------------------------------------------------------
        // [갱신:SCHEDULE, 삽입:SCHEDULE_EXT] 스케줄대상건의 BIN갯수 갱신
        //------------------------------------------------------------------
        // PROCESS_CD 10001(분석요청)이면서 BIN갯수 미세팅건이나 SCHEDULE_EXT 미생생된 경우

        qry = ScheduleDaemonSql.selectBinCount(); println(qry);
        rs = stat.executeQuery(qry);
        while(rs.next()){
          var map = HashMap[String,String]();
          map.put("SCHEDULE_ID" , rs.getString("SCHEDULE_ID"));
          map.put("BIN_X_CNT"   , rs.getString("BIN_X_CNT"  ));
          map.put("BIN_Y_CNT"   , rs.getString("BIN_Y_CNT"  ));
          map.put("AREA"        , rs.getString("AREA"       ));
          map.put("RU_CNT"      , rs.getString("RU_CNT"     ));
          list += map;
          //println( "SCHEDULE_ID="+rs.getString(1) );
        }

        var scheduleId = ""; var binXCnt = ""; var binYCnt = ""; var area = ""; var ruCnt = "";
        for(m <- list) {

          scheduleId = m.getOrElse("SCHEDULE_ID","");
          binXCnt    = m.getOrElse("BIN_X_CNT","");
          binYCnt    = m.getOrElse("BIN_Y_CNT","");
          area       = m.getOrElse("AREA","");
          ruCnt      = m.getOrElse("RU_CNT","");

          //[갱신:SCHEDULE]
          qry = ScheduleDaemonSql.updateScheduleBinCnt(scheduleId, binXCnt, binYCnt, ruCnt); println(qry);
          stat.execute(qry);

          //[삭제:SCHEDULE_EXT]
          qry = ScheduleDaemonSql.deleteScheduleExt(scheduleId); println(qry);
          stat.execute(qry);

          //[삽입:SCHEDULE_EXT]
        }

        //updateScheduleBinCnt

        //for(i <- 0 to rowCnt) {}
        //println( "list.size="+list.size );
        //for(m <- list) println(m.get("SCHEDULE_ID").fold("")(_.toString));
        list.clear();
        //for((k,v) <- map) printf("key: %s, value: %s\n", k, v)
        //map.foreach { case (key, value) => println(">>> key=" + key + ", value=" + value) }

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