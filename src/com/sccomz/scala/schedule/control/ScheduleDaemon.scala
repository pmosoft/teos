package com.sccomz.scala.schedule.control

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._
import com.sccomz.scala.schedule.control.sql.ScheduleDaemonSql
import com.sccomz.scala.comm.App
import java.lang.Runtime
import scala.sys.process._
import java.io.ByteArrayInputStream

object ScheduleDaemon {

  Class.forName(App.dbDriverOra);
  var con:Connection = DriverManager.getConnection(App.dbUrlOra,App.dbUserOra,App.dbPwOra);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var qry = "";

  def main(args: Array[String]): Unit = {

  }


  def execute(): Unit = {
    var loofCnt = 0;
    try {
      while(loofCnt<3) {
        loofCnt += 1;

        updateBinRuInfo();
        excuteJob();

        println("loofCnt="+loofCnt);

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

  def excuteJob(): Unit = {
    qry = ScheduleDaemonSql.selectBinRuCount(); println(qry);
    rs = stat.executeQuery(qry);
    while(rs.next()){

      var map = HashMap[String,String]();
      map.put("SCHEDULE_ID" , rs.getString("SCHEDULE_ID"));
      //println( "SCHEDULE_ID="+rs.getString(1) );
    }

  }

  /*
   * 사전작업 : 스케줄 테이블에 Bin갯수, Ru갯수, 빌딩크기 산출후 잡가중치를 저장한다
   */
  def updateBinRuInfo(): Unit = {


    val list = mutable.MutableList[Map[String,String]]()

    //------------------------------------------------------------------
    // [갱신:SCHEDULE, 삽입:SCHEDULE_EXT] 스케줄대상건의 BIN갯수 갱신
    //------------------------------------------------------------------
    // PROCESS_CD 10001(분석요청)이면서 BIN갯수 미세팅건이나 SCHEDULE_EXT 미생생된 경우

    qry = ScheduleDaemonSql.selectBinRuCount(); println(qry);
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
      qry = ScheduleDaemonSql.updateScheduleBinRuCnt(scheduleId, binXCnt, binYCnt, ruCnt); println(qry);
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
  }

  def selectExecuteJob(): Unit = {
  }


  def executeEtlToPostgre(): Unit = {
  }

  def executePostgreProcedure(): Unit = {
  }

  def executeEtlToHdfs(): Unit = {
  }

  def executeSparkJob(): Unit = {
  }

  def executeMakeBinFile(): Unit = {
  }



//def getProcessIds(key: String): util.ArrayList[Int] ={
//    import java.io.{BufferedReader, InputStreamReader}
//    var pids = new util.ArrayList[Int]()
//    val re = false
//    try {
//        val command = s"ps aux | grep '${key}'"
//        val process = Runtime.getRuntime.exec( Array("bash", "-c", command) )
//        val br = new BufferedReader(new InputStreamReader(process.getInputStream))
//        var line: String = br.readLine()
//        while (line != null){
//            println(line)
//            val tInputStringStream = new ByteArrayInputStream(line.getBytes)
//            // let string as input stream read the process id
//            val scanner = new Scanner(tInputStringStream)
//            val user = scanner.next()
//            val pid = scanner.nextInt()
//            scanner.close()
//            pids.add(pid)
//            line = br.readLine()
//        }
//        br.close()
//        pids
//    } catch {
//        case e: Exception =>
//        System.out.println(e.toString)
//        null
//    }
//}
//def killProcesses(key: String): Int={
//    var killNumbers = 0
//    val pids: List[Int] = getProcessIds(key)
//    println( pids )
//    pids.foreach(pid => {
//        val process = Runtime.getRuntime().exec(Array("bash", "-c", s"kill -9 ${pid}"))
//        if( process.waitFor() == 0)  // 因?有些得到的?程id是前面本身grep?生的无意?的
//        	killNumbers += 1
//    })
//    killNumbers
//}
//killProcesses("infrastructure.TranswarpServer")


}