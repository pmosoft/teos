package com.sccomz.java.schedule.control;

public class ExecuteJob {

	String scheduleId;

	ExecuteJob(){}
	ExecuteJob(String scheduleId){
		this.scheduleId = scheduleId;
	}

    public static void main(String[] args) {

    }

    void execute(){}

    // 1단계
    void etlOraToPost(){
    }

    // 2단계
    void executePostgre(){
    }

    // 3-1단계
    void etlOraToHdfs(){
    }

    // 3-2단계
    void etlPostToHdfs(){
    }

    // 4단계
    void executeSpark(){
    }

    // 5-1단계
    void etlHdfsToFile(){
    }

    // 5-2단계
    void etlHdfsToOra(){
    }
}
