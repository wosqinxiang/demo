package com.ahdms.auth.common;

public class AuthExceptionResult {

	private String code;
	private String desc;
	private String reason;
	private String suggest;
	
	public AuthExceptionResult(String code, String desc, String reason,String suggest) {
		this.code = code;
		this.desc = desc;
		this.reason = reason;
		this.suggest = suggest;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("认证异常，错误码").append(":").append(code)
			.append(",说明：").append(desc)
			.append(",可能原因:").append(reason)
			.append(",建议:").append(suggest);
		return sb.toString();
	}
}
