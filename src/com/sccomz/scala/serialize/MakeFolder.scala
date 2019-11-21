package com.sccomz.scala.serialize

import java.io._
import java.io.File
import java.sql.DriverManager
import java.sql.Statement

import scala.collection._

import org.apache.spark.internal.Logging
import org.apache.spark.sql.SparkSession

import com.sccomz.java.comm.util.DateUtil
import com.sccomz.java.comm.util.FileUtil
import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil
import com.sccomz.scala.comm.App
import java.sql.ResultSet
import java.sql.Connection

object MakeFolder extends Logging {


  def main(args: Array[String]): Unit = {
    var scheduleId = if (args.length < 1) "" else args(0);
    //makeResultPath("8460062");
  }


  // 폴더 생성 메소드
  def makeResultPath(scheduleId: String): mutable.Map[String, String] = {
    var ruInfo = mutable.Map[String, String]()
    Class.forName(App.dbDriverHive);
    var con = DriverManager.getConnection(App.dbUrlHive, App.dbUserHive, App.dbPwHive);
    var stat: Statement = con.createStatement();
    var qry = MakeBinFileSql.selectBinFilePath(scheduleId); ; println(qry);
    var rs = stat.executeQuery(qry);
    var rowCnt = 1;

    var ruIdList = mutable.MutableList[String]();
    var ruPathList = mutable.MutableList[String]();

    while (rs.next()) {
      // 처음 시작하자마자 폴더 유무 확인 후 있으면 삭제하고 다시 생성.
      if (rowCnt == 1) {
        ruInfo += ("SECTOR_PATH" -> rs.getString("SECTOR_PATH"));

        // 폴더 삭제
        FileUtil.delFiles2(App.resultPath + "/" + DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("SECTOR_PATH"));
        logInfo("Directory Drop Complete!!");
      }

      // RU 정보 생성
      ruInfo += (rs.getString("RU_ID") -> rs.getString("RU_PATH"));

      //      if (rowCnt <= 150) {
      // 폴더 생성
      var dir = new File(App.resultPath, DateUtil.getDate("yyyyMMdd") + "/" + rs.getString("RU_PATH"));
      if (!dir.exists()) dir.mkdirs();
      println(dir);
      //      }
      rowCnt = rowCnt + 1;
    }
    ruInfo;
  }

}