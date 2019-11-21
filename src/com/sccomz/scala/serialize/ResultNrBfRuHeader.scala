package com.sccomz.scala.serialize

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession
import java.sql.DriverManager
import com.sccomz.scala.comm.App
import java.sql.Statement

/*
 */

object ResultNrBfRuHeader {

  
def main(args: Array[String]): Unit = {  
  var scheduleId = if (args.length < 1) "" else args(0);
}   

def execute(scheduleId:String) = {
  val spark: SparkSession = SparkSession.builder().master("local[*]").appName(this.getClass.getName).config("spark.sql.warehouse.dir","/TEOS/warehouse").enableHiveSupport().getOrCreate();
  executeSql(spark, scheduleId);
  spark.close();
}
 
def executeSql(spark: SparkSession, scheduleId:String) = {
//var scheduleId = "8463233";
  
var objNm = "RESULT_NR_BF_SCEN_HEADER"
//------------------------------------------------------
    println(objNm + " 시작");
//------------------------------------------------------
var qry = "";
  
//---------------------------------------------------
    println("partiton 파일 삭제 및 drop table partition");
//---------------------------------------------------
val conf = new Configuration()
val fs = FileSystem.get(conf)
fs.delete(new Path(s"""/TEOS/warehouse/${objNm}/schedule_id=${scheduleId}"""),true)
import spark.implicits._
import spark.sql
qry = s"""ALTER TABLE ${objNm} DROP IF EXISTS PARTITION (schedule_id=${scheduleId})"""; sql(qry);

//---------------------------------------------------
    println("insert partition table");
//---------------------------------------------------
   
qry = s"""
with AREA as
(
select a.scenario_id, b.schedule_id,
       a.buildinganalysis3d_resolution as resolution
  from SCENARIO a, SCHEDULE b
 where b.schedule_id = ${scheduleId}
   and a.scenario_id = b.scenario_id
),
HEADERtemp as
(
select a.schedule_id, a.tbd_key,
       b.nx as nx2, b.ny as ny2, b.floorz as floorz2, b.ext_sx as ext_sx2, b.ext_sy as ext_sy2,
       c.nx as nx5, c.ny as ny5, c.floorz as floorz5, c.ext_sx as ext_sx5, c.ext_sy as ext_sy5
  from RESULT_NR_BF_TBDKEY a, BUILDING_3DS_HEADER b, BUILDING_3DS_HEADER_5BY5 c
 where a.schedule_id = ${scheduleId}
   and a.tbd_key = b.tbd_key
   and a.tbd_key = c.tbd_key
)
insert into RESULT_NR_BF_SCEN_HEADER partition(schedule_id=${scheduleId})
SELECT a.tbd_key,
       row_number() over () - 1 as building_index,
       if (b.resolution = 2, nx2, nx5) as nx,
       if (b.resolution = 2, ny2, ny5) as ny,
       if (b.resolution = 2, floorz2, floorz5) as floorz,
       if (b.resolution = 2, ext_sx2, ext_sx5) as ext_sx,
       if (b.resolution = 2, ext_sy2, ext_sy5) as ext_sy
  from HEADERtemp a, AREA b
 where a.schedule_id = b.schedule_id
"""
println(qry); spark.sql(qry).take(100).foreach(println);

}

}
