package com.sccomz.java.etl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.sccomz.java.comm.db.DbCon;
import com.sccomz.java.comm.db.JdbcInfo;

public class OraToPost {

    JdbcInfo jdbcInfo1 = new JdbcInfo();
    JdbcInfo jdbcInfo2 = new JdbcInfo();

    Connection conn1 = null;
    Statement stmt1 = null;    
    ResultSet rs1 = null;    
        
    Connection conn2 = null;
    Statement stmt2 = null;    
    ResultSet rs2 = null;    
    
    String qry = "";    
    
    OraToPost(){
    	
      jdbcInfo1.setJdbcNm("CELLPLAN");
      jdbcInfo1.setDb("Oracle");
      jdbcInfo1.setUrl("jdbc:log4jdbc:oracle:thin:@localhost:9951/IAMLTE");
      jdbcInfo1.setUsrId("cellplan");
      jdbcInfo1.setUsrPw("cell_2012");
      jdbcInfo1.setDriver("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
      conn1 = new DbCon("oraDev").getConnection();

      jdbcInfo2.setJdbcNm("CELLPLAN");
      jdbcInfo2.setDb("Oracle");
      jdbcInfo2.setUrl("jdbc:log4jdbc:oracle:thin:@localhost:9951/IAMLTE");
      jdbcInfo2.setUsrId("cellplan");
      jdbcInfo2.setUsrPw("cell_2012");
      jdbcInfo2.setDriver("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
      conn2 = new DbCon("oraDev").getConnection();
      
    }
    
    public static void main(String[] args) {}

    void execute(){
    	
    	
    	
    }


}
