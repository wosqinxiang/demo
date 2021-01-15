package com.ahdms.ctidservice.bean.dto;

public class WxImageInputDTO {
	
	private String token;
	private Long reqTime;
	private String signResult;
	private String verifyResult;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getReqTime() {
		return reqTime;
	}
	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}
	public String getSignResult() {
		return signResult;
	}
	public void setSignResult(String signResult) {
		this.signResult = signResult;
	}
	public String getVerifyResult() {
		return verifyResult;
	}
	public void setVerifyResult(String verifyResult) {
		this.verifyResult = verifyResult;
	}
	
}
