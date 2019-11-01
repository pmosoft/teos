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
with RU AS
(
select b.schedule_id, a.enb_id, a.pci, a.pci_port, a.ru_id,
       a.x_bin_cnt, a.y_bin_cnt,
       int(a.site_startx) div a.resolution * a.resolution as site_startx,
       int(a.site_starty) div a.resolution * a.resolution as site_starty,
       a.resolution
  from scenario_nr_ru a, schedule b
 where b.schedule_id = 8460062
   and a.scenario_id = b.scenario_id
)
select a.schedule_id,
       b.enb_id, b.pci, b.pci_port, b.ru_id,  b.x_bin_cnt, b.y_bin_cnt,
       ((a.rx_tm_xpos div b.resolution * b.resolution) - site_startx) div b.resolution as x_point,
       ((a.rx_tm_ypos div b.resolution * b.resolution) - site_starty) div b.resolution as y_point,
       value
 from RESULT_NR_2D_LOS_RU a, RU b
 where a.schedule_id = 8460062
   and a.schedule_id = b.schedule_id
   and a.ru_id = b.ru_id
   and a.ru_id = 1012242300
   LIMIT 1
"""
}

// LOS에서 Value 세팅할때 사용하는 쿼리
def test1(scheduleId:String) = {
/*
s"""
SELECT DISTINCT X_POINT, Y_POINT, LOS FROM I_RESULT_NR_2D_LOS WHERE scenario_id = 5108566 ORDER BY X_POINT, Y_POINT
""" 
*/
s"""
with RU AS
(
select b.schedule_id, a.enb_id, a.pci, a.pci_port, a.ru_id,
       a.x_bin_cnt, a.y_bin_cnt,
       int(a.site_startx) div a.resolution * a.resolution as site_startx,
       int(a.site_starty) div a.resolution * a.resolution as site_starty,
       int(a.site_endx) div a.resolution * a.resolution as site_endx,
       int(a.site_endy) div a.resolution * a.resolution as site_endy,
       a.resolution
  from scenario_nr_ru a, schedule b
 where b.schedule_id = 8460062
   and a.scenario_id = b.scenario_id
)
select a.schedule_id,
       b.enb_id, b.pci, b.pci_port, b.ru_id,  b.x_bin_cnt, b.y_bin_cnt,
       ((a.rx_tm_xpos div b.resolution * b.resolution) - site_startx) div b.resolution as x_point,
       ((a.rx_tm_ypos div b.resolution * b.resolution) - site_starty) div b.resolution as y_point,
       value
  from RESULT_NR_2D_LOS_RU a, RU b
 where a.schedule_id = 8460062
   and a.schedule_id = b.schedule_id
   and a.ru_id = b.ru_id
   and a.ru_id = 1012242300
   and (a.rx_tm_xpos div b.resolution * b.resolution) between site_startx and site_endx
   and (a.rx_tm_ypos div b.resolution * b.resolution) between site_starty and site_endy
   order by x_point, y_point
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