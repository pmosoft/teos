package com.sccomz.java.comm.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbCon {
	
	public JdbcInfo jdbcInfo = new JdbcInfo();
	public Connection conn = null;
	
	public DbCon(String jdbcNm){
		if(jdbcNm.contains("oraDev")){
			this.jdbcInfo = oraDevInfo();
		}

	}
	
    public Connection getConnection() {
        try {
            Class.forName(jdbcInfo.getDriver());
            conn = DriverManager.getConnection(jdbcInfo.getUrl(),jdbcInfo.getUsrId(),jdbcInfo.getUsrPw());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {            
        }
        return conn;
    }  

    public JdbcInfo oraDevInfo(){
    	
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("oraDev");
        jdbcInfo.setDriver("oracle");
    	jdbcInfo.setUrl("jdbc:log4jdbc:oracle:thin:@localhost:9951/IAMLTE");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    	
    }
    
    public JdbcInfo oraLocalInfo(){
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("oraLocal");
        jdbcInfo.setDriver("oracle");
    	jdbcInfo.setUrl("jdbc:log4jdbc:oracle:thin:@localhost:9951/IAMLTE");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    }
    
    public JdbcInfo postDevInfo(){
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("postLocal");
        jdbcInfo.setDriver("postgre");
    	jdbcInfo.setUrl("jdbc:log4jdbc:postgresql://localhost:5432/cellplan");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    }
    
}
