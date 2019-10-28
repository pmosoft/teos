package com.sccomz.java.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteUtil {

    @SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(ByteUtil.class);

    public static void main(String[] args) {
        //testDirFileInfo();
        //0x41200000 10

        byte[] aa = floatToByteArray(10f);
        System.out.println(aa[0]);
        System.out.println(aa[1]);
        System.out.println(aa[2]);
        System.out.println(aa[3]);

        byte[] bb = floatToByteArray(1f);
        System.out.println(bb[0]);
        System.out.println(bb[1]);
        System.out.println(bb[2]);
        System.out.println(bb[3]);
    }

    public static byte[] floatToByteArray(float value) {
        int floatValue =  Float.floatToIntBits(value);
        return intToByteArray(floatValue);
    }

	public static byte[] intMax() {
		byte[] INT_MAX = new byte[4];
		INT_MAX[0] = (byte) 0xff;  INT_MAX[1] = (byte) 0xff; INT_MAX[2] = (byte) 0xff; INT_MAX[3] = (byte) 0x7f;
        return INT_MAX;
	}

	public static byte[] floatMax() {
		byte[] INT_MAX = new byte[4];
		INT_MAX[0] = (byte) 0xff;  INT_MAX[1] = (byte) 0xff; INT_MAX[2] = (byte) 0x7f; INT_MAX[3] = (byte) 0x7f;
        return INT_MAX;
	}

	public static byte[] intZero() {
		byte[] INT_MAX = new byte[4];
		INT_MAX[0] = (byte) 0x00;  INT_MAX[1] = (byte) 0x00; INT_MAX[2] = (byte) 0x00; INT_MAX[3] = (byte) 0x00;
        return INT_MAX;
	}
    public static byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[3] = (byte)(value >> 24);
        byteArray[2] = (byte)(value >> 16);
        byteArray[1] = (byte)(value >> 8);
        byteArray[0] = (byte)(value);
        return byteArray;
    }

    public int byteArrayToInt(byte bytes[]) {
        return ((((int)bytes[3] & 0xff) << 24) |
                (((int)bytes[2] & 0xff) << 16) |
                (((int)bytes[1] & 0xff) << 8) |
                (((int)bytes[0] & 0xff)));
    }


    public float byteArrayToFloat(byte bytes[]) {
        int value =  byteArrayToInt(bytes);
        return Float.intBitsToFloat(value);
    }


}
