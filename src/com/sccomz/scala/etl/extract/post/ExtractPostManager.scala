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
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosEngResultDisSql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosEngResultDis1Sql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosBldResultDisSql
import com.sccomz.scala.etl.extract.post.sql.ExtractPostLosBldResultDis1Sql

/*
import com.sccomz.scala.etl.extract.post.ExtractPostManager
ExtractPostManager.extractPostToHadoopCsv("8459967")

 * */
object ExtractPostManager {

  Class.forName(App.dbDriverPost);
  var con:Connection = DriverManager.getConnection(App.dbUrlPost,App.dbUserPost,App.dbPwPost);
  var stat:Statement=con.createStatement();
  var rs:ResultSet = null;
  var tabNm = "";

  def main(args: Array[String]): Unit = {
    extractPostToHadoopCsv("test");
  }

  def extractPostToHadoopCsv(scheduleId:String): Unit = {

    var tabNm = ""; var qry = "";

    //---------------------------------------
         tabNm = "LOS_ENG_RESULT_DIS"
    //---------------------------------------
    qry = ExtractPostLosEngResultDisSql.selectLosEngResultDisCsv(scheduleId); println(qry);
    rs = stat.executeQuery(qry);
    var pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

    // //---------------------------------------
    //      tabNm = "LOS_ENG_RESULT_DIS1"
    // //---------------------------------------
    // qry = ExtractPostLosEngResultDis1Sql.selectLosEngResultDis1Csv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    //
    // //---------------------------------------
    //      tabNm = "LOS_BLD_RESULT_DIS"
    // //---------------------------------------
    // qry = ExtractPostLosBldResultDisSql.selectLosBldResultDisCsv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;
    //
    // //---------------------------------------
    //      tabNm = "LOS_BLD_RESULT_DIS1"
    // //---------------------------------------
    // qry = ExtractPostLosBldResultDis1Sql.selectLosBldResultDis1Csv(scheduleId); println(qry);
    // rs = stat.executeQuery(qry);
    // pw = new PrintWriter(new File(App.extJavaPath+"/"+tabNm+"_"+scheduleId+".dat" ),"UTF-8");
    // while(rs.next()) { pw.write(rs.getString(1)+"\n") }; pw.close;

  }
  def extractPostcleIns(): Unit = {}

}