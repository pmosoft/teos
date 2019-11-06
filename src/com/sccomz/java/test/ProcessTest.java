package com.sccomz.java.test;

import java.io.IOException;

public class ProcessTest {

    public static void main(String[] args) {
    	processTest02();
    }

	public static void processTest02(){
		//ProcessBuilder pb = new ProcessBuilder("copy d:/imsi/sqldeveloper.zip d:/imsi/sqldeveloper.zip1");
		//ProcessBuilder pb = new ProcessBuilder("d:\\imsi\\run.bat");
		ProcessBuilder pb = new ProcessBuilder("notepad");
		pb.redirectErrorStream(true);
		try {
			Process p = pb.start();
		} catch (IOException e) { e.printStackTrace(); }
	}
    
    
	public static void processTest01(){
		Process process;
		try {
			process = Runtime.getRuntime().exec("copy d:/imsi/sqldeveloper.zip d:/imsi/sqldeveloper.zip1");
			process.getErrorStream().close(); 
			process.getInputStream().close(); 
			process.getOutputStream().close(); 
			try {
				process.waitFor();
			} catch (InterruptedException e) {	e.printStackTrace(); } 
		} catch (IOException e) { e.printStackTrace(); } 
	}
	
}
