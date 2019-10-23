package com.sccomz.java.comm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveDBManager {
	public static Connection connectHive() throws SQLException {
		String url = "jdbc:hive2://name.dmtech.biz:10000/default";
		return DriverManager.getConnection(url, "hive", "");
	}
	
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try { rs.close(); } catch (Exception e) {}
		try { stmt.close(); } catch (Exception e) {}
		try { con.close(); } catch (Exception e) {}
	}

}
