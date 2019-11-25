package com.sccomz.java.test;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.sccomz.java.serialize.ByteUtil;

public class SerializeTest {

    public static void main(String[] args) {
        SerializeTest s = new SerializeTest();
        s.array2to1();

        //hexWriteTest06();
        //s.readBin();
        //s.writeBinTest01();
        //s.readBinTest01();
        //s.writeBinTest03();

        //s.array2to1();
        //s.bytePrint();
    }

    static short swap(short x) { return (short)((x << 8) | ((x >> 8) & 0xff)); }
    static char swap(char x) { return (char)((x << 8) | ((x >> 8) & 0xff)); }
    static int swap(int x) { return (int)((swap((short)x) << 16) | swap((short)(x >> 16)) & 0xffff); }
    static long swap(long x) { return (long)(((long)swap((int)(x)) << 32) | ((long)swap((int)(x >> 32)) & 0xffffffffL)); }
    static float swap(float x) { return Float.intBitsToFloat(swap(Float.floatToRawIntBits(x))); }
    static double swap(double x) { return Double.longBitsToDouble(swap(Double.doubleToRawLongBits(x))); }

	public static void hexWriteTest06(){

		int value=255;

        DataOutputStream dos = null;
        try {
        	dos = new DataOutputStream(new FileOutputStream("c:/pony/excel/bin/file03.bin"));
        	dos.write(value);
       } catch (Exception e) { e.printStackTrace(); }
	}
    
    
    
	public static void hexWriteTest04(){
	    int i = Integer.parseUnsignedInt("4294967295");
	    long l = Long.parseUnsignedLong("18446744073709551615");
	    System.out.println("i의 값");
	    System.out.println(i);
	    System.out.println(Integer.toUnsignedString(i));
	    System.out.println(Integer.toUnsignedString(i, 2)); // 2진법

	    System.out.println("l의 값");
	    System.out.println(l);
	    System.out.println(Long.toUnsignedString(l));
	    System.out.println(Long.toUnsignedString(l, 2)); // 2진법

	}
	public static void hexWriteTest03(){


		//int value=1;
		//int newValue = swap(value);
		//System.out.println("big endian value = 0x" + Integer.toHexString(value) + ", little endian value = 0x" + Integer.toHexString(newValue));

		//byte[] x=new byte[100];
		//ByteBuffer buf=ByteBuffer.allocate(100);
		//buf.order(ByteOrder.LITTLE_ENDIAN);
		//buf.position(0);
		//buf.get(x);
		//System.out.println(Integer.toHexString(buf.getInt()));
		////System.out.println(Integer.toHexString(buf.getChar()));
		////System.out.println(Long.toHexString(buf.getLong()));
		////System.out.println(buf.getDouble());


		int value=1;

        DataOutputStream dos = null;
        try {
        	dos = new DataOutputStream(new FileOutputStream("c:/pony/excel/bin/file03.bin"));
        	dos.writeInt(ByteUtil.swap(value));
       } catch (Exception e) { e.printStackTrace(); }
	}


	public static void hexWriteTest02(){

        DataOutputStream dos = null;

        try {
        	dos = new DataOutputStream(new FileOutputStream("c:/pony/excel/bin/file02.bin"));
        	dos.writeInt(1);
       } catch (Exception e) { e.printStackTrace(); }


	}


	public static void hexWriteTest01(){


        File file = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/file01.bin") ;
        FileOutputStream fos = null ;

		int i1 = 1;

		//byte[] INT_MAX = new byte[4];
		//INT_MAX[0] = (byte) 0x00;  INT_MAX[1] = (byte) 0x00; INT_MAX[2] = (byte) 0x00; INT_MAX[3] = (byte) 0x00;

        try {
            fos = new FileOutputStream(file);
            fos.write(i1) ;
        } catch (Exception e) { e.printStackTrace(); }

        if (fos != null) { try { fos.close(); } catch (Exception e) { e.printStackTrace();}}

	}


	public static String byteArrayToHexString(byte[] bytes){

		StringBuilder sb = new StringBuilder();

		for(byte b : bytes){

			sb.append(String.format("%02X", b&0xff));
		}

		return sb.toString();
	}


    void bytePrint(){

		byte byte_min = Byte.MIN_VALUE;
		byte byte_max = Byte.MAX_VALUE;
		short short_min = Short.MIN_VALUE;
		short short_max = Short.MAX_VALUE;
		int int_min = Integer.MIN_VALUE;
		int int_max = Integer.MAX_VALUE;
		long long_min = Long.MIN_VALUE;
		long long_max = Long.MAX_VALUE;
		char char_min = Character.MIN_VALUE;
		char char_max = Character.MAX_VALUE;
		float float_min = Float.MIN_VALUE;
		float float_max = Float.MAX_VALUE;
		double double_min = Double.MIN_VALUE;
		double double_max = Double.MAX_VALUE;

		System.out.println("byte : " + byte_min + " ~ " + byte_max);
		System.out.println("short : " + short_min + " ~ " + short_max);
		System.out.println("int : " + int_min + " ~ " + int_max);
		System.out.println("long : " + long_min + " ~ " + long_max);
		System.out.println("char : " + (int)char_min + " ~ " + (int)char_max);
		System.out.println("float : " + float_min + " ~ " + float_max);
		System.out.println("double : " + double_min + " ~ " + double_max);
    }

    void array2to1(){
      int arr[][] = new int[10][5];
      int arr1[] = new int[arr.length*arr[0].length];

      System.out.println(10*5);
      System.out.println(arr.length*arr[0].length);

      //배열에 값을 넣는다.
      for(int i=0; i<arr.length; i++) {
        for(int j=0; j<arr[i].length; j++) {
          arr[i][j]=(i+1)*(j+1);
        }
      }

      System.out.println("2차원 배열");
      //2차원 배열 출력
      for(int i=0; i<arr.length; i++) {
        for(int j=0; j<arr[i].length; j++) {
          System.out.print("\t"+arr[i][j]);
        }
        System.out.println();
      }

      System.out.println("변환");

      for(int i=0; i<arr.length; i++) {
        for(int j=0; j<arr[i].length; j++) {
          System.out.println("arr[i].length="+arr[i].length);	
          //2차원 배열의 원소를 1차원 배열의 원소로 이동.
          arr1[( i * arr[i].length ) + j ] = arr[i][j];
        }
      }
      //1차원 배열 출력
      System.out.println("1차원 배열");
      for(int i=0; i<arr1.length; i++) {
        if(i%arr[0].length==0) {
          System.out.println();
        }
        System.out.print("\t"+arr1[i]);
      }
    }

    void writeBinTest03(){
        File file = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/file3.bin") ;
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
            File pathFileNm = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/LOS.bin");
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

    void writeBinTest01(){
        File file = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/file.bin") ;
        FileOutputStream fos = null ;
        byte[] buf = new byte[5] ;

        // prepare data.
        buf[0] = 0x01 ;
        buf[1] = 0x02 ;
        buf[2] = 0x03 ;
        buf[3] = 0x04 ;
        buf[4] = 0x05 ;

        try {
            // open file.
            fos = new FileOutputStream(file) ;  // fos = new FileOutputStream("file.bin") ;

            // write file.
            fos.write(buf) ;

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

    void writeBinTest02(){
        File file = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/file2.bin") ;
        FileOutputStream fos = null ;
        byte[] buf = new byte[5] ;

        // prepare data.
        buf[0] = 0x01 ;
        buf[1] = 0x02 ;
        buf[2] = 0x03 ;
        buf[3] = 0x04 ;
        buf[4] = 0x7f ;

        try {
            // open file.
            fos = new FileOutputStream(file) ;  // fos = new FileOutputStream("file.bin") ;

            // write file.
            fos.write(buf) ;

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

    void test01(){
        //byte[] bb = s.floatToByteArray(Float.MAX_VALUE);
        //System.out.println(bb[0]);
        //System.out.println(bb[1]);
        //System.out.println(bb[2]);
        //System.out.println(bb[3]);
        //System.out.println(byteArrayToHexString(bb));
        //
        //bb = s.floatToByteArray(Float.MIN_VALUE);
        //System.out.println(bb[0]);
        //System.out.println(bb[1]);
        //System.out.println(bb[2]);
        //System.out.println(bb[3]);
        //System.out.println(byteArrayToHexString(bb));
        //
        ////binary bytes, 11111111 11111111 11111111 01111111. decimal ... hexidecimal, FFFFFF7F
        //
        //String myString = "BF800000";
        //myString = "42CF43D7";
        //myString = "42CF1F74";
        //
        //Long i = Long.parseLong(myString, 16);
        //Float f = Float.intBitsToFloat(i.intValue());
        //System.out.println(f);
        //System.out.println(Integer.toHexString(Float.floatToIntBits(f)));

        //new Byte4(ByteUtil.floatMax())

    }
    void readBinTest01(){
        File file = new File("d:/fframe/workspace/pony/src/test/java/test/serialize/file.bin") ;
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
