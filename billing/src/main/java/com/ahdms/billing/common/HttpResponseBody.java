/**
 * Created on 2020年1月13日 by liuyipin
 */
package com.ahdms.billing.common;

import java.io.Serializable;

/**
 * @Title 返回类
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class HttpResponseBody<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;//成功或失败的响应码，自己定义常量 如：0:成功，1：失败

	private String message;//响应消息，自己定义常量，如：用户名已存在

	private T data;//具体的业务数据

	public HttpResponseBody(){
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setData(T data) {
		this.data = data;
	}
	public T getData() {
		return data;
	}
}


