package com.sccomz.java.comm.db;

import net.pmosoft.pony.dams.jdbc.JdbcInfo;

public class DbConInfo {
	
    public JdbcInfo oraDevInfo(){
    	
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("oraDev");
        jdbcInfo.setDriver("oracle.jdbc.driver.OracleDriver");
    	jdbcInfo.setUrl("jdbc:oracle:thin:@localhost:9951/IAMLTE");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    	
    }
    
    public JdbcInfo oraLocalInfo(){
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("oraLocal");
        jdbcInfo.setDriver("oracle.jdbc.driver.OracleDriver");
    	jdbcInfo.setUrl("jdbc:oracle:thin:@localhost:9951/IAMLTE");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    }
    
    public JdbcInfo postDevInfo(){
    	JdbcInfo jdbcInfo = new JdbcInfo();
    	
    	jdbcInfo.setJdbcNm("postLocal");
        jdbcInfo.setDriver("org.postgresql.Driver");
    	jdbcInfo.setUrl("jdbc:postgresql://localhost:5432/cellplan");
    	jdbcInfo.setUsrId("cellplan");
        jdbcInfo.setUsrPw("cell_2012");

    	return jdbcInfo;    	
    }
    
}
