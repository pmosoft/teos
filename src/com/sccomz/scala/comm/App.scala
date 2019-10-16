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
  // in/output pc path
  val pcEtlPath             = "c:/pony/excel";

  // input pc path
  val linuxEtlPath          = "/home/teos/entity";
  val hdfsLinuxEtlPath      = "file:///"+linuxEtlPath;
  val hdfsEtlPath           = "/user/teos/sam/entity";

  // linux output path
  val hdfsParquetEntityPath = "/user/teos/parquet/entity";

  // hdfs output path
  val linuxBinPath          = "/home/teos/data/bin";

  val extJavaPath =
           if(ip=="192.168.0.6")   pcEtlPath
      else if(ip=="192.168.73.71") linuxEtlPath
      else                         pcEtlPath;
  val extSparkPath =
           if(ip=="192.168.0.6")   pcEtlPath
      else if(ip=="192.168.73.71") hdfsLinuxEtlPath
      else                         hdfsLinuxEtlPath;


   //val breakfast =            if (likeEggs) "계란후라이"         else "사과"

  //val spark = SparkSession.builder().appName("Batch").getOrCreate() //.config("spark.master","local")

  /**********************************************************
   * DB 접속 정보
   **********************************************************/
  val dbDriverOra      = "oracle.jdbc.driver.OracleDriver";
  val dbUrlOra         = "jdbc:oracle:thin:@192.168.0.6:1521/ORCL";
           if(ip=="192.168.0.6")   "jdbc:oracle:thin:@192.168.0.6:1521/ORCL"
      else if(ip=="192.168.73.71") "jdbc:oracle:thin:@192.168.0.6:1521/ORCL"
      else if(ip=="150.23.21.44")  "jdbc:oracle:thin:@localhost:9951/IAMLTE"
      else                         "jdbc:oracle:thin:@192.168.0.6:1521/ORCL"
  val dbUserOra        = "cellplan";
  val dbPwOra          = "cell_2012";

  val dbDriverPost     = "org.postgresql.Driver";
  val dbUrlPost        =
           if(ip=="192.168.0.6")   "jdbc:postgresql://localhost:5432/postgres"
      else if(ip=="192.168.73.71") "jdbc:postgresql://185.15.16.156:5432/postgres"
      else if(ip=="150.23.21.44")  "jdbc:postgresql://localhost:55432/postgres"
      else                         "jdbc:postgresql://185.15.16.156:5432/postgres";
    ;
  val dbUserPost       = "postgres";
  val dbPwPost         = "postgres";
  
}