//package com.sccomz.java.comm.util;
//
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class StringUtil {
//
//    public static void main(String[] args) {
//        testSqlTabScript();
//    }
//
//    public static void testSqlTabScript(){
//        System.out.println(inParams("aaa\nbbb"));
//    }
//    /**
//     * 개행문자 제거
//     */
//    public static String delCR(String s) { return s.replaceAll("(\r\n|\r|\n|\n\r)", " "); }
//    
//    /**
//     * "aaa\nbbb"를 'aaa','bbb'로 변형. in 조건문에서 사용
//     */
//    public static String inParams(String str) {
//        //String sql = "aaa\nbbb";
//        //System.out.println(sql);
//
//        String[] words = str.split("\\s");
//        //System.out.println("words=="+words.length);
//
//        String inParams = "";
//        for (int i = 0; i < words.length; i++) {
//            if(i < words.length-1) inParams += "'"+words[i]+"',";
//            else inParams += "'"+words[i]+"'";
//        }
//        //System.out.println(inParams);
//        return inParams;
//    }
//
//
//    public static String padRight(String s, int n) {
//        String result = s;
//        if(n > 0) result = String.format("%1$-" + n + "s", s) + s;
//        return result;
//    }
//    
//    public static String padLeft(String s, int n) {
//        String result = s;
//        if(n > 0) result = String.format("%1$" + n + "s", s) + s;
//        return result;
//    }
//
//    /*
//     *  토큰을 첫문자대문자 Camel로 변환
//     * */
//    public static String tokenToUCamel(String str, String delimeter) {
//
//        //String str = "aa-bb-cc";
//        String[] array = str.split(delimeter);
//        //System.out.println(array.length);
//        String firstUpperCamel = "";
//        for (int i = 0; i < array.length; i++) {
//            //System.out.println(array[i]);
//            //System.out.println(StringUtil.replaceFirstCharUpperCase(array[i]));
//            firstUpperCamel += StringUtil.replaceFirstCharUpperCase(array[i]);
//        }
//        return firstUpperCamel;
//    }
//
//    /*
//     *  토큰을 첫문자소문자 Camel로 변환
//     * */
//    public static String tokenToLCamel(String str, String delimeter) {
//        //String str = "aa-bb-cc";
//        String[] array = str.split(delimeter);
//        //System.out.println(array.length);
//        String firstLowerCamel = "";
//        for (int i = 0; i < array.length; i++) {
//            //System.out.println(array[i]);
//            //System.out.println(StringUtil.replaceFirstCharUpperCase(array[i]));
//            if(i==0) firstLowerCamel += StringUtil.replaceFirstCharLowerCase(array[i]);
//            else firstLowerCamel += StringUtil.replaceFirstCharUpperCase(array[i]);
//        }
//        //System.out.println(firstLowerCamel);
//        return firstLowerCamel;
//    }
//
//
//    /**
//     * 특정 문자열을 첫문자를 대문자로 대체<br>
//     */
//    public static String replaceFirstCharUpperCase(String str){
//        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
//    }
//
//    /**
//     * 특정 문자열을 첫문자를 소문자로 대체<br>
//     */
//    public static String replaceFirstCharLowerCase(String str){
//        return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
//    }
//
//
//    /**
//     * 배열을 받아 연결될 문자열로 연결한다 이때 각 엘레먼트 사이에 구분문자열을 추가한다.
//     */
//    public static String stringJoin(Object aStr[], String padStr){
//    	StringBuffer strBuff = new StringBuffer();
//
//    	int i = aStr.length;
//    	if(i > 0){
//    		strBuff.append(aStr[0]);
//    	}
//    	for(int j=1; j < i; j++){
//    		strBuff.append(padStr);
//    		strBuff.append(aStr[j].toString());
//    	}
//    	return strBuff.toString();
//    }
//
//    /**
//     * 리스트를 sysout으로 출력
//     */
//    public static void printList(ArrayList<String> arrayList){
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//    }
//
//    /**
//     * 정규식에 일치하는 패턴 그룹정보를 리턴
//     */
//    public static String patternMatch(String str, String rule) {
//        Pattern p; Matcher m;
//        p = Pattern.compile(rule); m = p.matcher(str); m.find();
//        return m.group();
//    }
//
//    /**
//     * 
//     */
//    public static String trimNull(String str) {
//        if(str==null) str="";
//        str = str.trim().replace("null", "");
//        return str;
//    }
//
//    /**
//     * 
//     */
//    public static String zeroNull(String str) {
//        if(str==null) str="0";
//        str = str.trim().replace("null", "0");
//        return str;
//    }
//    
//}
