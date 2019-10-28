package com.sccomz.java.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sccomz.java.serialize.ByteUtil;

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
		File file = new File("C:/Pony/Excel/result/Pathloss/pathlossTest2.bin");
		FileOutputStream fos = null;
        
        try {
        	logger.log(Level.INFO, "========================== 초기화 ===========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// 초기화
        	//----------------------------------------------------------------------------------------------------------------
        	con = HiveDBManager.connectHive();
        	stmt = con.createStatement();

        	int x_bin_cnt = 307, y_bin_cnt = 301;

        	BinValue[][] bin = new BinValue[x_bin_cnt][y_bin_cnt];
       	
			for (int y = 0; y < y_bin_cnt; y++) {
				for (int x = 0; x < x_bin_cnt; x++) {
					bin[x][y] = new BinValue(ByteUtil.floatMax());
				}
			}
			
			logger.log(Level.INFO, "======================== Value 세팅 =========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// VALUE 세팅
        	//----------------------------------------------------------------------------------------------------------------
        	String query2 = "SELECT DISTINCT X_POINT, Y_POINT, PATHLOSS FROM I_RESULT_NR_2D_PATHLOSS WHERE scenario_id = 5104573 ORDER BY X_POINT, Y_POINT";

        	rs = stmt.executeQuery(query2);
        	
        	int x_point = 0, y_point = 0;
        	float pathloss = 0.0f;
        	
			while (rs.next()) {
				x_point = rs.getInt("x_point");
				y_point = rs.getInt("y_point");
				pathloss = rs.getFloat("pathloss");
				bin[x_point][y_point].value = floatToByteArray(pathloss);
			}
			
			logger.log(Level.INFO, "======================= 1차원 배열로 변환 =======================");
			//----------------------------------------------------------------------------------------------------------------
        	// 1차원 배열로 변환
        	//----------------------------------------------------------------------------------------------------------------
			BinValue[] newBin = new BinValue[bin.length * bin[0].length];		// 1차원 배열 bin
			
			for (int i = 0; i < bin.length; i++) {
				for (int j = 0; j < bin[i].length; j++) {
					// 2차원 배열의 원소를 1차원 배열의 원소로 이동.
					newBin[(i * bin[i].length) + j] = bin[i][j];
				}
			}
			
			logger.log(Level.INFO, "======================== 파일 Write =========================");
        	//----------------------------------------------------------------------------------------------------------------
        	// 파일 WRITE
        	//----------------------------------------------------------------------------------------------------------------
			fos = new FileOutputStream(file);

//			for (int y = 0; y < y_bin_cnt; y++) {
//				for (int x = 0; x < x_bin_cnt; x++) {
//					fos.write(bin[x][y].value);
//				}
//			}
			
			for (int x = 0; x < newBin.length; x++) {
				fos.write(newBin[x].value);
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
