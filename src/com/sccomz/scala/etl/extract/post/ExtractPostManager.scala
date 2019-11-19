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

  }
  def extractPostcleIns(): Unit = {}

}