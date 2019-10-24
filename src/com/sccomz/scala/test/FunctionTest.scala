package com.sccomz.scala.test

import scala.collection._

object FunctionTest {

  def main(args: Array[String]): Unit = {
    test02();
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

}

