package com.sccomz.scala.test

import scala.collection._

object FunctionTest {

  def main(args: Array[String]): Unit = {
    test05();
  }

  def test02(): Unit = {
    var list : mutable.MutableList[String] = test01();
    for(m <- list) {
      println(m);
    }
  }

  def test01(): mutable.MutableList[String] = {
    var list = mutable.MutableList[String]();
    list += "aa";
    list += "bb";
    list;
  }

  def test03(): Unit = {
    var listAll : mutable.MutableList[mutable.MutableList[String]] = test04();
    println(listAll.get(0).get(0));
    println(listAll.get(0).get(1));
    println(listAll.get(1).get(0));
    println(listAll.get(1).get(1));
    for(m <- listAll) {
      println(m);
    }
  }

  def test04(): mutable.MutableList[mutable.MutableList[String]] = {
    var listAll = mutable.MutableList[mutable.MutableList[String]]();
    var list1 = mutable.MutableList[String]();
    list1 += "aa";
    list1 += "bb";
    var list2 = mutable.MutableList[String]();
    list2 += "cc";
    list2 += "dd";
    listAll += list1;
    listAll += list2;
    listAll;
  }

  def test05(): Unit = {
    var listAll : mutable.Map[String,mutable.MutableList[String]] = test06();
    println(listAll.get("binCntList").get(0));
    println(listAll.get("binCntList").get(1));
    for(m <- listAll) {
      println(m);
    }


    var ll = listAll.get("ruIdList");
    for(ruId <- ll) {
      println(ruId);
    }
  }

  def test06(): mutable.Map[String,mutable.MutableList[String]] = {
    var mapAll = mutable.Map[String,mutable.MutableList[String]]();

    var binCntList = mutable.MutableList[String](); binCntList += "10"; binCntList += "20";
    mapAll += ("binCntList" -> binCntList);

    var sectorPath = mutable.MutableList[String](); sectorPath += "/1111";
    mapAll += ("sectorPath" -> sectorPath);

    var ruIdList = mutable.MutableList[String](); ruIdList += "1111"; ruIdList += "1112";
    mapAll += ("ruIdList" -> ruIdList);

    var ruPath = mutable.MutableList[String](); ruPath += "/1111"; ruPath += "/1112";
    mapAll += ("ruPath" -> ruPath);

  }


}

