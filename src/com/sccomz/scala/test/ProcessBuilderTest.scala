package com.sccomz.scala.test

import java.lang.Runtime
import scala.sys.process._
import java.io.ByteArrayInputStream
import java.io.File

import scala.concurrent._
import ExecutionContext.Implicits.global

object ProcessBuilderTest {

  def main(args: Array[String]): Unit = {
    test01();
  }

  def test01(): Unit = {
    //import scala.sys.process._
    //val cmd = "notepad" // Your command
    //val output = cmd.!! // Captures the output
    //execute();
    //val runtime = Runtime.getRuntime;
    //runtime.exec("notepad");
    //Process("notepad")!
  }

  def test02(): Unit = {
  }

  def test03(): Unit = {
    val p = "sleep 100".run()               // start asynchronously
    val f = Future(blocking(p.exitValue())) // wrap in Future
    val res = try {
                Await.result(f, duration.Duration(2, "sec"))
              } catch {
                case _: TimeoutException => 
                println("TIMEOUT!")
                p.destroy()
                p.exitValue()
              }
  }
  
  def test04(): Unit = {
    val contents = Process("ls").lineStream
    var content2 = Process("sh execPostgre.sh 8460062").lineStream
    var content3 = Process(s"sh /home/icpap/sh/execPostgre.sh 8460062").lineStream
  }


  
}

