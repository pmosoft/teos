package com.sccomz.scala.test

import oracle.net.aso.e
import oracle.net.aso.c
import oracle.net.aso.d

object StringTest {

  def main(args: Array[String]): Unit = {
      // bunch of variables to be inserted in the strings
  val a: Int = 192
  val b: Long = 168L
  val c: Byte = 1.toByte
  val d: String = "0F"
  val e: String = "15"


  var aa= Array[Byte](1,2,3);
  print(aa);
/*
    printBytes(ip"192.$b.1.$e")
  printBytes(ip"$a.$b.$c.$e")
  printBytes(hexBytes"C0,A8,01,0F")
  printBytes(hexBytes"C0,$b,$c,0F")
  printBytes(hexBytes"$a,$b,$c,0F")
  printBytes(decBytes"192,$b,1,15")
  printBytes(decBytes"192,168,$c,$e")
  printBytes(decBytes"$a,$b,1,$e")
  printBytes(hexdump"C0A8 010F")
  printBytes(hexdump"$a $b $c $d")
  printBytes(hexdump"C0 $b 01 $d")
*/

  }

  def printBytes(bytes: Array[Byte]) = println(bytes.map(b => "%02X".format(b)).mkString("[",",","]"))





}

