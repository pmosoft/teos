package com.sccomz.scala.schedule.real

object ExecuteJobSql {


def selectScheduleStep(scheduleId:String) = {
s"""
SELECT MAX(TYPE_STEP_CD) AS TYPE_STEP_CD
FROM   SCHEDULE_STEP
WHERE  SCHEDULE_ID = '${scheduleId}'
"""
}

}