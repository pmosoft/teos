package com.sccomz.java.comm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.sccomz.scala.comm.App;

public class HiveDBManager {
	public static Connection connectHive() throws SQLException, ClassNotFoundException {
		Class.forName(App.dbDriverHive());
//		String url = "jdbc:hive2://150.23.13.151:10000/default";
		return DriverManager.getConnection(App.dbUrlHive(), App.dbUserHive(), App.dbPwHive());
	}
	
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try { rs.close(); } catch (Exception e) {}
		try { stmt.close(); } catch (Exception e) {}
		try { con.close(); } catch (Exception e) {}
	}

}
