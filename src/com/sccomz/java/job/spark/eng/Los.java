package com.sccomz.java.job.spark.eng;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.sql.SparkSession;

public class Los {

	public static void main(String[] args) {
		
		String scheduleId = "";
		String ruId = "";
	    SparkSession spark = SparkSession
	    	      .builder()
	    	      .appName("Java Spark SQL basic example")
	    	      .config("spark.some.config.option", "some-value")
	    	      .getOrCreate();
	    executeSql(spark, scheduleId, ruId);
		spark.close();
		
	} 
	
	private static void executeSql(SparkSession spark, String scheduleId, String ruId) {
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
			fs.delete(new Path("/TEOS/warehouse/'"+ objNm +"/schedule_id="+ scheduleId +"'"), true);
			qry = "ALTER TABLE '"+ objNm +" DROP IF EXISTS PARTITION (schedule_id="+ scheduleId +"')";
			
			//-----------------------------------------------------------------
			System.out.println("insert partition table");
			//-----------------------------------------------------------------
			String qry2 = "SELECT DISTINCT RU_ID FROM SCENARIO_NR_RU WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID = '" + scheduleId + "')";
			System.out.println(qry2);
			spark.sql(qry);
			
			String qry3 = "with AREA as\r\n" + 
					"   (\r\n" + 
					"   select a.scenario_id, b.schedule_id,\r\n" + 
					"          a.tm_startx div a.resolution * a.resolution as tm_startx,\r\n" + 
					"          a.tm_starty div a.resolution * a.resolution as tm_starty,\r\n" + 
					"          a.tm_endx div a.resolution * a.resolution as tm_endx,\r\n" + 
					"          a.tm_endy div a.resolution * a.resolution as tm_endy,\r\n" + 
					"          a.resolution\r\n" + 
					"     from SCENARIO a, SCHEDULE b\r\n" + 
					"    where b.schedule_id = '"+ scheduleId +"'\r\n" + 
					"      and a.scenario_id = b.scenario_id\r\n" + 
					"   )\r\n" + 
					"   insert into table RESULT_NR_2D_LOS partition (schedule_id='"+ scheduleId +"')\r\n" + 
					"   select max(AREA.scenario_id) as scenario_id,\r\n" + 
					"          RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution as rx_tm_xpos,\r\n" + 
					"          RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution as rx_tm_ypos,\r\n" + 
					"          (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution as x_point,\r\n" + 
					"          (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution as y_point,\r\n" + 
					"          case when sum(case when RSLT.value = 1 then 1 else 0 end) > 0 then 1 else 0 end as los\r\n" + 
					"     from AREA, RESULT_NR_2D_LOS_RU RSLT\r\n" + 
					"    where RSLT.schedule_id = AREA.schedule_id\r\n" + 
					"      and RSLT.ru_id = '"+ ruId +"'\r\n" + 
					"      and AREA.tm_startx <= RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution and RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution < AREA.tm_endx\r\n" + 
					"      and AREA.tm_starty <= RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution and RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution < AREA.tm_endy\r\n" + 
					"     group by RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution, RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution,\r\n" + 
					"              (RSLT.rx_tm_xpos div AREA.resolution * AREA.resolution - AREA.tm_startx) / AREA.resolution, (RSLT.rx_tm_ypos div AREA.resolution * AREA.resolution - AREA.tm_starty) / AREA.resolution";			
			System.out.println(qry3);
			spark.sql(qry3).take(100);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
