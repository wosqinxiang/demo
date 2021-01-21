/**
 * Created on 2020年3月3日 by liuyipin
 */
package com.ahdms.billing.exception;
/**
 * @Title 错误码
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public enum ErrorCode {  

	NULL_OBJ("DMS0001","对象为空"),
	ERROR_ADD_USER("DMS0002","添加用户失败"),
	ADD_USER_USERNAME_NULL("DMS0003","用户名为空"),
	UNKNOWN_ERROR("DMS0999","系统繁忙，请稍后再试....");

	private String value;
	private String desc;

	private ErrorCode(String value, String desc) {
		this.setValue(value);
		this.setDesc(desc);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "[" + this.value + "]" + this.desc;
	} 
}

