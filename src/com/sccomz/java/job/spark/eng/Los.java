package com.sccomz.java.job.spark.eng;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.AnalysisException;

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
