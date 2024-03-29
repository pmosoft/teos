package com.sccomz.java.comm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static void main(String[] args) {
        //testDirFileInfo();
    }

    public static void testDelFiles(){
        delFiles("F:/../",".*java");
    }

    @SuppressWarnings("static-access")
	public static void testListToFile(){
        System.out.println("start");
        FileUtil fileUtil = new FileUtil();

        ArrayList<String> arrayList =new ArrayList<String>();
        arrayList.add("aaaa");arrayList.add("bbbb");arrayList.add("cccc");arrayList.add("dddd");

        fileUtil.listToFile(arrayList, "c:///");
        System.out.println("end");
    }

    public static void testDirFileInfo(){
        String pathFileNm = ""; String fileNm = "";
        //pathFileNm = "c:/pony/"; fileNm = ".*.java";
        pathFileNm = "D:/fframe/workspace/asis/src/"; fileNm = ".*cpp";

        dirFileInfoPrint(pathFileNm, fileNm);
    }

    public static void dirFileInfoPrint(String pathFileNm, String fileNm){
        List<HashMap<String, String>> srcInfoList = new ArrayList<HashMap<String, String>>();
        List<HashMap<String, String>> retList = dirFileInfo(pathFileNm, fileNm, srcInfoList);
        for (int i = 0; i < retList.size(); i++) {
            System.out.println(retList.get(i).get("pathFileNm"));
        }
    }

    /*
     * 해당 파일 경로 밑에 모든 파일 및 경로 정보를 출력한다
     * */
    public static List<HashMap<String, String>> dirFileInfo(String pathFileNm, String fileNm, List<HashMap<String, String>> srcInfoList){
        File dir = new File(pathFileNm);
        File[] fileList = dir.listFiles();

        try {
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile() && file.getName().matches(fileNm)) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    //System.out.println(file.getName());
                    map.put("fileNm",  file.getName());
                    map.put("pathFileNm",  file.getPath().replace('\\', '/'));

                    srcInfoList.add(map);

                    //logger.info(file.getPath().replace('\\', '/').replace("D:/workspace/", ""));

                } else if (file.isDirectory()) {
                    dirFileInfo(file.getCanonicalPath().toString(),fileNm, srcInfoList);
                }
            }
        } catch (Exception e) {}

        return srcInfoList;
    }
    
 // 해당 파일 경로 밑에 모든 파일과 폴더들을 삭제
    public static void delFiles2(String pathFileNm){
        File dir = new File(pathFileNm);

        try {
        	if(dir.exists()) {
        		File[] fileList = dir.listFiles();		// 파일리스트 얻어오기
        		
        		for (int i = 0; i < fileList.length; i++) {
        			if(fileList[i].isFile()) {
        				fileList[i].delete();
        				System.out.println("파일이 삭제되었습니다.");
        			} else {
        				delFiles2(fileList[i].getPath());		// 재귀함수 호출
        				System.out.println("폴더가 삭제되었습니다.");
        			}
        			fileList[i].delete();
        		}
        		dir.delete();		// 폴더 삭제
        	}
        	
        } catch (Exception e) {e.printStackTrace();}
    }

    // 해당 파일 경로 밑에 모든 파일들을 삭제(폴더들은 미삭제)
    public static void delFiles(String pathFileNm, String fileNm){
        File dir = new File(pathFileNm);
        File[] fileList = dir.listFiles();

        try {
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                if (file.isFile() && file.getName().matches(fileNm)) {
                    file.delete();
                } else if (file.isDirectory()) {
                    delFiles(file.getCanonicalPath().toString(),fileNm);
                }
            }
        } catch (Exception e) {}
    }

    // 파일을 복사하는 메소드
    public static void fileCopy(String inPathNm, String inFileNm, String outPathNm, String outFileNm) {
        try {

            File dir = new File(outPathNm);
            if(!dir.exists()) dir.mkdirs();

            FileInputStream fis = new FileInputStream(inPathNm+"/"+inFileNm);
            FileOutputStream fos = new FileOutputStream(outPathNm+"/"+outFileNm);

            int data = 0;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
            fis.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일을 이동하는 메소드
    public static void fileMove(String inFileName, String outFileName) {
        try {
            FileInputStream fis = new FileInputStream(inFileName);
            FileOutputStream fos = new FileOutputStream(outFileName);

            int data = 0;
            while ((data = fis.read()) != -1) {
                fos.write(data);
            }
            fis.close();
            fos.close();

            // 복사한뒤 원본파일을 삭제함
            File f = new File(inFileName); f.delete();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 디렉토리의 파일 리스트를 읽는 메소드
    public static List<File> getDirFileList(String dirPath) {
        // 디렉토리 파일 리스트
        List<File> dirFileList = null;

        // 파일 목록을 요청한 디렉토리를 가지고 파일 객체를 생성함
        File dir = new File(dirPath);

        // 디렉토리가 존재한다면
        if (dir.exists()) {
            // 파일 목록을 구함
            File[] files = dir.listFiles();

            // 파일 배열을 파일 리스트로 변화함
            dirFileList = Arrays.asList(files);
        }

        return dirFileList;
    }


    /*
     * 파일을 읽어서 String으로 변환(UTF-8)
     * */
    public static String fileToString(String pathFileNm) {
        return fileToString(pathFileNm,"");
    }

    /*
     * 파일을 읽어서 String으로 변환(인코딩 반영)
     * */
    public static String fileToString(String pathFileNm, String encoding) {
        String src = "";
        try {
            File file = new File(pathFileNm);
            if (file.isFile()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFileNm),encoding));
                while (br.ready()) {
                    String str = br.readLine();
                    if (str != null)
                        src += str + "\n";
                    else
                        break;
                }
                br.close();
            }
        } catch (Exception e) {
            logger.info("" + e.getMessage());
        }

        return src;
    }




    /*
     * 파일을 읽어서 리스트로 반환(UTF-8)
     * */
    public static ArrayList<String> fileToList(String pathFileNm) {
        ArrayList<String> retList = new ArrayList<String>();
        try {
            String encoding = detectEncoding(pathFileNm);
            retList = fileToList(pathFileNm,encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retList;
    }

    /*
     * 파일을 읽어서 리스트로 반환(인코딩 반영)
     * */
    public static ArrayList<String> fileToList(String pathFileNm, String encoding) {
        ArrayList<String> al = new ArrayList<String>();
        String line = "";
        File f = new File(pathFileNm);
        if(!f.exists()) logger.info("No exists File");
        if(!f.canRead()) logger.info("Read protected");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(pathFileNm),encoding));
            while (br.ready()) {
                line = br.readLine();
                al.add(line);
            }
            br.close();
        } catch(Exception e){
            logger.info(""+e);
            //logger.info("encoding="+encoding+":"+line);
        }
        return al;
    }

    /*
     * 리스트를 파일로 생성
     * */
    public static void listToFile(ArrayList<String> arrayList, String pathFileNm) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(pathFileNm,false));

            for (int i = 0; i < arrayList.size(); i++) {
                bw.write(arrayList.get(i).toString()+"\n");
            }
            bw.flush();
            logger.info("writerList : "+pathFileNm);
        } catch (Exception e) {
            try {
                bw.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /*
     * String을 파일로 생성
     * */
    public static void stringToFile(String str, String pathFileNm) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(pathFileNm,false));
            bw.write(str);
            bw.flush();
            logger.info("writerList : "+pathFileNm);
        } catch (Exception e) {
            try {
                bw.flush();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /*
     * 파일 인코인 확인
     * */
    @SuppressWarnings("resource")
    public static String detectEncoding(String pathFileNm) throws Exception {
        try {
            byte[] buf = new byte[4096];
            java.io.FileInputStream fis;
            fis = new FileInputStream(pathFileNm);
            UniversalDetector detector = new UniversalDetector(null);
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            detector.dataEnd();
            String encoding = detector.getDetectedCharset();

            //encoding = encoding.replace("WINDOWS-1252", "EUC-KR");
            if (encoding != null) {
                logger.info("Detected encoding = " + encoding);
            } else {
                logger.info("No encoding detected.");
                encoding = "EUC-KR";
                //encoding = "UTF-8";
                logger.info("encoding="+encoding);
            }
            detector.reset();

            return encoding;
        } catch(Exception e) { e.printStackTrace(); throw e; }

    }

}
