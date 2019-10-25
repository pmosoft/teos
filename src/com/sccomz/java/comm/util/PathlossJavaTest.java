package com.sccomz.java.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathlossJavaTest {
	
	public class BinValue {
		public byte[] value = new byte[4];
		
		public BinValue(byte[] value) {
			this.value = value;
		}
	}

	public static void main(String[] args) {
		PathlossJavaTest pjt = new PathlossJavaTest();
		pjt.pathlossTest();
	}
	
	void pathlossTest() {
		
		Logger logger = Logger.getLogger(HiveSerializeTest.class.getName());
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		// 파일 WRITE
		File file = new File("C:/Pony/Excel/result/pathlossTest.bin");
		FileOutputStream fos = null;
		
		byte[] INT_MAX = new byte[4];
		INT_MAX[0] = (byte) 0x00;
		INT_MAX[1] = (byte) 0x00;
		INT_MAX[2] = (byte) 0x00;
		INT_MAX[3] = (byte) 0x00;
        
        try {
        	logger.log(Level.INFO, "========================== 초기화 ===========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// 초기화
        	//----------------------------------------------------------------------------------------------------------------
        	con = HiveDBManager.connectHive();
        	stmt = con.createStatement();

        	int x_bin_cnt = 307, y_bin_cnt = 301;

        	BinValue[][] bin = new BinValue[x_bin_cnt][y_bin_cnt];
       	
			for (int i = 0; i < x_bin_cnt; i++) {
				for (int j = 0; j < y_bin_cnt; j++) {
					bin[i][j] = new BinValue(INT_MAX);
				}
			}
			logger.log(Level.INFO, "======================== Value 세팅 =========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// VALUE 세팅
        	//----------------------------------------------------------------------------------------------------------------
        	String query2 = "SELECT DISTINCT X_POINT, Y_POINT, PATHLOSS FROM I_RESULT_NR_2D_PATHLOSS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT";

        	rs = stmt.executeQuery(query2);
        	
        	int x_point = 0, y_point = 0;
        	float pathloss = 0;
        	
			while (rs.next()) {
				x_point = rs.getInt("x_point");
				y_point = rs.getInt("y_point");
				pathloss = rs.getFloat("pathloss");
				bin[x_point][y_point].value = floatToByteArray(pathloss);
			}
        	
			logger.log(Level.INFO, "======================== 파일 Write =========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// 파일 WRITE
        	//----------------------------------------------------------------------------------------------------------------
			fos = new FileOutputStream(file);

				for (int i = 0; i < x_bin_cnt; i++) {
					for (int j = 0; j < y_bin_cnt; j++) {
						fos.write(bin[i][j].value);
					}
				}
			
			logger.log(Level.INFO, "======================== bin 생성 완료 ========================");
	}
        catch (Exception e) { e.printStackTrace() ;}
        if (fos != null) { try { fos.close() ; } catch (Exception e) { e.printStackTrace(); }}
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
