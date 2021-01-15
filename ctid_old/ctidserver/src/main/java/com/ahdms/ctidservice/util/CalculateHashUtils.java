package com.ahdms.ctidservice.util;

import com.ahdms.ctidservice.common.Base64Utils;
import org.bouncycastle.util.Arrays;

/**
* @ClassName: CalculateHashUtils 
* @Description: TODO(计算hash值公共类) 
*
*/
public class CalculateHashUtils {
	
	
	
	public  static String calculateHash(byte [] filebytes){
		String base64Hash="";
		try {
			//调用硬件计算杂凑值
			SM3Digest sm3 = new SM3Digest();
			byte[] fileByte  =sm3.sm3Digest(filebytes);
			base64Hash =Base64Utils.encode(fileByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return base64Hash;
	}
	
	public  static byte[] calculateHash1(byte [] filebytes){
		String base64Hash="";
		try {
			//调用硬件计算杂凑值
			SM3Digest sm3 = new SM3Digest();
			return sm3.sm3Digest(filebytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(calculateHash1("430527199405017213".getBytes()).length);

		byte[] bytes = calculateHash1("430527199405017213".getBytes());
//		Arrays.cop


	}
	
}
