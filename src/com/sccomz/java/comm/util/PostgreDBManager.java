package com.sccomz.java.comm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreDBManager {
	public static Connection connectPost() throws SQLException {
		String url = "jdbc:postgresql://localhost:5432/postgres";
		return DriverManager.getConnection(url, "postgres", "admin");
	}
	
	public static void close(Connection con, Statement stmt, ResultSet rs) {
		try { rs.close(); } catch (Exception e) {}
		try { stmt.close(); } catch (Exception e) {}
		try { con.close(); } catch (Exception e) {}
	}

}