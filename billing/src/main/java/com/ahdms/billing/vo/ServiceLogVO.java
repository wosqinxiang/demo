package com.ahdms.billing.vo;

public class ServiceLogVO {
	
private String key;   //
	
	private String serviceType;   //服务类型
	
	private String serivceMode;  //服务模式
	
	private String result;   //结果
	
	private String message;  //错误信息
	 
	private String channelMode;  //渠道来源
	
	private String serialNum; //流水号
	
	private String specialCode;
	
	private String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSpecialCode() {
		return specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSerivceMode() {
		return serivceMode;
	}

	public void setSerivceMode(String serivceMode) {
		this.serivceMode = serivceMode;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChannelMode() {
		return channelMode;
	}

	public void setChannelMode(String channelMode) {
		this.channelMode = channelMode;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
}
