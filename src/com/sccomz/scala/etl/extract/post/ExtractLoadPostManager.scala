package com.sccomz.scala.etl.extract.post

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import scala.collection._

import com.sccomz.scala.comm.App
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosEngResultDis1Sql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosBldResultDisSql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosBldResultDis1Sql
import com.sccomz.scala.etl.extract.post.sql.ExtractJobDisSql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosEngResultSql
import com.sccomz.scala.etl.load.LoadHdfsLosManager

/*
import com.sccomz.scala.etl.extract.post.ExtractLoadPostManager
ExtractLoadPostManager.monitorJobDis("8460062","5113566");

ExtractJobDisSql.selectRuCnt("8463233");
ExtractLoadPostManager.extractPostToHadoopCsv("8460062","1012242284","gis01");

8463233	5113566

 * */
object ExtractLoadPostManager {

  Class.forName(App.dbDriverPost);
  var con:Connection = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    //monitorJobDis("");
    println("ExtractLoadPostManager start");
    
    //extractPostToHadoopCsv("8460062","1012242284","gis01");

    monitorJobDis("8460062","5113566");
    
    println("ExtractLoadPostManager end");
  }

  def monitorJobDis(scheduleId:String,scenarioId:String): Unit = {

    Class.forName(App.dbDriverPost);
    var con:Connection = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();
    var rs:ResultSet = null;
    var qry = "";

    var ruCnt = 0; var extCnt = 0; var extDoneCnt = -1;

    var loofCnt = 0;
    try {
      while(ruCnt>extDoneCnt) {
        loofCnt += 1;

        qry = ExtractJobDisSql.selectRuCnt(scheduleId); println(qry);
        rs = stat.executeQuery(qry); rs.next();
        ruCnt  = rs.getInt("RU_CNT");
        extDoneCnt = rs.getInt("EXT_DONE_CNT");
        extCnt = rs.getInt("EXT_CNT");

        if(extCnt>0) executeExtractLoad(scheduleId);

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

  def executeExtractLoad(scheduleId:String): Unit = {

    Class.forName(App.dbDriverPost);
    var con:Connection = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();
    var rs:ResultSet = null;
    var qry = "";
    var ruId = ""; var clusterName = "";

    qry = ExtractJobDisSql.selectExtRu(scheduleId);
    rs = stat.executeQuery(qry);

    var extList = mutable.Map[String,String]();

    
    //while(rs.next()) {
    if(rs.next()) {
        extList += (rs.getString("RU_ID") -> rs.getString("CLUSTER_NAME"));
        ruId  = rs.getString("RU_ID");
        clusterName = rs.getString("CLUSTER_NAME");
        //
    };

    for(ext <- extList) {
        ruId  = ext._1; clusterName = ext._2;
        insertJobDisExt(scheduleId,ruId,"4")
        extractPostToHadoopCsv(scheduleId,ruId,clusterName);
        LoadHdfsLosManager.samToParquetPartition("RESULT_NR_2D_LOS_RU", scheduleId, ruId);
        updateJobDisExt(scheduleId,ruId,"5")
    }

    LoadHdfsLosManager.sparkClose;    
  }


  def extractPostToHadoopCsv(scheduleId:String,ruId:String,clusterName:String) : Unit = {

    Class.forName(App.dbDriverPost);
    var dbUrl = if(clusterName=="gis01") App.dbUrlPost else if(clusterName=="gis02") App.dbUrlPost2 else if(clusterName=="gis03") App.dbUrlPost3 else if(clusterName=="gis04") App.dbUrlPost4 else App.dbUrlPost;
    var con = DriverManager.getConnection(dbUrl,App.dbUserPost,App.dbPwPost);
    var stat:Statement=con.createStatement();
    var rs:ResultSet = null;

    var tabNm = ""; var qry = "";

    //---------------------------------------
         tabNm = "LOS_ENG_RESULT";
    //---------------------------------------
    qry = ExtractPostLosEngResultSql.selectLosEngResultCsv(scheduleId,ruId); println(qry);
    rs = stat.executeQuery(qry); 
    var filePathNm = App.extJavaPath+"/"+tabNm+"_"+scheduleId+"_"+ruId+".dat"; println(filePathNm);
    var pw = new PrintWriter(new File(filePathNm),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    // //---------------------------------------
    //      tabNm = "LOS_BLD_RESULT_DIS"
    // //---------------------------------------
    // qry = ExtractPostLosBldResultDisSql.selectLosBldResultDisCsv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    //
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