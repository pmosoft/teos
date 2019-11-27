package com.sccomz.java.job.spark.eng;

import org.apache.hadoop.conf.Configuration;
import org.apache.spark.sql.SparkSession;

public class Los {

	public static void main(String[] args) {
		Los los = new Los();
		los.execute();
	}
	
	void execute() {
		System.out.println("test");
		SparkSession spark = 
		SparkSession.builder().master("yarn").appName("Simple Application").config("spark.sql.warehouse.dir","/TEOS/warehouse").getOrCreate();
		executeSql();
		spark.close();
	}
	
	void executeSql() {
		String objNm = "RESULT_NR_2D_LOS";
		//-----------------------------------------------------------------
		System.out.println(objNm + " 시작");
		//-----------------------------------------------------------------
		String qry = "";
		
		//-----------------------------------------------------------------
		System.out.println("partition 파일 삭제 및 drop table partition");
		//-----------------------------------------------------------------
		Configuration conf = new Configuration();
		
		
	}

}
