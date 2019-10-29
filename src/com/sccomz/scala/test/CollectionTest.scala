package com.sccomz.scala.test

object CollectionTest {

  def main(args: Array[String]): Unit = {
    test01();
  }

  def test01() = {

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

    //updateScheduleBinCnt
    //for(i <- 0 to rowCnt) {}
    //println( "list.size="+list.size );
    //for(m <- list) println(m.get("SCHEDULE_ID").fold("")(_.toString));
    //for((k,v) <- map) printf("key: %s, value: %s\n", k, v)
    //map.foreach { case (key, value) => println(">>> key=" + key + ", value=" + value) }
    
  }
}

