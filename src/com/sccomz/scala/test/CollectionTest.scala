package com.sccomz.scala.test
import scala.collection._
import scala.collection.mutable.ListBuffer

object CollectionTest {

  def main(args: Array[String]): Unit = {
    mapTest04();
    //listTest01();
    //mapTest03();
  }

  def ArrayTest01() = {

    val a = Array("hello", "world")

    print(a);

    var ab = Array.fill[Byte](5)(0);
    print(ab);

    val row = 5
    val column = 3
    val temp = Array.ofDim[String](row, column)
    val temp2 = Array("a", "b")
    val temp3 = Array.ofDim[Integer](row, column)
    val temp4 = Array.ofDim[Byte](row, column)
  }



  def listTest01() = {

    var aa = "1,2";
    val a = ListBuffer(1,2);
    println(a);
    for(m <- a) {
      println(m);
    }

    var list1 = ListBuffer[String]();
    list1 += "aa";
    list1 += "bb";

    //println(list1.get(1));
    for(m <- list1) {
      println(m);
    }


  }

  def mapTest01() = {
    val m2 = Map (1 -> "one" , 2-> "two")


    val map1 = mutable.Map[String,Int]();
    map1 += ("one" -> 1);
    map1 += ("two" -> 2);
    map1 += ("three" -> 3);

    println(map1)
    println(map1("one"))
  }

  def mapTest02() = {
    var mapAll = mutable.Map[String,mutable.MutableList[String]]();

    var binCntList = mutable.MutableList[String](); binCntList += "10"; binCntList += "20";
    mapAll += ("binCntList" -> binCntList);

    var sectorPath = mutable.MutableList[String](); sectorPath += "/1111";
    mapAll += ("sectorPath" -> sectorPath);

    var ruIdList = mutable.MutableList[String](); ruIdList += "1111"; ruIdList += "1112"; ruIdList += "1113";
    mapAll += ("ruIdList" -> ruIdList);

    for(ruId <- ruIdList) {
      println("ruId="+ruId);
    }


    for(ruId <- mapAll.get("ruIdList")) {
      println("ruId="+ruId);
    }

    println(mapAll.get("ruIdList").size);

    var ruPath = mutable.MutableList[String](); ruPath += "/1111"; ruPath += "/1112";
    mapAll += ("ruPath" -> ruPath);

    println(mapAll.get("binCntList").get(0));
    println(mapAll.get("ruIdList").get(0));


    println(mapAll.get("ruIdList").size)

    var list1 = mutable.MutableList[String]();
    list1 += "aa";
    list1 += "bb";

    println(list1.get(1));
    for(m <- list1) {
      println(m);
    }

  }


  def mapTest03() = {
    var mapAll = mutable.Map[String,ListBuffer[String]]();
    var ruIdList = ListBuffer[String](); ruIdList += "1111"; ruIdList += "1112"; ruIdList += "1113";
    mapAll += (("ruIdList",ruIdList));

    for(ruId <- ruIdList) {
      println("ruId="+ruId);
    }

    for(ruId <- mapAll.get("ruIdList")) {
      println("ruId="+ruId);
    }


  }


  def mapTest04() = {
    var mapAll = mutable.Map[String,String]();
    mapAll += ("1111"->"1122");
    mapAll += ("2111"->"2122");

    println(mapAll.getOrElse("1111",""));
    for(ruId <- mapAll) {
      println("ruId="+ruId._1);
      println("ruId="+ruId._2);
    }

  }


}

