package com.ahdms.billing.common;

public enum JFResultEnum {
	
	SUCCESS("0","success"),NOCOUNT("1","次数已用完"),
	NOQPS("2","并发已达上限"),UNABLE("3","用户被禁用"),
	EXPIRE("5","服务已过期"),DEFAULT("6","服务出错"),NOACCOUNT("4","信息有误");
	
	private JFResultEnum(String code,String message){
		this.code = code;
		this.message = message;
	}
	
	private String code;
	
	private String message;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean equals(String code) {
		return this.code.equals(code);
	}

}
