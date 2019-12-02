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
import com.sccomz.scala.schedule.real.ExecuteJob


import java.util.logging.Logger
import org.apache.spark.internal.Logging

/*
import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
ExtractLoadPostManager.deleteJobDisExt("8463246");



import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
ExtractLoadPostManager.deleteJobDisExt("8460178");


import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
ExtractLoadPostManager.deleteJobDisExt("8463290");

ExtractLoadPostManager.monitorJobDis("8460178");


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
object ExtractLoadPostManager extends Logging {

  var scenarioId = "";
  var typeCd = "";
  
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

    monitorJobDis("8463235",true);

    //5113766
    println("ExtractLoadPostManager end");
  }

  def deleteJobDisExt(scheduleId:String): Unit = {
    var qry = ExtractJobDisSql.deleteJobDisExt(scheduleId); println(qry); stat1.execute(qry)
  }
  
  
  def monitorJobDis(scheduleId:String,isRetry:Boolean): Unit = {

    logInfo("monitorJobDis start scheduleId="+scheduleId)    
    
    var qry = "";

    if(isRetry) deleteJobDisExt(scheduleId)
 
    scenarioId = ExecuteJob.selectScenarioId(scheduleId)
    typeCd = ExecuteJob.selectTypeCd(scheduleId)
 
    var ruCnt = 0; var post_done_cnt = 0; var extCnt = 0; var extDoneCnt = -1;
    var avgCnt = 1;

    var loofCnt = 0;
    var isLoof = true;
    var exeLoofCnt = 0;
    var bdYn = "N";

    qry = ExtractJobDisSql.selectBdYn(scenarioId); println(qry);
    rs1 = stat1.executeQuery(qry); rs1.next();
    bdYn = rs1.getString("BD_YN");

    try {
      while(isLoof) {
        loofCnt += 1;
        qry = ExtractJobDisSql.selectRuCnt(scheduleId,typeCd,bdYn); println(qry);
        rs1 = stat1.executeQuery(qry); rs1.next();
        ruCnt  = rs1.getInt("RU_CNT");
        extDoneCnt = rs1.getInt("EXT_DONE_CNT");
        extCnt = rs1.getInt("EXT_CNT");

        if(ruCnt > 0 && ruCnt==post_done_cnt && post_done_cnt==extDoneCnt) isLoof = false
        
        println("extCnt="+extCnt);

        if(extCnt>0) {
          exeLoofCnt += 1;
          if(exeLoofCnt==1) {
            executeExtractLoadOneTime(scheduleId,scenarioId);
          }
          executeExtractLoad(scheduleId,typeCd,bdYn);
        }
        println("loofCnt="+loofCnt);
        Thread.sleep(1000*3);

      }

      logInfo("monitorJobDis end scheduleId="+scheduleId)    

    } catch {
      case ex: Throwable => {logInfo("ex="+ex);throw ex}
    } finally {
      con1.close();
      con2.close();
      con3.close();
      con4.close();
    }
  }

  def executeExtractLoad(scheduleId:String,typeCd:String,bdYn:String): Unit = {

   logInfo("executeExtractLoad start scheduleId="+scheduleId)    
    
    
    var qry = "";
    var ruId = ""; var clusterName = "";

    qry = ExtractJobDisSql.selectExtRu(scheduleId,typeCd,bdYn); println(qry);
    rs1 = stat1.executeQuery(qry);

    val extList = mutable.Map[String,String]();

    while(rs1.next()) {
    //if(rs.next()) {
        extList += (rs1.getString("RU_ID") -> rs1.getString("CLUSTER_NAME"));
        ruId  = rs1.getString("RU_ID");
        clusterName = rs1.getString("CLUSTER_NAME");
        //
    };

    for(ext <- extList) {
        ruId  = ext._1; clusterName = ext._2;
        println("ruId="+ruId);
        
        insertJobDisExt(scheduleId,ruId,"4")
        println("ruId1="+ruId);
        extractPostToHadoopCsv(scheduleId,ruId,clusterName,typeCd,bdYn);
        println("ruId2="+ruId);
        LoadHdfsLosManager.executeRealPostToHdfs(scheduleId,ruId,typeCd,bdYn);
        println("ruId3="+ruId);
        updateJobDisExt(scheduleId,ruId,"5")
        println("ruId4="+ruId);
    }

   logInfo("executeExtractLoad end scheduleId="+scheduleId)    
    
    //LoadHdfsLosManager.sparkClose();
  }

  def extractPostToHadoopCsv(scheduleId:String,ruId:String,clusterName:String,typeCd:String,bdYn:String) : Unit = {

    logInfo("extractPostToHadoopCsv start scheduleId="+scheduleId)    
    
    
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

    if(typeCd=="SC051" && bdYn=="Y") {
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
    
    logInfo("extractPostToHadoopCsv end scheduleId="+scheduleId)    
  }

  def executeExtractLoadOneTime(scheduleId:String,scenarioId:String): Unit = {
    logInfo("executeExtractLoadOneTime start scheduleId="+scheduleId)    
    
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
         
    logInfo("executeExtractLoadOneTime end scheduleId="+scheduleId)    

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