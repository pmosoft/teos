package com.sccomz.java.etl.extract;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sccomz.java.comm.db.TabInfo;


/*
 * DB의 테이블 데이터를 추출하여 샘파일로 만든다
 * 
 * */
public class ExtractTab {

    private static Logger logger = LoggerFactory.getLogger(ExtractTab.class);

    int extractCnt = 10000;
    int logCnt = 10000;
    
    public ExtractTab() {
    }
    
    public static void main(String[] args) {
        ExtractTab extractTab = new ExtractTab();
    }

    
    public String selectInsStat(Connection conn, String db, String owner, String tabNm, String selQry, String pathFileNm, boolean isFile) {

        logger.info("selectInsStatToFile start");
        
        Map<String, Object> result = new HashMap<String, Object>();

        // DB접속 변수
        Statement stmt = null;
        ResultSet rs = null;
        
        // 파일 변수
        PrintWriter writer = null;

        // 쿼리 변수
        int    colCnt = 0;
        String dataTypeNm  = "";
       
        String colData = "";
        String s01 = "";
        String s02 = "";
        String insQry = "";
        int    rowCnt = 0;
        
        String retQry = "";
        
        try {

            // 파일 변수 초기화
        	if(isFile) writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(pathFileNm)));

            /***********************************************************************************
             *                              insert 문장 생성
             **********************************************************************************/
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selQry);
            s01 = "INSERT INTO "+owner+"."+tabNm+" VALUES (";
            
            /****************************
             * 메타정보 수보
             ****************************/
            ResultSetMetaData rsmd = rs.getMetaData();
            colCnt = rsmd.getColumnCount();
            for (int i = 0; i < colCnt; i++) {
                //System.out.println(rsmd.getColumnName(i+1)+"    "+rsmd.getColumnTypeName(i+1));
            }
            
            //System.out.println("colCnt="+colCnt);
            
            while(rs.next()){
            //if(rs.next()){
                extractCnt++;
                if(extractCnt%logCnt == 0) {
                    logger.info("extractCnt=========="+extractCnt);
                }

                //for (int i = 0; i < tabInfoList.size(); i++) {
                for (int i = 0; i < colCnt; i++) {
                    //dataTypeNm = tabInfoList.get(i).getDataTypeNm().trim().toUpperCase();
                    dataTypeNm = rsmd.getColumnTypeName(i+1).trim().toUpperCase();
                    
                    //System.out.println("tabInfoList.get(i).getDataTypeNm()=="+tabInfoList.get(i).getDataTypeNm()+"   "+rs.getString(i+1));
                    //System.out.println(tabInfo.getJdbcInfo().getDb());
                    colData = rs.getString(i+1);
                    if (dataTypeNm.matches("NUMBER|INT|NUMERIC") || colData==null) {
                        s02 += colData + ",";
                        //System.out.println("insertData1="+insertData);
                    } else if (dataTypeNm.matches("DATE|TIMESTAMP")) {
                        
                        /********************************************************************************
                         * [DATE] DBMS 및 site에 따라 date 형식을 처리하는 것이 상이하므로 별도로 처리요 
                         ********************************************************************************/
                        // 데이트 타입 변형조건일 경우 DBMS의 SQL규칙에 맞게 형변환 처리
                        if     (db.equals("ORACLE")) {colData = "TO_DATE('"+colData+"','YYYY-MM-DD HH24:MI:SS')";}
                        else if(db.equals("ORACLE")) {colData = "TO_DATE('"+colData+"','YYYY-MM-DD HH24:MI:SS')";}
                       
                        s02 += colData + ",";
                    } else {          
                        s02 += "'"+colData + "',";
                    }
                }
                insQry = s01 + s02 + ");"; insQry = insQry.replace(",);",");");
                if(isFile) { writer.println(insQry); } else {retQry += insQry +"\n";}
                insQry = ""; s02 = "";                
            }            
            writer.close();
            result.put("isSuccess", true);
            //if(!isFile) { System.out.println(retQry);}
            logger.info("selectInsStatToFile rowCnt="+rowCnt+" extractCnt="+extractCnt+" end");
            
            return retQry;
            
        } catch ( Exception e ) { 
            result.put("isSuccess", false);
            result.put("errUsrMsg", "시스템 장애가 발생하였습니다");
            result.put("errSysMsg", e.getMessage());
            e.printStackTrace();
        } finally { }
        
        return retQry;
    }
    
}