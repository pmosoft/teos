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
SELECT * 
FROM   SCENARIO_NR_RU
LIMIT 1000
"""
}


}