package com.sccomz.java.etl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
    
    OraToPost() {
      conn1 = new DbCon("oraDev").getConnection();
      conn2 = new DbCon("postDev").getConnection();
    }
    
    public static void main(String[] args) {
    	OraToPost oraToPost = new OraToPost();
    	oraToPost.execute();
    }

    void execute(){
        //ResultSetMetaData rsmd = rs.getMetaData();
    	
        try {
            stmt1 = conn1.createStatement();

            String query = "SELECT * FROM CELLPLAN.SCENARIO WHERE ROWNUM < 10";
            rs1 = stmt1.executeQuery(query);
            
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            
            
            while (rs1.next()) {
                String empno = rs1.getString(1);
                String ename = rs1.getString(2);

                System.out.println(empno + " : " + ename);
            }
        } catch ( Exception e ) { e.printStackTrace(); } finally { DBClose1(); }
    	
    	
    }

    void extractOra(){
    	
    	
        //ResultSetMetaData rsmd = rs.getMetaData();
    }

    void DBClose1(){ rs1 = null; stmt1 = null; conn1 = null; }    
}
