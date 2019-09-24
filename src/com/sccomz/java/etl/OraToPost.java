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
    String qry = "";    

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
    	//oraToPost.ettTabScenario();
    	oraToPost.realEtlTab();
    	//oraToPost.loadPost();
    }

    void realEtlQry(){
        ettTabScenario();
        ettTabSchedule();
    }
    
    void realEtlTab(){
        ettTabScenario();
        ettTabSchedule();
        ettTabAnalysisList();
        ettTabSite();
        ettTabSector();
        ettTabDu();
        ettTabRu();
        ettTabRuAntena();
    }

    void ettTabScenario(){ 
        String owner  = "CELLPLAN"; String tabNm = "SCENARIO"; String selQry = getSelQry(owner,tabNm);
        String insQry = new ExtractTab(jdbcInfo1).selectInsStatToString(owner, tabNm, selQry);
        System.out.println("111");
        new LoadTab(jdbcInfo2).executeInsertStringToDb(owner, tabNm, insQry);
        System.out.println("222");
    }
    
    void ettTabSchedule(){}
    void ettTabAnalysisList(){}
    void ettTabSite(){}
    void ettTabSector(){}
    void ettTabDu(){}
    void ettTabRu(){}
    void ettTabRuAntena(){}
    
    String getSelQry(String owner, String tabNm){
    	String retQry = "";
    	retQry += "SELECT * FROM "+owner+"."+tabNm+" WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID='"+scheduleId+"')";
    	System.out.println(retQry);
    	return retQry;
    }

    void loadPost(){
    }
    
}
