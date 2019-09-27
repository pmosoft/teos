package com.sccomz.java.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sccomz.java.comm.db.DbConInfo;

import net.pmosoft.pony.dams.table.TabInfo;
import net.pmosoft.pony.etl.extract.ExtractTab;
import net.pmosoft.pony.etl.load.LoadTab;

public class OraToPost {

    private static Logger logger = LoggerFactory.getLogger(OraToPost.class);
    
    TabInfo tabInfo1 = new TabInfo();
    TabInfo tabInfo2 = new TabInfo();

    String scheduleId = "";    

    public OraToPost() {
        tabInfo1.setJdbcInfo(new DbConInfo().oraDevInfo());
        tabInfo2.setJdbcInfo(new DbConInfo().postDevInfo());
    }
    
    public OraToPost(String scheduleId) {    	
        tabInfo1.setJdbcInfo(new DbConInfo().oraDevInfo());
        tabInfo2.setJdbcInfo(new DbConInfo().postDevInfo());
        this.scheduleId = scheduleId;
    }
    
    public static void main(String[] args) {
    	OraToPost oraToPost = new OraToPost("8443705");
    	oraToPost.realEtlTab();
    	//oraToPost.realEtlQry();
    }

    void realEtlQry(){
    	ettQry(OraQry.schedule10001(),"SCHEDULE");    
    }

    void realEtlTab(){
    	ettTab("SCHEDULE");    
    	ettTab("SCENARIO");
    	ettTab("ANALYSIS_LIST");
    	ettTab("SITE");        
    	ettTab("SECTOR");      
    	ettTab("DU");          
    	ettTab("RU");          
    	ettTab("RU_ANTENA");
    }

    void ettQry(String selQry, String tarTabNm){ 
        String insQry = new ExtractTab(tabInfo1,tabInfo2).selectQryToInsStatToString(selQry, tarTabNm);
        System.out.println("insQry=="+insQry);
        //String whereDel = getWhereDel();
        //new LoadTab(tabInfo2).executeInsertStringToDb(extQry, whereDel, insQry);
    }    
    
    void ettTab(String tabNm){
    	tabInfo1.setOwner(tabInfo1.getJdbcInfo().getUsrId()); tabInfo2.setOwner(tabInfo2.getJdbcInfo().getUsrId());
    	tabInfo1.setTabNm(tabNm); tabInfo2.setTabNm(tabNm);
    	tabInfo1.setChkWhere(true);	tabInfo1.setTxtWhere(getWhereScheduleId());
    	
        String insQry = new ExtractTab(tabInfo1,tabInfo2).executeTabToString();
        String whereDel = getWhereDel();
        new LoadTab(tabInfo2).executeInsertStringToDb(tabNm, whereDel, insQry);
    }
    
    String getWhereScheduleId(){
    	String qry = "WHERE SCENARIO_ID IN (SELECT SCENARIO_ID FROM SCHEDULE WHERE SCHEDULE_ID='"+scheduleId+"')";
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
