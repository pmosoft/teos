package com.sccomz.scala.etl.extract.post

import java.io._
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

import scala.collection._
import scala.collection.mutable.Map

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.post.sql.ExtractJobDisSql


import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosEngResultSql

import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosBldResultSql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostScenarioNrRuDemSql
import com.sccomz.scala.etl.load.LoadHdfsLosManager
import com.sccomz.scala.etl.load.LoadHdfsManager

/*
import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
ExtractLoadPostManager.monitorJobDis("8463235","5113766");


ExtractLoadPostManager.executeExtractLoadAvg("8463233","5113566");

ExtractLoadPostManager.monitorJobDis("8463233","5113566");

ExtractJobDisSql.selectRuCnt("8463233");
ExtractLoadPostManager.extractPostToHadoopCsv("8460062","1012242284","gis01");

8463233	5113566

spark.sql("SELECT COUNT(*) FROM SCENARIO_NR_RU_AVG_HEIGHT WHERE SCENARIO_ID = 5113566;)").take(100).foreach(println);


spark.sql("SELECT COUNT(*) FROM (SELECT DISTINCT SCHEDULE_ID,RU_ID FROM RESULT_NR_BF_LOS_RU)").take(100).foreach(println);


spark.sql("SELECT COUNT(*) FROM SCENARIO_NR_RU_AVG_HEIGHT WHERE SCENARIO_ID = 5113566;)").take(100).foreach(println);

spark.sql("SELECT COUNT(*) FROM (SELECT DISTINCT SCHEDULE_ID,RU_ID FROM RESULT_NR_2D_LOS_RU)").take(100).foreach(println);

spark.sql("SELECT DISTINCT 'INSERT INTO TEMP001 VALUE ('''||RU_ID||''')' FROM RESULT_NR_2D_LOS_RU WHERE SCHEDULE_ID = 8460062").take(100).foreach(println);

SELECT DISTINCT SCHEDULE_ID,RU_ID
FROM RESULT_NR_2D_LOS_RU

 * */
object ExtractLoadPostManager {

  Class.forName(App.dbDriverPost);

  Class.forName(App.dbDriverPost);
  var con1:Connection = DriverManager.getConnection(App.dbUrlPost1,App.dbUserPost,App.dbPwPost);
  var stat1:Statement=con1.createStatement();
  var rs1:ResultSet = null;

  var con2:Connection = DriverManager.getConnection(App.dbUrlPost2,App.dbUserPost,App.dbPwPost);
  var stat2:Statement=con2.createStatement();
  var rs2:ResultSet = null;

  var con3:Connection = DriverManager.getConnection(App.dbUrlPost3,App.dbUserPost,App.dbPwPost);
  var stat3:Statement=con3.createStatement();
  var rs3:ResultSet = null;

  var con4:Connection = DriverManager.getConnection(App.dbUrlPost4,App.dbUserPost,App.dbPwPost);
  var stat4:Statement=con4.createStatement();
  var rs4:ResultSet = null;

  def main(args: Array[String]): Unit = {
    //monitorJobDis("");
    println("ExtractLoadPostManager start");

    //extractPostToHadoopCsv("8460062","1012242284","gis01");

    monitorJobDis("8463235","5113766");

    println("ExtractLoadPostManager end");
  }

  def monitorJobDis(scheduleId:String,scenarioId:String): Unit = {

    var qry = "";

    var ruCnt = 0; var extCnt = 0; var extDoneCnt = -1;
    var avgCnt = 1;

    var loofCnt = 0;
    var exeLoofCnt = 0;
    var bdYn = "N";

    qry = ExtractJobDisSql.selectBdYn(scenarioId); println(qry);
    rs1 = stat1.executeQuery(qry); rs1.next();
    bdYn = rs1.getString("BD_YN");
    var procStat = if(bdYn=="Y") 4 else 3;
    try {
      while(ruCnt>extDoneCnt) {
        loofCnt += 1;
        qry = ExtractJobDisSql.selectRuCnt(scheduleId,procStat); println(qry);
        rs1 = stat1.executeQuery(qry); rs1.next();
        ruCnt  = rs1.getInt("RU_CNT");
        extDoneCnt = rs1.getInt("EXT_DONE_CNT");
        extCnt = rs1.getInt("EXT_CNT");

        println("extCnt="+extCnt);

        if(extCnt>0) {
          exeLoofCnt += 1;
          if(exeLoofCnt==1) {
            executeExtractLoadOneTime(scheduleId,scenarioId);
          }
          executeExtractLoad(scheduleId,bdYn,procStat);
        }
        println("loofCnt="+loofCnt);
        Thread.sleep(1000*3);

      }

      println("execute end");

    } catch {
      case e : Exception => {
        println("Exception="+e);
      }
    } finally {
      con1.close();
      con2.close();
      con3.close();
      con4.close();
    }
  }

  def executeExtractLoad(scheduleId:String,bdYn:String,procStat:Int): Unit = {

    var qry = "";
    var ruId = ""; var clusterName = "";

    qry = ExtractJobDisSql.selectExtRu(scheduleId,procStat); println(qry);
    rs1 = stat1.executeQuery(qry);

    var extList = mutable.Map[String,String]();

    while(rs1.next()) {
    //if(rs.next()) {
        extList += (rs1.getString("RU_ID") -> rs1.getString("CLUSTER_NAME"));
        ruId  = rs1.getString("RU_ID");
        clusterName = rs1.getString("CLUSTER_NAME");
        //
    };

    for(ext <- extList) {
        ruId  = ext._1; clusterName = ext._2;
        insertJobDisExt(scheduleId,ruId,"4")
        extractPostToHadoopCsv(scheduleId,ruId,clusterName,bdYn);
        LoadHdfsLosManager.executeRealPostToHdfs(scheduleId, ruId, bdYn);
        updateJobDisExt(scheduleId,ruId,"5")
    }

    LoadHdfsLosManager.sparkClose();
  }

  def extractPostToHadoopCsv(scheduleId:String,ruId:String,clusterName:String,bdYn:String) : Unit = {

    //var dbUrl = if(clusterName=="gis01") App.dbUrlPost else if(clusterName=="gis02") App.dbUrlPost2 else if(clusterName=="gis03") App.dbUrlPost3 else if(clusterName=="gis04") App.dbUrlPost4 else App.dbUrlPost;
    //var con = DriverManager.getConnection(dbUrl,App.dbUserPost,App.dbPwPost);
    var stat:Statement = null;
    var rs:ResultSet = null;

    if(clusterName=="gis01") {
      stat = stat1; var rs = rs1
    } else if(clusterName=="gis02") {
      stat = stat2; var rs = rs2
    } else if(clusterName=="gis03") {
      stat = stat3; var rs = rs3
    } else if(clusterName=="gis04") {
      stat = stat4; var rs = rs4
    }

    var tabNm = ""; var qry = "";
    var filePathNm = "";

    if(bdYn=="Y") {
      //---------------------------------------
           tabNm = "RESULT_NR_BF_LOS_RU"
      //---------------------------------------
      qry = ExtractPostLosBldResultSql.selectLosBldResultCsv(scheduleId,ruId); println(qry);
      rs = stat.executeQuery(qry);
      filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+"_"+ruId+".dat"; println(filePathNm);
      var pw = new PrintWriter(new File(filePathNm),"UTF-8");
      while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

      ////---------------------------------------
      //     tabNm = "TREE_BLD_RESULT";
      ////---------------------------------------
      //qry = ExtractPostTreeBldResultSql.selectTreeBldResultCsv(scheduleId,ruId); println(qry);
      //rs = stat.executeQuery(qry);
      //var filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+"_"+ruId+".dat"; println(filePathNm);
      //var pw = new PrintWriter(new File(filePathNm),"UTF-8");
      //while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    } else {

      //---------------------------------------
           tabNm = "RESULT_NR_2D_LOS_RU";
      //---------------------------------------
      qry = ExtractPostLosEngResultSql.selectLosEngResultCsv(scheduleId,ruId); println(qry);
      rs = stat.executeQuery(qry);
      filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+"_"+ruId+".dat"; println(filePathNm);
      var pw = new PrintWriter(new File(filePathNm),"UTF-8");
      while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    //
    // //---------------------------------------
    //      tabNm = "TREE_ENG_RESULT";
    // //---------------------------------------
    // qry = ExtractPostTreeEngResultSql.selectTreeEngResultCsv(scheduleId,ruId); println(qry);
    // rs = stat.executeQuery(qry);
    // filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+"_"+ruId+".dat"; println(filePathNm);
    // pw = new PrintWriter(new File(filePathNm),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    }
  }

  def executeExtractLoadOneTime(scheduleId:String,scenarioId:String): Unit = {

    var tabNm = ""; var qry = "";

    //---------------------------------------
         tabNm = "SCENARIO_NR_RU_AVG_HEIGHT";
    //---------------------------------------
    qry = ExtractPostScenarioNrRuDemSql.selectScenarioNrRuDemCsv(scheduleId); println(qry);
    rs1 = stat1.executeQuery(qry);
    var filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat"; println(filePathNm);
    var pw = new PrintWriter(new File(filePathNm),"UTF-8");
    while(rs1.next()) { pw.write(rs1.getString(1)+"\n") }; pw.close;

    LoadHdfsManager.postAvgToHdfs(scheduleId, scenarioId);

    //---------------------------------------
         tabNm = "BLD_LIST";
    //---------------------------------------

  }

  def insertJobDisExt(scheduleId:String,ruId:String,stat2:String) : Unit = {
    Class.forName(App.dbDriverPost);
    var con = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();
    var qry = ExtractJobDisSql.insertJobDisExt(scheduleId, ruId, stat2); println(qry); stat.execute(qry);

  }

  def updateJobDisExt(scheduleId:String,ruId:String,stat2:String) : Unit = {
    Class.forName(App.dbDriverPost);
    var con = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();
    var qry = ExtractJobDisSql.updateJobDisExt(scheduleId, ruId, stat2); println(qry); stat.execute(qry);
  }


}