package com.sccomz.java.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ImpalaTest {

    public static void main(String[] args) {

        TestJdbcBasicImpala impalaBasicJdbc = new TestJdbcBasicImpala();
        impalaBasicJdbc.test01();
    }
}

class TestJdbcBasicImpala {

    public Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;

    TestJdbcBasicImpala() { DBConn(); }

    public void test01(){

        try {
            stmt = conn.createStatement();
            String query = "SELECT '1' empno, 'aaa' ename";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                String empno = rs.getString("empno");
                String ename = rs.getString("ename");

                System.out.println(empno + " : " + ename);
            }
            } catch ( Exception e ) { e.printStackTrace(); } finally { DBClose(); }
    }

    void DBConn(){

        //String DB_URL = "jdbc:impala://name.dmtech.biz:21050/default";
        String DB_URL = "jdbc:impala://localhost:21050/default";
        String DB_USER = "hive"; String DB_PASSWORD = "";

        try {
            //
        	Class.forName("com.cloudera.impala.jdbc41.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch ( Exception e ) { e.printStackTrace(); } finally {}
    }

    void DBClose(){ rs = null; stmt = null; conn = null; }
}

