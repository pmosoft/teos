package com.sccomz.scala.test
import scala.collection._
import scala.collection.mutable.ListBuffer
import java.io._
import java.io.File

import com.sccomz.java.serialize.Byte4
import com.sccomz.java.serialize.ByteUtil


object FileTest {

  def main(args: Array[String]): Unit = {
    FileTest03();
  }


  def FileTest03() = {

    val iv = new Byte4(ByteUtil.intZero())
    val bin = Array.ofDim[Byte4](2 * 2)
    for (i <- 0 until 2 * 2 by 1) { bin(i) = iv }

    bin(0*2+0).value = ByteUtil.intZero()
    bin(0*2+1).value = ByteUtil.intZero()
    bin(1*2+0).value = ByteUtil.intZero()
    bin(1*2+1).value = ByteUtil.intZero()
/*

0,0 0*0+0=0 0*1+0=0
1,0 1*0+1=1 1*1+0=1
2,0 2*0+2=2 2*1+0=2
0,1 0*1+0=1 0*1+1=1
1,1 1*1+1=2
2,1 2*1+2=3

0,0 0*0+0=0 0*2
1,0 1*0+1=1 0*2
2,0 2*0+2=2 0*2
0,1 0*1+0=1 1*2
1,1 1*1+1=2 1*2
2,1 2*1+2=4 1*2


0,0 0*0+0=0 0*1+0=0
1,0 1*0+0=0 1*1+0=1
2,0 2*0+0=0 2*1+0=2
0,1 0*1+1=1 0*1+1=1
1,1 1*1+1=2
2,1 2*1+1=3

0,0 0
1,0 1
2,0 2
0,1 3
1,1 4
2,1 5






 * */
  }


  def FileTest02() = {
    var ba = Array.ofDim[Byte](10*5)
    for (i <- 0 until 10*5 by 1) {
      //println(i)
      ba(i) = 1
    }
    val dos = new DataOutputStream(new FileOutputStream("c:/pony/excel/bin/file03.bin"));
    dos.write(ba);
  }



  def FileTest01() = {
    val dos = new DataOutputStream(new FileOutputStream("c:/pony/excel/bin/file02.bin"));
        //fos.write(1);
    dos.writeInt(1);

  }


}

