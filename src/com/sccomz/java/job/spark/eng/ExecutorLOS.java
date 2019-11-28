package com.sccomz.java.job.spark.eng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.*;

import com.sccomz.java.comm.util.HiveDBManager;
import com.sccomz.scala.comm.App;
import com.sccomz.scala.serialize.MakeBinFileSql;

public class ExecutorLOS {
	
	public static void main(String[] args) {
		ExecutorLOS el = new ExecutorLOS();
		el.runThreads("8463233", "123");
	}
	
	public void runThreads(String scheduleId, String ruId) {
		//ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/2);
		ExecutorService executor= Executors.newFixedThreadPool(100);
		
		// 하이브 연결해서 ScheduleId별로 해당 RuId 갯수 가져오는 쿼리 실행
        /*
         * 1. scheduleId를 이용하여 대상 RUID를 조회하는 쿼리 실행 로직
         * 2. 위 쿼리 결과 갯수 만큼 for-loop를 돌면서 executor.execute(new Los(scheduleId, RUID));
         */
		Connection con = null;
		Statement stat = null;
		ResultSet rs = null;

		ArrayList<String> ruInfo = new ArrayList<String>();

		try {
			//Class.forName(App.dbDriverHive());
			//con = DriverManager.getConnection(App.dbUrlHive(), App.dbUserHive(), App.dbPwHive());
			con = HiveDBManager.connectHive();
			stat = con.createStatement();
			
			String qry = MakeBinFileSql.selectRuIdInfo(scheduleId);
			System.out.println(qry);
			rs = stat.executeQuery(qry);
			
			while(rs.next()) {
				ruInfo.add(rs.getString(0));
			}
			
			for (int i = 0; i < ruInfo.size(); i++) {
				executor.execute(new Los(scheduleId, ruId));
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
        executor.shutdown(); // once you are done with ExecutorService
	}
	
}