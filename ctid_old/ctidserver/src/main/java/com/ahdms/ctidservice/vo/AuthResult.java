package com.ahdms.ctidservice.vo;

public class AuthResult {

	public static final int AUTH_RET_TYPE_DN 		= 1;
	public static final int AUTH_RET_TYPE_PICTRUE 	= 2;
	public static final int AUTH_RET_TYPE_CTID 		= 3;
	public static final int AUTH_RET_TYPE_SECURITY 	= 4;
	
	private int type; 
	private String code;
	private String desc;
	private String reason;
	private String suggest;
	
	public AuthResult(int type, String code, String desc, String reason,String suggest) {
		this.type = type;
		this.code = code;
		this.desc = desc;
		this.reason = reason;
		this.suggest = suggest;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
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
		String typeName = null;
		switch(type) {
			case AUTH_RET_TYPE_DN:
				typeName = "身份信息匹配比对结果";
				break;
			case AUTH_RET_TYPE_PICTRUE:
				typeName = "人像服务比对结果";
				break;
			case AUTH_RET_TYPE_CTID:
				typeName = "网证及认证码匹配比对结果";
				break;
			case AUTH_RET_TYPE_SECURITY:
				typeName = "保留字段";
				break;
			default:
				typeName = "未知类型";
		}
		sb.append(typeName).append(":").append(desc);
		/*if ( !"0".equals(code) ) {
			sb.append(",可能原因:").append(reason)
				.append(",建议:").append(suggest);
		}*/
		
		return sb.toString();
	}
}
