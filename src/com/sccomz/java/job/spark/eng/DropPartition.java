package com.sccomz.java.job.spark.eng;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;

public class DropPartition {

	public static void main(String[] args) {
		SparkSession spark = SparkSession.builder().appName("Java Spark SQL basic example")
				.config("spark.some.config.option", "some-value").getOrCreate();
		String scheduleId = args[0];
		dropPath(spark, scheduleId);
		spark.close();
	}
	
	private static void dropPath(SparkSession spark, String scheduleId) {
		String objNm = "RESULT_NR_2D_LOS";
		//-----------------------------------------------------------------
		System.out.println("partition 파일 삭제 및 drop table partition");
		//-----------------------------------------------------------------
		try {
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			fs.delete(new Path("/TEOS/warehouse/'"+ objNm +"/schedule_id="+ scheduleId +"'"), true);
			String qry = "";
			qry = "ALTER TABLE '"+ objNm +" DROP IF EXISTS PARTITION (schedule_id="+ scheduleId +"')";
			spark.sql(qry);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
