// package com.sccomz.java.etl.load;
// 
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.InputStreamReader;
// import java.sql.Connection;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.HashMap;
// import java.util.Map;
// 
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// 
// public class LoadTab {
//     
//     private static Logger logger = LoggerFactory.getLogger(ExtractTab.class);
//     
//     TabInfo  tabInfo = new TabInfo();
//     
//     int rowCnt = 0;    
//     int loadCnt = 0;
// 
//     int commitCnt = 0;
//     int commitMaxCnt = 10000;
//     
//     int logCnt = 10000;
//     
//     Connection conn = null;
//     Statement stmt = null;
//     ResultSet rs = null;
//     
//     String qry = "";
//     
//     public LoadTab(){}
//     public LoadTab(TabInfo tabInfo){
//         this.tabInfo = tabInfo;
//         tabInfo.getJdbcInfo().setUrl(tabInfo.getJdbcInfo().getUrl().replace("log4jdbc:", ""));
//         this.conn = new DbCon().getConnection(tabInfo.getJdbcInfo());
//     }
//     
//     public static void main(String[] args) {
//         TabInfo tabInfo = new TabInfo();
//         tabInfo.getJdbcInfo().setUrl("jdbc:oracle:thin:@localhost:1521/orcl");
//         tabInfo.getJdbcInfo().setUsrId("cellplan");
//         tabInfo.getJdbcInfo().setUsrPw("cell_2012");
//         tabInfo.getJdbcInfo().setDb("Oracle");
//         tabInfo.getJdbcInfo().setDriver("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
//         tabInfo.setJdbcNm("CELLPLAN");
//         tabInfo.setOwner("CELLPLAN"); 
//         tabInfo.setTabNm("ANALYSIS_RESULT");
//         //tabInfo.setTabNm("DU");
//         
//         LoadTab loadTab = new LoadTab(tabInfo);
//         loadTab.executeInsertFileToDb();
//     }
//     
//     public void executeInsertFileToDb() {
//         
//         logger.info("executeInsertFileToDb "+tabInfo.getOwner()+"."+tabInfo.getTabNm()+" start");
//         Map<String, Object> result = new HashMap<String, Object>();
//         
//         // 파일 변수
//         String pathFileNm = App.excelPath+tabInfo.getTabNm()+".sql";
//         String encoding = "";
// 
//         // 쿼리 변수
//         String qry = "";
// 
//         
//         File f = new File(pathFileNm);
//         if(!f.exists()) logger.info("No exists File");
//         if(!f.canRead()) logger.info("Read protected");
// 
//         try {
//             
//             stmt = conn.createStatement();
//             
//             /***************************************************************
//              * DELETE 테이블
//              ***************************************************************/
//             qry = "DELETE FROM "+tabInfo.getOwner()+"."+tabInfo.getTabNm(); 
//             //logger.info(qry);
//             stmt.execute(qry);
//             
//             /***************************************************************
//              * INSERT 테이블
//              ***************************************************************/
//             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFileNm)));
//             qry = " ";
//             while (br.ready()) {
//                 loadCnt++;
//                 commitCnt++;
//                 qry += br.readLine()+"\n";
//                 
//                 if(commitCnt%commitMaxCnt == 0) {
//                    stmt.execute(setDbmsSql(qry));
//                    logger.info("loadCnt=========="+loadCnt);
//                    commitCnt = 0;
//                    qry = "";
//                 }
//             }
//             
//             if(commitCnt < commitMaxCnt) {
//                 logger.info("loadCnt=========="+loadCnt);
//                 stmt.execute(setDbmsSql(qry));
//             }            
//             result.put("isSuccess", true);
//             logger.info("executeInsertFileToDb "+tabInfo.getOwner()+"."+tabInfo.getTabNm()+" loadCnt="+loadCnt+" end");
//             
//         } catch ( Exception e ) { 
//             logger.info("\n"+qry);
//             result.put("isSuccess", false);
//             result.put("errUsrMsg", "시스템 장애가 발생하였습니다");
//             result.put("errSysMsg", e.getMessage());
//             e.printStackTrace();
//         } finally { DBClose(); }        
//         
//     }
//     
//     public String setDbmsSql(String qry) {
//         String retQry = "";
// 
//         if(tabInfo.getJdbcInfo().getDb().toUpperCase().equals("ORACLE")){
//             retQry += "BEGIN\n";
//             retQry += qry;
//             retQry += "END;\n";
//             
//         } else {
//             retQry += qry;
//         }
//         
//         return retQry;
//     }
//     
//     void DBClose(){ try { stmt.close(); conn.close();} catch (SQLException e) { e.printStackTrace(); } }    
//         
// }