package com.sccomz.scala.comm

import java.util.Calendar
import java.text.SimpleDateFormat
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import java.net.InetAddress

object App {

  /**********************************************************
   * 실행머신 IP
   **********************************************************/
  val ip = InetAddress.getLocalHost().getHostAddress();

  /**********************************************************
   * 전역 변수 세팅
   **********************************************************/
  val pcEtlPath             = "c:/pony/excel";
  val linuxEtlPath          = "/disk2/etl";
  val hdfsLinuxEtlPath      = "file:///"+linuxEtlPath;
  val hdfsEtlPath           = "/teos/text";

  val linuxResultPath       = "/disk2/result";


  // linux output path
  //val hdfsParquetEntityPath = "/teos/warehouse";
  val hdfsWarehousePath = "/teos/warehouse";

  // hdfs output path
  val resultPath =
           if(ip=="192.168.0.6")   pcEtlPath
      else if(ip=="192.168.73.71") linuxResultPath
      else if(ip=="150.23.21.44")  pcEtlPath
      else if(ip=="150.23.21.207") pcEtlPath
      else                         linuxResultPath;

  val extJavaPath =
           if(ip=="192.168.0.6")   pcEtlPath
      else if(ip=="192.168.73.71") linuxEtlPath
      else if(ip=="150.23.21.44")  pcEtlPath
      else if(ip=="150.23.21.207") pcEtlPath
      else                         linuxEtlPath;
  val extSparkPath =
           if(ip=="192.168.0.6")   pcEtlPath
      else if(ip=="192.168.73.71") hdfsLinuxEtlPath
      else if(ip=="150.23.21.44")  pcEtlPath
      else if(ip=="150.23.21.207") pcEtlPath
      else                         hdfsLinuxEtlPath;


   //val breakfast =            if (likeEggs) "계란후라이"         else "사과"

  //val spark = SparkSession.builder().appName("Batch").getOrCreate() //.config("spark.master","local")

  /**********************************************************
   * 오라클 DB 접속 정보
   **********************************************************/
  val dbDriverOra      = "oracle.jdbc.driver.OracleDriver";
  val dbUrlOra         =
           if(ip=="192.168.0.6")    "jdbc:oracle:thin:@192.168.0.6:1521/ORCL"
      else if(ip=="192.168.73.71")  "jdbc:oracle:thin:@192.168.0.6:1521/ORCL"
      else if(ip=="150.23.21.44")   "jdbc:oracle:thin:@localhost:9951/IAMLTE"
      else if(ip=="150.23.21.207")  "jdbc:oracle:thin:@localhost:9951/IAMLTE"
      else                          "jdbc:oracle:thin:@150.23.13.165:11521/IAMLTE";
  val dbUserOra        = "cellplan";
  val dbPwOra          = "cell_2012";

  /**********************************************************
   * Postgre DB 접속 정보
   **********************************************************/
  val dbDriverPost     = "org.postgresql.Driver";
  val dbUrlPost        =
           if(ip=="192.168.0.6")    "jdbc:postgresql://localhost:5432/postgres"
      else if(ip=="192.168.73.71")  "jdbc:postgresql://localhost:5432/postgres"
      else if(ip=="150.23.21.44")   "jdbc:postgresql://localhost:54321/postgres"
      else if(ip=="150.23.21.207")  "jdbc:postgresql://localhost:54321/postgres"
      else                          "jdbc:postgresql://185.15.16.156:5432/postgres";
  val dbUserPost       = "postgres";
  val dbPwPost         = "postgres";

  val dbUrlPost1        =
           if(ip=="150.23.21.44")   "jdbc:postgresql://localhost:54321/postgres"
      else if(ip=="150.23.21.207")  "jdbc:postgresql://localhost:54321/postgres"
      else                          "jdbc:postgresql://185.15.16.156:5432/postgres";

  val dbUrlPost2        =
           if(ip=="150.23.21.44")   "jdbc:postgresql://localhost:54322/postgres"
      else if(ip=="150.23.21.207")  "jdbc:postgresql://localhost:54322/postgres"
      else                          "jdbc:postgresql://185.15.16.157:5432/postgres";

  val dbUrlPost3        =
           if(ip=="150.23.21.44")   "jdbc:postgresql://localhost:54323/postgres"
      else if(ip=="150.23.21.207")  "jdbc:postgresql://localhost:54323/postgres"
      else                          "jdbc:postgresql://185.15.16.158:5432/postgres";
  
  val dbUrlPost4        =
           if(ip=="150.23.21.44")   "jdbc:postgresql://localhost:54324/postgres"
      else if(ip=="150.23.21.207")  "jdbc:postgresql://localhost:54324/postgres"
      else                          "jdbc:postgresql://185.15.16.159:5432/postgres";
  
  
  /**********************************************************
   * Hive DB 접속 정보
   **********************************************************/

  val dbDriverHive     = "org.apache.hive.jdbc.HiveDriver";
  val dbUrlHive         =
           if(ip=="192.168.0.6")    "jdbc:hive2://name.dmtech.biz:10000/default"
      else if(ip=="192.168.73.71")  "jdbc:hive2://name.dmtech.biz:10000/default"
      else if(ip=="150.23.21.44")   "jdbc:hive2://localhost:10000"
      else if(ip=="150.23.21.207")  "jdbc:hive2://localhost:10000"
      else                          "jdbc:hive2://185.15.16.151:10000";
  val dbUserHive       = "hive";
  val dbPwHive         = "";

  /**********************************************************
   * Impala DB 접속 정보
   **********************************************************/
  val dbDriverImpala     = "com.cloudera.impala.jdbc41.Driver";
  val dbUrlImpala        =
           if(ip=="192.168.0.6")    "jdbc:impala://name.dmtech.biz:21050/default"
      else if(ip=="192.168.73.71")  "jdbc:impala://name.dmtech.biz:21050/default"
      else if(ip=="150.23.21.44")   "jdbc:hive2://localhost:21050/default"
      else if(ip=="150.23.21.207")  "jdbc:hive2://localhost:21050/default"
      else                          "jdbc:impala://185.15.16.151:21050/default"
  val dbUserImpala       = "hive";
  val dbPwImpala         = "";
}