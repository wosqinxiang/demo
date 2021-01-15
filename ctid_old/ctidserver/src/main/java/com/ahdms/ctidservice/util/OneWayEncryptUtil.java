/**
 * Created on 2019年12月2日 by luotao
 */
package com.ahdms.ctidservice.util;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2017</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author luotao
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class OneWayEncryptUtil {
	
	public static String encrypt( String aesKeyStr,String content) throws Exception{
		if(StringUtils.isBlank(content)){
			return null;
		}
        //将Base64编码后的Server公钥转换成PublicKey对象
//        PublicKey serverPublicKey = RSAUtil.string2PublicKey(publicKeyStr);
        //每次都随机生成AES秘钥
//        String aesKeyStr = AESUtil.genKeyAES();
        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
        //用Server公钥加密AES秘钥
//        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
        //用AES秘钥加密请求内容
        byte[] encryptRequest = AESUtil.encryptAES(content.getBytes(), aesKey);
        
//        JSONObject result = new JSONObject();
//        result.put("ak", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
//        result.put("ct", RSAUtil.byte2Base64(encryptRequest).replaceAll("\r\n", ""));
        return RSAUtil.byte2Base64(encryptRequest).replaceAll("\r\n", "");
    }
	

//  //加密
//    public static String encrypt(String publicKeyStr, String content) throws Exception{
//        //将Base64编码后的Server公钥转换成PublicKey对象
//        PublicKey serverPublicKey = RSAUtil.string2PublicKey(publicKeyStr);
//        //每次都随机生成AES秘钥
//        String aesKeyStr = AESUtil.genKeyAES();
//        SecretKey aesKey = AESUtil.loadKeyAES(aesKeyStr);
//        //用Server公钥加密AES秘钥
//        byte[] encryptAesKey = RSAUtil.publicEncrypt(aesKeyStr.getBytes(), serverPublicKey);
//        //用AES秘钥加密请求内容
//        byte[] encryptRequest = AESUtil.encryptAES(content.getBytes(), aesKey);
//        
//        JSONObject result = new JSONObject();
//        result.put("ak", RSAUtil.byte2Base64(encryptAesKey).replaceAll("\r\n", ""));
//        result.put("ct", RSAUtil.byte2Base64(encryptRequest).replaceAll("\r\n", ""));
//        return result.toString();
//    }
    
  //解密
    public static String decrypt(String privateKey , String content) throws Exception{
        JSONObject result = JSONObject.fromObject(content);
        String encryptAesKeyStr = (String) result.get("ak");
        String encryptContent = (String) result.get("ct");
        
        //将Base64编码后的Server私钥转换成PrivateKey对象
        PrivateKey serverPrivateKey = RSAUtil.string2PrivateKey(privateKey);
        //用Server私钥解密AES秘钥
        byte[] aesKeyBytes = RSAUtil.privateDecrypt(RSAUtil.base642Byte(encryptAesKeyStr), serverPrivateKey);
        SecretKey aesKey = AESUtil.loadKeyAES(new String(aesKeyBytes));
        //用AES秘钥解密请求内容
        byte[] request = AESUtil.decryptAES(RSAUtil.base642Byte(encryptContent), aesKey);
        
        JSONObject result2 = new JSONObject();
        result2.put("ak", new String(aesKeyBytes));
        result2.put("ct", new String(request));
        return result2.toString();
    }
}

