package com.ahdms.ctidservice.bean.dto;

public class AuthCtidInfoInputDTO {
	
	private String appId;
	private String faceData;
	private String ctidIndex;
	private String appPackage;
    
    public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getFaceData() {
		return faceData;
	}
	public void setFaceData(String faceData) {
		this.faceData = faceData;
	}
	public String getCtidIndex() {
		return ctidIndex;
	}
	public void setCtidIndex(String ctidIndex) {
		this.ctidIndex = ctidIndex;
	}
	
}
