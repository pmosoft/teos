//package com.sccomz.java.comm.db;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.util.Map;
//
//
//
//public class DbCon {
//
//    public Connection conn = null;
//
//    public Connection getConnection(JdbcInfo jdbcInfo) {
//        try {
//            Class.forName(jdbcInfo.getDriver());
//            conn = DriverManager.getConnection(jdbcInfo.getUrl(),jdbcInfo.getUsrId(),jdbcInfo.getUsrPw());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {            
//        }
// 
//        return conn;
//    }    
//    
//    public Connection getConnection(String dbDriver,String dbConn,String dbUser,String dbPassword) {
//        try {
//            Class.forName(dbDriver);
//            conn = DriverManager.getConnection(dbConn,dbUser,dbPassword);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {            
//        }
// 
//        return conn;
//    }
//
//    public Connection getConnection(Map<String,String> params) {
//        
//        System.out.println("getConnection(params)="+params);
//        try {
//            if(params.get("dbType").equals("SQLITE")){
//                conn = DriverManager.getConnection(params.get("dbConn"));
//            } else {
//                Class.forName(params.get("dbDriver"));
//                conn = DriverManager.getConnection(params.get("dbConn"),params.get("dbUser"),params.get("dbPassword"));
//            }        
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {            
//        }
// 
//        return conn;
//    }
//    
//    
//}
