package com.ahdms.ctidservice.util;

import com.ahdms.ctidservice.common.Base64Utils;

public class CtidEncodeUtils {

	/**
	 * 将十六进制字符串转换成byte 数组eg:流水号转换
	 *
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static String encode(String source){
		
		return Base64Utils.encode(hexStringToBytes(source));
	}

}
