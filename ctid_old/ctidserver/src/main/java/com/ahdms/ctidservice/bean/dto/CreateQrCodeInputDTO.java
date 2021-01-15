package com.ahdms.ctidservice.bean.dto;

public class CreateQrCodeInputDTO {
	
	private String appId;
	
	private String appPackage;
	
	private String ctidIndex;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}

	public String getCtidIndex() {
		return ctidIndex;
	}

	public void setCtidIndex(String ctidIndex) {
		this.ctidIndex = ctidIndex;
	}
	
}
