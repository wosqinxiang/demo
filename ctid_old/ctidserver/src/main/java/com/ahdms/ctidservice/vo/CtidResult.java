/**
 * <p>Title: EsResult.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月23日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

import java.io.Serializable;

import com.ahdms.ap.common.Contents;
import com.ahdms.ctidservice.util.JsonUtils;
import com.ahdms.ctidservice.util.MD5Utils;

/**
 * <p>Title: EsResult</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年7月23日  
 */
public class CtidResult<T> implements Serializable{
	
	private static final long serialVersionUID = -3607797113138186257L;

	private int code;

	private String message;

	private T data;
	
	private String sign;
	
	public CtidResult(){
	}
	
	public CtidResult(int code ,String message,T data){
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public CtidResult(int code ,String message,String sign,T data){
		this.code = code;
		this.message = message;
		this.sign = sign;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static <T> CtidResult<T> ok(T data){
		return new CtidResult<T>(0, "成功", data);
	}
	
	public static <T> CtidResult<T> success(T data,String sign){
		return new CtidResult<T>(0, "成功", sign,data);
	}

	public static <T> CtidResult<String> successSign(T data,String key){
		String string = JsonUtils.bean2Json(data);
		try {
			String md5Sign = MD5Utils.md5(string, key);
			return success(string, md5Sign);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new CtidResult<String>(0, "成功", key,string);
	}
	
	public static <T> CtidResult<T> error(String message){
		return new CtidResult<T>(1, message, null);
	}
	
	public static <T> CtidResult<T> error(int code,String message){
		return new CtidResult<T>(code, message, null);
	}
	
	public static <T> CtidResult<T> tokenerror(String message){
		return new CtidResult<T>(Contents.RETURN_CODE_TOKEN_ERROR, message, null);
	}

}
