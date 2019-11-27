package com.sccomz.java.job.spark.eng;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;



public class Los {

	public static void main(String[] args) {
		
		
		String scheduleId = "";
	    SparkSession spark = SparkSession
	    	      .builder()
	    	      .appName("Java Spark SQL basic example")
	    	      .config("spark.some.config.option", "some-value")
	    	      .getOrCreate();		

	    
	    executeSql(spark, scheduleId);
		spark.close();
	} 
	
	private static void executeSql(SparkSession spark, String scheduleId) {
		String objNm = "RESULT_NR_2D_LOS";
		try {
			//-----------------------------------------------------------------
			System.out.println(objNm + " 시작");
			//-----------------------------------------------------------------
			String qry = "";
			
			//-----------------------------------------------------------------
			System.out.println("partition 파일 삭제 및 drop table partition");
			//-----------------------------------------------------------------
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			fs.delete(new Path("/TEOS/warehouse/"+ objNm +"/schedule_id="+ scheduleId +""), true);
			qry = "ALTER TABLE "+ objNm +" DROP IF EXISTS PARTITION (schedule_id="+ scheduleId +")";
			
			//-----------------------------------------------------------------
			System.out.println("insert partition table");
			//-----------------------------------------------------------------
			String qry2 = "SELECT DISTINCT RU_ID FROM SCENARIO_NR_RU WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = ${scheduleId})";
			System.out.println(qry2);
            spark.sql(qry).take(100);

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
