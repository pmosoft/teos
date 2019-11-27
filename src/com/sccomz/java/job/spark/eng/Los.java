//package com.sccomz.java.job.spark.eng;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.io.Serializable;

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
		
	}
	
	void execute(String scheduleId, String queueNm) {
//		SparkSession spark = SparkSession
//				.builder()
//				.master("yarn")
//				.appName()
//				.config("spark.sql.warehouse.dir","/TEOS/warehouse")
//				.getOrCreate();
	}

}
