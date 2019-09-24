package com.sccomz.java.etl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.pmosoft.pony.comm.util.StringUtil;

public class OraToPostQry {


    public static void main(String[] args) {
    	String qry = ""; 
    	String asCol = ""; String datatype = ""; String col = "";
    	qry = OraToPostQry.schedule10001();
    	
    	ArrayList<String> qryList = new ArrayList<String>(Arrays.asList(qry.split("\n")));
    	//StringUtil.printList(qryList);
    	for (int i = 0; i < qryList.size(); i++) {
    		if(qryList.get(i).matches(".* AS [I|S|D]_.*")){
    			asCol = StringUtil.patternMatch(qryList.get(i)," AS [I|S|D]_[a-zA-Z0-9]*[ ]?.*").trim();
    			col = asCol.substring(5,asCol.length());
    			datatype = asCol.substring(3,4);
    			System.out.println(asCol + "  " + col + "  " +datatype);
    		}
		}
    }	
	
	/*
	 * 분석요청(10001)중인 스케쥴 정보를 조회한다.
	 * */
	public static String schedule10001() {
		String qry = "";
		
        qry+=" SELECT SCHEDULE_ID                             AS I_SCHEDULE_ID                                               \n";
        qry+="      , TYPE_CD                                 AS S_TYPE_CD                                                   \n";
        qry+="      , SCENARIO_ID                             AS I_SCENARIO_ID                                               \n";
        qry+="      , USER_ID                                 AS S_USER_ID                                                   \n";
        qry+="      , PRIORITIZE                              AS S_PRIORITIZE                                                \n";
        qry+="      , PROCESS_CD                              AS S_PROCESS_CD                                                \n";
        qry+="      , PROCESS_MSG                             AS S_PROCESS_MSG                                               \n";
        qry+="      , SCENARIO_PATH                           AS S_SCENARIO_PATH                                             \n";
        qry+="      , TO_CHAR(REG_DT, 'YYYYMMDDHH24MISS')     AS D_REG_DT                                                    \n";
        qry+="      , TO_CHAR(MODIFY_DT, 'YYYYMMDDHH24MISS')  AS D_MODIFY_DT                                                 \n";
        qry+=" FROM  (SELECT ROW_NUMBER() OVER(ORDER BY SCHEDULE_ID ASC, TYPE_CD ASC, PRIORITIZE ASC, RU_CNT ASC) AS ROW_NUM \n";
        qry+="             , SCHEDULE_ID                                                                                     \n";
        qry+="             , TYPE_CD                                                                                         \n";
        qry+="             , SCENARIO_ID                                                                                     \n";
        qry+="             , USER_ID                                                                                         \n";
        qry+="             , PRIORITIZE                                                                                      \n";
        qry+="             , PROCESS_CD                                                                                      \n";
        qry+="             , PROCESS_MSG                                                                                     \n";
        qry+="             , SCENARIO_PATH                                                                                   \n";
        qry+="             , REG_DT                                                                                          \n";
        qry+="             , MODIFY_DT                                                                                       \n";
        qry+="        FROM   SCHEDULE                                                                                        \n";
        //qry+="        WHERE  PROCESS_CD IN ('10001')                                                                         \n";
        //qry+="        AND    TYPE_CD IN ('SC051')                                                                            \n";
        qry+="       )                                                                                                       \n";
        qry+=" WHERE ROW_NUM <= 2                                                                                            \n";
        qry+=" ORDER BY ROW_NUM                                                                                              \n";
		
		return qry;
	}

}
