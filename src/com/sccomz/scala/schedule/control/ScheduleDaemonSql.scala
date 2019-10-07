package com.sccomz.scala.schedule.control

object ScheduleDaemonSql {

  def selectSchedule10001() = {
    """
    SELECT SCHEDULE_ID
         , TYPE_CD
         , SCENARIO_ID
         , USER_ID
         , PRIORITIZE
         , PROCESS_CD
         , PROCESS_MSG
         , SCENARIO_PATH
         , TO_CHAR(REG_DT, 'YYYYMMDDHH24MISS')
         , TO_CHAR(MODIFY_DT, 'YYYYMMDDHH24MISS')
    FROM  (SELECT ROW_NUMBER() OVER(ORDER BY SCHEDULE_ID ASC, TYPE_CD ASC, PRIORITIZE ASC, RU_CNT ASC) AS ROW_NUM
                , SCHEDULE_ID
                , TYPE_CD
                , SCENARIO_ID
                , USER_ID
                , PRIORITIZE
                , PROCESS_CD
                , PROCESS_MSG
                , SCENARIO_PATH
                , REG_DT
                , MODIFY_DT
           FROM   SCHEDULE
           WHERE  PROCESS_CD IN ('10001')
           --AND    TYPE_CD IN ('SC001','SC051')
          )
    WHERE ROW_NUM <= 2
    ORDER BY ROW_NUM
		"""
  }

  def selectBinCount(scheduleId:String) = {
    """
    SELECT NVL(RESOLUTION ,  10) AS RESOLUTION
         , NVL(TM_STARTX  , 0.0) AS TM_STARTX
         , NVL(TM_STARTY  , 0.0) AS TM_STARTY
         , NVL(TM_ENDX    , 0.0) AS TM_ENDX
         , NVL(TM_ENDY    , 0.0) AS TM_ENDY
    FROM   SCENARIO
    WHERE  SCENARIO_ID  = '${scheduleId}'
    """
  }

  def selectRuCount(scheduleId:String) = {
    """
    SELECT COUNT(*) AS RU_CNT
    FROM   SCENARIO T_SCENARIO
         , DU       T_DU
         , SITE     T_SITE
         ,(SELECT SCENARIO_ID
                , ENB_ID
                , PCI
                , PCI_PORT
                , RU_ID
                , MAX(MAKER)               AS MAKER
                , MAX(REPEATERATTENUATION) AS REPEATERATTENUATION
                , MAX(REPEATERPWRRATIO)    AS REPEATERPWRRATIO
                , MAX(RU_SEQ)              AS RU_SEQ
           FROM   RU
           WHERE  SCENARIO_ID = '${scheduleId}'
           GROUP BY SCENARIO_ID, ENB_ID, PCI, PCI_PORT, RU_ID
           ) T_RU
     WHERE T_DU.SCENARIO_ID       = T_SCENARIO.SCENARIO_ID
       AND T_DU.SCENARIO_ID       = T_RU.SCENARIO_ID
       AND T_DU.ENB_ID            = T_RU.ENB_ID
       AND T_RU.SCENARIO_ID       = T_SITE.SCENARIO_ID
       AND T_RU.ENB_ID            = T_SITE.ENB_ID
       AND T_RU.PCI               = T_SITE.PCI
       AND T_RU.PCI_PORT          = T_SITE.PCI_PORT
       AND T_RU.RU_ID             = T_SITE.RU_ID
       AND T_SITE.TYPE            IN ('RU', 'RU_N')
       AND T_SITE.STATUS          = 1
       and T_SCENARIO.SCENARIO_ID = '${scheduleId}'
     ORDER BY T_RU.ENB_ID, T_RU.PCI, T_RU.PCI_PORT, T_RU.RU_ID
    """
  }




def test01() = {
"""
SELECT COUNT(*)
FROM   SCHEDULE
WHERE  PROCESS_CD IN ('10001')
"""
}



}