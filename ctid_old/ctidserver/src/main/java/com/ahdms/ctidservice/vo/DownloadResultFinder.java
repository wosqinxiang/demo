package com.ahdms.ctidservice.vo;

import java.util.HashMap;
import java.util.Map;

import com.ahdms.ctidservice.constants.ErrorCodeConstants;

public class DownloadResultFinder {

	private static final Map<String, String> retMap = new HashMap<String, String>();
	
	static {
		retMap.put("0", "下载成功");
		retMap.put("1", "网证已被冻结");
		retMap.put("2", "网证已被注销");
		retMap.put("3", "认证码错误");
		retMap.put("4", "输入参数异常（为空或长度不合法）");
		retMap.put("21", "身份信息不匹配或者身份信息和网证不匹配");
		retMap.put("22", "业务流水号异常");
		retMap.put("23", "网证未开通或者身份信息有误");
		retMap.put("25", "网证不在库中");
		retMap.put("C", "新网证生成异常");
		retMap.put("J", "调用签名服务器异常");
		retMap.put("G", "数据库异常");
		retMap.put("77", "系统异常");
	}
	
	 public static String findDownloadResult(String code) {
	    	return retMap.get(code);
	 }

	public static int findDownloadCode(String result) {
		int code = 1;
		switch (result) {
		case "1":
			code = ErrorCodeConstants.DOWN_CTID_BLOCK_CODE;
			break;
		case "2":
			code = ErrorCodeConstants.DOWN_CTID_CANCEL_CODE;
			break;
		case "3":
			code = ErrorCodeConstants.DOWN_CTID_AUTHCODE_CODE;
			break;
		case "21":
		case "23":
			code = ErrorCodeConstants.DOWN_CTID_IDCARD_CODE;
			break;
		case "25":
			code = ErrorCodeConstants.DOWN_CTID_NONE_CODE;
			break;
		default:
			code = ErrorCodeConstants.DOWN_CTID_DEFAULT_CODE;
			break;
		}
		return code;
	}
}
