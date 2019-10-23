package com.sccomz.scala.serialize

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.io._
import java.io.File;

import com.sccomz.scala.comm.App


object MakeBinFileSql2 {

def main(args: Array[String]): Unit = {
}

def selectScenarioNrRu(scheduleId:String) = {
s"""
SELECT * FROM SCENARIO_NR_RU
"""
}

def test1(scheduleId:String) = {
s"""
SELECT DISTINCT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS WHERE scenario_id = 5108566 ORDER BY X_POINT, Y_POINT
"""
}

def selectResultNr2dLos(scheduleId:String) = {
s"""
SELECT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS ORDER BY X_POINT, Y_POINT
"""
}



}