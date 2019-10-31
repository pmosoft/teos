package com.sccomz.scala.test

object StringTest2 {
  def main(args: Array[String]): Unit = {
    var sql = "SELECT A.SCHEDULE_ID, A.SCENARIO_ID, A.REG_DT, A.PROCESS_MSG, B.SCENARIO_NM, A.BIN_X_CNT, A.BIN_Y_CNT, A.SCENARIO_PATH";
    sql += " FROM SCHEDULE A, SCENARIO B WHERE A.SCENARIO_ID = B.SCENARIO_ID AND A.SCHEDULE_ID = 8460970 AND A.SCENARIO_ID = 5108566";
    sql += " ORDER BY REG_DT DESC";
    println(sql);
  }
}