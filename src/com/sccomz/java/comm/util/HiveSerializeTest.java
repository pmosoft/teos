package com.sccomz.java.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveSerializeTest {
	
	public class BinValue {
		public byte[] value = new byte[4];
		
		public BinValue(byte[] value) {
			this.value = value;
		}
	}

	public static void main(String[] args) {
		HiveSerializeTest hst = new HiveSerializeTest();
		hst.hiveTest();
	}
	
	void hiveTest() {
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		byte[] INT_MAX = new byte[4];
		INT_MAX[0] = (byte) 0xff;
		INT_MAX[1] = (byte) 0xff;
		INT_MAX[2] = (byte) 0xff;
		INT_MAX[3] = (byte) 0x7f;
        
    	// 파일 WRITE
        File file = new File("C:/Pony/Excel/result/test.bin") ;
        FileOutputStream fos = null ;
        try {
        	con = HiveDBManager.connectHive();
        	stmt = con.createStatement();
        	String query = "SELECT x_bin_cnt, y_bin_cnt FROM scenario_nr_ru WHERE scenario_id = 5103366 AND ru_id = 1012242308";
        	rs = stmt.executeQuery(query);
        	
        	int x_bin_cnt = 0, y_bin_cnt = 0;
			while (rs.next()) {
				x_bin_cnt = rs.getInt("x_bin_cnt");
				y_bin_cnt = rs.getInt("y_bin_cnt");
			}
        	BinValue[][] bin = new BinValue[x_bin_cnt][y_bin_cnt];
        	for (int i = 0; i < x_bin_cnt; i++) {
        		for (int j = 0; j < y_bin_cnt; j++) {
        			bin[i][j] = new BinValue(INT_MAX);
        		}
        	}
        	
        	String query2 = "SELECT X_POINT, Y_POINT, LOS FROM RESULT_NR_2D_LOS ORDER BY X_POINT, Y_POINT";
        	rs2 = stmt.executeQuery(query2);
        	
        	int x_point = 0, y_point = 0, los = 0;
        	
        	while(rs2.next()) {
        		x_point = rs2.getInt("x_point");
        		y_point = rs2.getInt("y_point");
        		los = rs2.getInt("los");
        		// VALUE 세팅
        		bin[x_point][y_point].value = intToByteArray(los);
        	}
        	
			fos = new FileOutputStream(file);
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					fos.write(bin[i][j].value);
				}
			}
        } catch (Exception e) { e.printStackTrace() ;}

        if (fos != null) { try { fos.close() ; } catch (Exception e) { e.printStackTrace(); }}
        
	}
	
	public byte[] intToByteArray(int value) {
		byte[] byteArray = new byte[4];
		byteArray[3] = (byte) (value >> 24);
		byteArray[2] = (byte) (value >> 16);
		byteArray[1] = (byte) (value >> 8);
		byteArray[0] = (byte) (value);
		return byteArray;
	}

    public int byteArrayToInt(byte bytes[]) {
        return ((((int)bytes[3] & 0xff) << 24) |
                (((int)bytes[2] & 0xff) << 16) |
                (((int)bytes[1] & 0xff) << 8) |
                (((int)bytes[0] & 0xff)));
    }

	public byte[] floatToByteArray(float value) {
		int floatValue = Float.floatToIntBits(value);
		return intToByteArray(floatValue);
	}

	public float byteArrayToFloat(byte bytes[]) {
		int value = byteArrayToInt(bytes);
		return Float.intBitsToFloat(value);
	}

}
