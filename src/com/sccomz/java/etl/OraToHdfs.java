package com.sccomz.java.etl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sccomz.java.comm.db.DbConInfo;

import net.pmosoft.pony.comm.App;
import net.pmosoft.pony.dams.jdbc.JdbcInfo;
import net.pmosoft.pony.etl.extract.ExtractTab;
import net.pmosoft.pony.etl.load.LoadTab;

public class OraToHdfs {

    private static Logger logger = LoggerFactory.getLogger(OraToHdfs.class);
    
    JdbcInfo jdbcInfo1 = new JdbcInfo();
    JdbcInfo jdbcInfo2 = new JdbcInfo();

    String scheduleId = "";    

    public OraToHdfs() {
        jdbcInfo1 = new DbConInfo().oraDevInfo();
        jdbcInfo2 = new DbConInfo().postDevInfo();
    }
    
    public OraToHdfs(String scheduleId) {    	
        jdbcInfo1 = new DbConInfo().oraDevInfo();
        jdbcInfo2 = new DbConInfo().postDevInfo();
        this.scheduleId = scheduleId;
    }
    
    public static void main(String[] args) {
    	OraToHdfs OraToHdfs = new OraToHdfs("8443705");
    	OraToHdfs.realEtlTab();
    }

    void realEtlQry(){
    	ettQry(OraQry.schedule10001(),"SCHEDULE");    
    }

    void realEtlTab(){
    	ettTab("SCHEDULE");    
    	//ettTab("SCENARIO");
    	//ettTab("ANALYSIS_LIST");
    	//ettTab("SITE");        
    	//ettTab("SECTOR");      
    	//ettTab("DU");          
    	//ettTab("RU");          
    	//ettTab("RU_ANTENA");
    }

    void ettQry(String selQry, String tarTabNm){ 
        String insQry = new ExtractTab(jdbcInfo1,jdbcInfo2).selectQryToInsStatToString(selQry, tarTabNm);
        System.out.println("insQry=="+insQry);
        //String whereDel = getWhereDel();
        //new LoadTab(jdbcInfo2).executeInsertStringToDb(extQry, whereDel, insQry);
    }    
    
    void ettTab(String tabNm){ 
        String selQry = getSelQry(tabNm);
        String pathFileNm = App.excelPath + tabNm+"_"+scheduleId+".dat";
        new ExtractTab(jdbcInfo1,jdbcInfo2).selectTabToFile(selQry, pathFileNm, "|", tabNm);
        //String whereDel = getWhereDel();
        //new LoadTab(jdbcInfo2).executeInsertStringToDb(tabNm, whereDel, insQry);
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
