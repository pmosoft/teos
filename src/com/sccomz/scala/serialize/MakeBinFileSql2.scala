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

// LOS에서 Value 세팅할때 사용하는 쿼리
def test1(scheduleId:String) = {
s"""
SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5108566 ORDER BY X_POINT, Y_POINT
"""
}

// Pathloss에서 Value 세팅할때 사용하는 쿼리
def test2(scheduleId:String) = {
s"""
SELECT DISTINCT X_POINT, Y_POINT, PATHLOSS FROM I_RESULT_NR_2D_PATHLOSS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT
"""
}


def selectResultNr2dLos(scheduleId:String) = {
s"""
SELECT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS ORDER BY X_POINT, Y_POINT
"""
}



}