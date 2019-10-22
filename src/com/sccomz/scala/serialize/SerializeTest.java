package com.sccomz.scala.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SerializeTest {

	public class BinValue {
		public byte[] value = new byte[4];
		
		public BinValue(byte[] value) {
			this.value = value;
		}
	}	
	
    public static void main(String[] args) {
	
    	
        SerializeTest s = new SerializeTest();
        //s.readBin();
        //s.writeBinTest01();
        //s.readBinTest01();
        //s.writeBinTest03();
        //s.writeBinTest04();
        s.writeBinTest06();
    }

    void writeBinTest06(){
    	// 초기화
        byte[] INT_MAX = new byte[4] ; INT_MAX[0] = (byte) 0xff ;  INT_MAX[1] = (byte) 0xff ; INT_MAX[2] = (byte) 0xff ; INT_MAX[3] = (byte) 0x7f ;
        BinValue[][] bin = new BinValue[10][10];
        for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				bin[i][j] = new BinValue(INT_MAX);
			}
		}
        
        // VALUE 세팅
        bin[0][0].value = intToByteArray(0);
        bin[0][1].value = intToByteArray(1);
        
    	// 파일 WRITE
        File file = new File("c:/pony/excel/file06.bin") ;
        FileOutputStream fos = null ;
        try {
            fos = new FileOutputStream(file);
            for (int i = 0; i < 10; i++) {
    			for (int j = 0; j < 10; j++) {
                    fos.write(bin[i][j].value) ;
    			}
    		}
        } catch (Exception e) { e.printStackTrace() ;}

        if (fos != null) { try { fos.close() ; } catch (Exception e) { e.printStackTrace(); }}        
        
    }    
    
    void writeBinTest01(){
        File file = new File("c:/pony/excel/file.bin") ;
        FileOutputStream fos = null ;
        byte[] buf = new byte[4] ;

        // prepare data.
        //buf[0] = 0x01 ;
        //buf[1] = 0x02 ;
        //buf[2] = 0x03 ;
        //buf[3] = 0x04 ;

        buf[0] = (byte) 0xff ;
        buf[1] = (byte) 0xff ;
        buf[2] = (byte) 0xff ;
        buf[3] = (byte) 0x7f ;
        
        
        try {
            // open file.
            fos = new FileOutputStream(file) ;  // fos = new FileOutputStream("file.bin") ;

            // write file.
            fos.write(buf) ;
            fos.write(buf) ;
            fos.write(Integer.MAX_VALUE) ;
            
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        // close file.
        if (fos != null) {
            // catch Exception here or throw.
            try {
                fos.close() ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void writeBinTest05(){
    	int a[] = {1,2,3,4};
    	Integer b[] = Arrays.stream(a).boxed().toArray(Integer[]::new);
    	
    	System.out.println( a.getClass());
    	System.out.println( Arrays.toString(a));

    	System.out.println( b.getClass());
    	System.out.println( Arrays.toString(b));
    	System.out.printf("%x",b);
    	System.out.printf("%00x",b);
     }    
    
    
    void writeBinTest04(){
    	   // 10진수를 16진수로 출력: 가장 간단한 방법 ㅎㅎ
        System.out.format("%08X%n", 255);  // FF
        System.out.format("%02X%n", 255);  // FF
        System.out.format("%02x%n", 255);  // ff
        System.out.format("%X%n"  , 10);   // A


        String s = String.format("%02X%n", 10); // 16진수 문자열로 변환
        System.out.println("문자열로 만들어서 출력: " + s); // 문자열로 만들어서 출력: 0A


        // 10진수를 16진수로: 불편한 방법
        System.out.println(Integer.toHexString(255)); // ff
        System.out.println(Integer.toHexString(255).toUpperCase()); // 대문자로: FF
        System.out.println(Integer.toHexString(10).toUpperCase()); // A
    }    
    
    void writeBinTest03(){
        File file = new File("c:/pony/excel/file3.bin") ;
        FileOutputStream fos = null ;
        int x = 1;

        try {
            // open file.
            fos = new FileOutputStream(file) ;  // fos = new FileOutputStream("file.bin") ;
            // write file.
            fos.write(intToByteArray(x)) ;
            fos.write(intToByteArray(x)) ;
            //fos.write(x) ;

        } catch (Exception e) {
            e.printStackTrace() ;
        }

        // close file.
        if (fos != null) {
            // catch Exception here or throw.
            try {
                fos.close() ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public  byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[3] = (byte)(value >> 24);
        byteArray[2] = (byte)(value >> 16);
        byteArray[1] = (byte)(value >> 8);
        byteArray[0] = (byte)(value);
        return byteArray;
    }

    public  int byteArrayToInt(byte bytes[]) {
        return ((((int)bytes[3] & 0xff) << 24) |
                (((int)bytes[2] & 0xff) << 16) |
                (((int)bytes[1] & 0xff) << 8) |
                (((int)bytes[0] & 0xff)));
    }

    public byte[] floatToByteArray(float value) {
        int floatValue =  Float.floatToIntBits(value);
        return intToByteArray(floatValue);
    }

    public float byteArrayToFloat(byte bytes[]) {
        int value =  byteArrayToInt(bytes);
        return Float.intBitsToFloat(value);
    }


    void readBin(){
        try {
            File pathFileNm = new File("c:/pony/excel/LOS.bin");
            if(pathFileNm.exists()) System.out.println("aaaa");

            FileInputStream fis = new FileInputStream(pathFileNm);

            int data = 0;
            //while ((data = fis.read()) != -1) {
            while ((data = fis.read()) != -1) {
                System.out.println(data);
            }
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void writeBinTest02(){
    }


    void readBinTest01(){
        File file = new File("c:/pony/excel/file.bin") ;
        FileInputStream fis = null ;
        int data = 0 ;

        if (file.exists() && file.canRead()) {
            try {
                // open file.
                fis = new FileInputStream(file) ;

                // read file.
                while ((data = fis.read()) != -1) {
                    // TODO : use data
                    System.out.println("data : " + data) ;
                }

                // close file.
                fis.close() ;
            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }
    }
}
