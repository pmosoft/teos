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
    	oraToPost.loadPost();
    }

    void extractOra(){
        try {
            stmt1 = conn1.createStatement();

            String query = "SELECT * FROM CELLPLAN.SCENARIO WHERE ROWNUM < 10";
            rs1 = stmt1.executeQuery(query);
            
            ResultSetMetaData rsmd1 = rs1.getMetaData();
            
            for (int i = 0; i < rsmd1.getColumnCount(); i++) {
            	System.out.println(rsmd1.getColumnName(i+1)+"  "+rsmd1.getColumnTypeName(i+1));
            }            
            
            while (rs1.next()) {
                String empno = rs1.getString(1);
                String ename = rs1.getString(2);

                System.out.println(empno + " : " + ename);
            }
        } catch ( Exception e ) { e.printStackTrace(); } finally { DBClose1(); }
    }

    void loadPost(){
        try {
            stmt2 = conn2.createStatement();

            String query = "SELECT * FROM CELLPLAN.SCENARIO LIMIT 10";
            rs2 = stmt2.executeQuery(query);
            
            ResultSetMetaData rsmd2 = rs2.getMetaData();
            
            for (int i = 0; i < rsmd2.getColumnCount(); i++) {
            	System.out.println(rsmd2.getColumnName(i+1)+"  "+rsmd2.getColumnTypeName(i+1));
            }            
            
            while (rs2.next()) {
                String empno = rs2.getString(1);
                String ename = rs2.getString(2);

                System.out.println(empno + " : " + ename);
            }
        } catch ( Exception e ) { e.printStackTrace(); } finally { DBClose2(); }
    }
    
    void DBClose1(){ rs1 = null; stmt1 = null; conn1 = null; }
    void DBClose2(){ rs2 = null; stmt2 = null; conn2 = null; }    
    
}
