package net.pmosoft.pony.comm.db;

public class OracleInfo implements DbmsInfo {

	@Override
	public String dbConn() {
		return "jdbc:oracle:thin:@localhost:1521/ORCL";
	}

	@Override
	public String dbUser() {
		return "AMLS";
	}

	@Override
	public String dbPassword() {
		return "AMLS";
	}

	@Override
	public String dbDriver() {
		return "oracle.jdbc.driver.OracleDriver";
	}
 	
}
