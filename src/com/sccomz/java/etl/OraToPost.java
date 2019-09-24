package com.sccomz.java.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sccomz.java.comm.db.DbConInfo;

import net.pmosoft.pony.dams.jdbc.JdbcInfo;
import net.pmosoft.pony.etl.extract.ExtractTab;
import net.pmosoft.pony.etl.load.LoadTab;

public class OraToPost {

    private static Logger logger = LoggerFactory.getLogger(OraToPost.class);
    
    JdbcInfo jdbcInfo1 = new JdbcInfo();
    JdbcInfo jdbcInfo2 = new JdbcInfo();

    String scheduleId = "";    

    public OraToPost() {
        jdbcInfo1 = new DbConInfo().oraDevInfo();
        jdbcInfo2 = new DbConInfo().postDevInfo();
    }
    
    public OraToPost(String scheduleId) {    	
        jdbcInfo1 = new DbConInfo().oraDevInfo();
        jdbcInfo2 = new DbConInfo().postDevInfo();
        this.scheduleId = scheduleId;
    }
    
    public static void main(String[] args) {
    	OraToPost oraToPost = new OraToPost("8443705");
    	oraToPost.realEtlTab();
    }

    void realEtlQry(){
    	ettTab("SCHEDULE");    
    	ettTab("SCENARIO");
    }

    void realEtlTab(){
    	ettTab("SCHEDULE");    
    	ettTab("SCENARIO");
    	ettTab("ANALYSIS_LIST");
    	ettTab("SITE");        
    	ettTab("SECTOR");      
    	ettTab("DU");          
    	ettTab("RU");          
    	//ettTab("RU_ANTENA");
    }

    void ettQry(String extQry, String tarTabNm){ 
        String insQry = new ExtractTab(jdbcInfo1).selectInsStatToString(tarTabNm, extQry);
        String whereDel = getWhereDel();
        new LoadTab(jdbcInfo2).executeInsertStringToDb(extQry, whereDel, insQry);
    }    
    
    void ettTab(String tabNm){ 
        String selQry = getSelQry(tabNm);
        String insQry = new ExtractTab(jdbcInfo1).selectInsStatToString(tabNm, selQry);
        String whereDel = getWhereDel();
        new LoadTab(jdbcInfo2).executeInsertStringToDb(tabNm, whereDel, insQry);
    }
    
    String getSelQry(String tabNm){
    	String qry = "SELECT * FROM "+tabNm+" WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID='"+scheduleId+"')";
    	logger.info(qry);
    	return qry;
    }

    String getWhereDel(){
    	return " WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID='"+scheduleId+"')";
    }

    String getJoinQry01(String tabNm){
    	String qry = "SELECT * FROM "+tabNm+" WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID='"+scheduleId+"')";
    	logger.info(qry);
    	return qry;
    }
}
