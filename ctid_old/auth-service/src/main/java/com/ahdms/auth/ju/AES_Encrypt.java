package com.ahdms.auth.ju;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES_Encrypt {
	
	/** 
     * ���ܺ��� 
     * @param content   ���ܵ�����
     * @param strKey    ��Կ 
     * @return          ���ض������ַ�����
     * @throws Exception 
     */  
    public static byte[] enCrypt(String content,String strKey) throws Exception{  
        KeyGenerator keygen;          
        SecretKey desKey;  
        Cipher c;         
        byte[] cByte;  
        String str = content;  
          
        keygen = KeyGenerator.getInstance("AES");  
      	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      	random.setSeed(strKey.getBytes("utf-8"));
      	keygen.init(128, random);
//        keygen.init(128, new SecureRandom(strKey.getBytes()));  
          
        desKey = keygen.generateKey();        
        c = Cipher.getInstance("AES");  
          
        c.init(Cipher.ENCRYPT_MODE, desKey);  
          
        cByte = c.doFinal(str.getBytes("UTF-8"));         
          
        return cByte;  
    }
    /**������ת��Ϊ16���� 
     * @param buf 
     * @return 
     */  
    public static String parseByte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
            String hex = Integer.toHexString(buf[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
                }  
            sb.append(hex.toUpperCase());  
            }  
        return sb.toString();  
    } 
    
}
