package com.sccomz.java.schedule.control;

public class RealJobQry {

	/*
	 * 분석요청(10001)중인 스케쥴 정보를 조회한다.
	 * */
	public static String selectSchedule10001() {
		String qry = "";
		
        qry+=" SELECT SCHEDULE_ID                                                                                            \n";
        qry+="      , TYPE_CD                                                                                                \n";
        qry+="      , SCENARIO_ID                                                                                            \n";
        qry+="      , USER_ID                                                                                                \n";
        qry+="      , PRIORITIZE                                                                                             \n";
        qry+="      , PROCESS_CD                                                                                             \n";
        qry+="      , PROCESS_MSG                                                                                            \n";
        qry+="      , SCENARIO_PATH                                                                                          \n";
        qry+="      , TO_CHAR(REG_DT, 'YYYYMMDDHH24MISS')                                                                    \n";
        qry+="      , TO_CHAR(MODIFY_DT, 'YYYYMMDDHH24MISS')                                                                 \n";
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
        qry+="        WHERE  PROCESS_CD IN ('10001')                                                                         \n";
        qry+="        AND    TYPE_CD IN ('SC001','SC051')                                                                    \n";
        qry+="       )                                                                                                       \n";
        qry+=" WHERE ROW_NUM <= 2                                                                                            \n";
        qry+=" ORDER BY ROW_NUM                                                                                              \n";
		
		return qry;
	}

	/*
	 * Bin 개수 확인
	 * */
	public static String selectBinCount(String scenarioId) {
		String qry = "";
        qry+=" SELECT NVL(RESOLUTION     ,  10) AS RESOLUTION  \n";
        qry+="      , NVL(TM_STARTX      , 0.0) AS TM_STARTX   \n";
        qry+="      , NVL(TM_STARTY      , 0.0) AS TM_STARTY   \n";
        qry+="      , NVL(TM_ENDX        , 0.0) AS TM_ENDX     \n";
        qry+="      , NVL(TM_ENDY        , 0.0) AS TM_ENDY     \n";
        qry+=" FROM   SCENARIO                                 \n";
        qry+=" WHERE  SCENARIO_ID = '"+scenarioId+"'           \n";

		return qry;
	}
	
	/*
	 * RU 개수 확인
	 * */
	public static String selectRuCount(String scenarioId) {
		String qry = "";
        qry+=" SELECT COUNT(*) CNT                                            \n";
        qry+="   INTO :SZRU_COUNT                                             \n";
        qry+="   FROM SCENARIO T_SCENARIO                                     \n";
        qry+="       ,DU       T_DU                                           \n";
        qry+="       ,SITE     T_SITE                                         \n";
        qry+="       ,(SELECT SCENARIO_ID                                     \n";
        qry+="               ,ENB_ID                                          \n";
        qry+="               ,PCI                                             \n";
        qry+="               ,PCI_PORT                                        \n";
        qry+="               ,RU_ID                                           \n";
        qry+="               ,MAX(MAKER) AS MAKER                             \n";
        qry+="               ,MAX(REPEATERATTENUATION) AS REPEATERATTENUATION \n";
        qry+="               ,MAX(REPEATERPWRRATIO) AS REPEATERPWRRATIO       \n";
        qry+="               ,MAX(RU_SEQ) AS RU_SEQ                           \n";
        qry+="          FROM RU                                               \n";
        qry+="         WHERE SCENARIO_ID = :SZSCENARIO_ID                     \n";
        qry+="         GROUP BY SCENARIO_ID, ENB_ID, PCI                      \n";
        qry+="                 ,PCI_PORT, RU_ID                               \n";
        qry+="        ) T_RU                                                  \n";
        qry+="  WHERE T_DU.SCENARIO_ID       = T_SCENARIO.SCENARIO_ID         \n";
        qry+="    AND T_DU.SCENARIO_ID       = T_RU.SCENARIO_ID               \n";
        qry+="    AND T_DU.ENB_ID            = T_RU.ENB_ID                    \n";
        qry+="    AND T_RU.SCENARIO_ID       = T_SITE.SCENARIO_ID             \n";
        qry+="    AND T_RU.ENB_ID            = T_SITE.ENB_ID                  \n";
        qry+="    AND T_RU.PCI               = T_SITE.PCI                     \n";
        qry+="    AND T_RU.PCI_PORT          = T_SITE.PCI_PORT                \n";
        qry+="    AND T_RU.RU_ID             = T_SITE.RU_ID                   \n";
        qry+="    AND T_SITE.TYPE            IN ('RU', 'RU_N')                \n";
        qry+="    AND T_SITE.STATUS          = 1                              \n";
        qry+="    AND T_SCENARIO.SCENARIO_ID = '"+scenarioId+"'               \n";
        qry+="  ORDER BY T_RU.ENB_ID, T_RU.PCI, T_RU.PCI_PORT, T_RU.RU_ID     \n";
        
		return qry;
	}

	
}
