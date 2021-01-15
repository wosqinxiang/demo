package com.ahdms.auth.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthConstant {

	@Value("${ctid.constant.customerNumber}")
	private String customerNumber; // 客户号
	// 应用名称
	@Value("${ctid.constant.appName}")
	private String appName;
	// 读卡控件版本
	@Value("${ctid.constant.cardReaderVersion}")
	private String cardReaderVersion;
	// 活体控件版本
	@Value("${ctid.constant.liveDetectionControlVersion}")
	private String liveDetectionControlVersion;
	// 认证码控件版本
	@Value("${ctid.constant.authCodeControlVersion}")
	private String authCodeControlVersion;

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getCardReaderVersion() {
		return cardReaderVersion;
	}

	public void setCardReaderVersion(String cardReaderVersion) {
		this.cardReaderVersion = cardReaderVersion;
	}

	public String getLiveDetectionControlVersion() {
		return liveDetectionControlVersion;
	}

	public void setLiveDetectionControlVersion(String liveDetectionControlVersion) {
		this.liveDetectionControlVersion = liveDetectionControlVersion;
	}

	public String getAuthCodeControlVersion() {
		return authCodeControlVersion;
	}

	public void setAuthCodeControlVersion(String authCodeControlVersion) {
		this.authCodeControlVersion = authCodeControlVersion;
	}

}
