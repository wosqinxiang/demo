package com.ahdms.ctidservice.bean.dto;

public class DownCtidInfoInputDTO {
	
	private String appId;
	private String vCode;
	private String ctidIndex;
	private String userData;
	private Integer type;
	private String appPackage;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getCtidIndex() {
		return ctidIndex;
	}
	public void setCtidIndex(String ctidIndex) {
		this.ctidIndex = ctidIndex;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getAppPackage() {
		return appPackage;
	}
	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
}
