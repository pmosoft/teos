package com.sccomz.java.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BTest {

	public static void main(String[] args) {
		BTest bt = new BTest();
		bt.binTest();
	}
	
	void binTest() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		File file = new File("C:/Pony/Excel/result/20191018/SYS/5108171/los.bin");
		FileOutputStream fos = null;
		
		ArrayList<Integer> bin = new ArrayList<>();
		
		int count = 0;
		
		try {
			con = PostgreDBManager.connectPost();
			String query = "SELECT x, y, value FROM los ORDER BY y, x";
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			fos = new FileOutputStream(file);
			
			while(rs.next()) {
				int resultValue = rs.getInt("value");
				bin.add(resultValue);
				count++;
				if(count == 4) {
					for (int j = 0; j < bin.size(); j++) {
						fos.write(intToByteArray(bin.get(j)));
					}
					count = 0;
					bin.clear();
				}
			}
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) fos.close();
			} catch (IOException e) {
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

}
