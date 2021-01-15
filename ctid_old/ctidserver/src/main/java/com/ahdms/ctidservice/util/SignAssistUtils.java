package com.ahdms.ctidservice.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ahdms.api.model.CtidMessage;
import com.ahdms.ctidservice.vo.AuReturn;
import com.ahdms.ctidservice.vo.AuthResultFinder;
import com.ahdms.ctidservice.vo.CDReturn;
import com.ahdms.ctidservice.vo.DownloadResultFinder;

import net.sf.json.JSONObject;

public class SignAssistUtils {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuffer content = new StringBuffer();
		ArrayList keys = new ArrayList(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;

		for (int i = 0; i < keys.size(); ++i) {
			String key = (String) keys.get(i);
			String value = (String) sortedParams.get(key);
			if (areNotEmpty(new String[]{key, value})) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				++index;
			}
		}

		return content.toString();
	}
	
	public static boolean isEmpty(String value) {
		int strLen;
		if (value != null && (strLen = value.length()) != 0) {
			for (int i = 0; i < strLen; ++i) {
				if (!Character.isWhitespace(value.charAt(i))) {
					return false;
				}
			}

			return true;
		} else {
			return true;
		}
	}
	
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values != null && values.length != 0) {
			String[] arr$ = values;
			int len$ = values.length;

			for (int i$ = 0; i$ < len$; ++i$) {
				String value = arr$[i$];
				result &= !isEmpty(value);
			}
		} else {
			result = false;
		}

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getSignCheckContent(Map<String, String> params) {
		if (params == null) {
			return null;
		} else {
			params.remove("sign");
			StringBuffer content = new StringBuffer();
			ArrayList keys = new ArrayList(params.keySet());
			Collections.sort(keys);

			for (int i = 0; i < keys.size(); ++i) {
				String key = (String) keys.get(i);
				String value = (String) params.get(key);
				if(StringUtils.isNotBlank(value)){
					content.append((i == 0 ? "" : "&") + key + "=" + value);
				}
			}
			return content.toString();
		}
	}
	
	public static Map<String, String> getErrorDownloadContent(String code, String errorMsg) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("result", ""+1);
		params.put("code", code);
		params.put("message", errorMsg);
		
		return params;
	}
	
	public static Map<String, String> getErrorAuthContent(String code, String errorMsg) {
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("result", ""+1);
		params.put("code", code);
		params.put("reason", errorMsg);
		
		return params;
	}
	
	public static Map<String, String> getAuthContent(AuReturn auReturn) {
		Map<String, String> params = new HashMap<String, String>();
		
		boolean success = AuthResultFinder.isSuccess(auReturn.getAuResult());
		params.put("result", "" + (success ? 0 : 1));
		params.put("code", auReturn.getAuResult());
		if (!success) {
			params.put("reason", AuthResultFinder.getAuthResultAllString(auReturn.getAuResult()));
		}
		
		return params;
	}
	
	public static Map<String, String> getDownloadContent(CDReturn cdReturn) {
		Map<String, String> params = new HashMap<String, String>();
		int result = 1;
		if("0XXX".equals(cdReturn.getDownloadResult()) || "00XX".equals(cdReturn.getDownloadResult())){
			result = 0;
		}
		params.put("result", ""+result);
		
		CtidMessage ctidMsg = cdReturn.getCtidMessage();
		if (null != ctidMsg) {
			if (!isEmpty(ctidMsg.getCtidIndex())) {
				params.put("ctid_index", ctidMsg.getCtidIndex());
			}
			if (!isEmpty(ctidMsg.getCtidInfo())) {
				params.put("ctid_info", ctidMsg.getCtidInfo());
			}
			if (!isEmpty(ctidMsg.getCtidName())) {
				params.put("ctid_name", ctidMsg.getCtidName());
			}
			params.put("code", ""+ctidMsg.getResult());
			params.put("status", ""+ctidMsg.getCtidStatus());
			if (!"0".equals(ctidMsg.getResult())) {
				params.put("error_msg", DownloadResultFinder.findDownloadResult(ctidMsg.getResult()));
			}
		}
		
		return params;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String mapToJsonString(Map<String, String> params) {
		JSONObject jsonObj = new JSONObject();
		
		ArrayList keys = new ArrayList(params.keySet());

		for (int i = 0; i < keys.size(); ++i) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			jsonObj.put(key, value);
		}
		
		return jsonObj.toString();
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static Map<String, String> jsonStringToMap(String jsonStr) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		
		Iterator ite = jsonObj.keys();
		Map<String, String> params = new HashMap<String, String>();
		while (ite.hasNext()) {
			Object key = ite.next();
			params.put((String)key, (String)jsonObj.get(key));
		}
		
		return params;
	}
}
