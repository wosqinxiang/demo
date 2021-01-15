/**
 * <p>Title: MD5Utils.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahdms.ctidservice.bean.DownCtidApplyBean;
import com.ahdms.ctidservice.web.controller.DownCtidController;

import net.sf.json.JSONObject;

/**
 * <p>Title: MD5Utils</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class MD5Utils {
	private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);
	/**
	 * @param text明文
	 * @param key密钥
	 * @return 密文
	 */
	// 带秘钥加密
	public static String md5(String text, String key) throws Exception {
		// 加密后的字符串
		String md5str = DigestUtils.md5Hex(text + key);
		logger.debug("MD5加密后的字符串为:" + md5str);
		return md5str;
	}
	
	public static String md5(Object text, String key) throws Exception {
		// 加密后的字符串
		String md5str = DigestUtils.md5Hex(JSONObject.fromObject(text).toString() + key);
		logger.debug("MD5加密后的字符串为:" + md5str);
		return md5str;
	}

	// 不带秘钥加密
	public static String md5(String text) throws Exception {
		// 加密后的字符串
		String md5str = DigestUtils.md5Hex(text);
		return md5str;
	}

	/**
	 * MD5验证方法
	 * 
	 * @param text明文
	 * @param key密钥
	 * @param md5密文
	 */
	// 根据传入的密钥进行验证
	public static boolean verify(String text, String key, String md5) throws Exception {
		try {
			String md5str = md5(text, key);
			if (md5str.equalsIgnoreCase(md5)) {
				return true;
			}else{
				logger.info(">>>>>MD5验证失败！");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean verify(Object text, String key, String md5) throws Exception {
		String md5str = md5(text, key);
		if (md5str.equalsIgnoreCase(md5)) {
			return true;
		}
		return false;
	}
	
}
