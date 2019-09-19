package com.sccomz.java.comm.db;

public class JdbcInfo {

	String jdbcNm   = "";
    String db       = "";
    String driver   = "";
    String url      = "";
    String usrId    = "";
    String usrPw    = "";
    
	public String getJdbcNm() {
		return jdbcNm;
	}
	public void setJdbcNm(String jdbcNm) {
		this.jdbcNm = jdbcNm;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrPw() {
		return usrPw;
	}
	public void setUsrPw(String usrPw) {
		this.usrPw = usrPw;
	}
}
