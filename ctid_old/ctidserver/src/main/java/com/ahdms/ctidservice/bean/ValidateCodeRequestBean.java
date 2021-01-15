/**
 * <p>Title: ValidateCodeRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: ValidateCodeRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 */
public class ValidateCodeRequestBean {
	
	private Integer mode;
	private String bsn;
	private String vCode;
	private String faceData;
	private String idCheck;
	private String appId;
	private String appPackage;
    
    public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getBsn() {
		return bsn;
	}
	public void setBsn(String bsn) {
		this.bsn = bsn;
	}
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getFaceData() {
		return faceData;
	}
	public void setFaceData(String faceData) {
		this.faceData = faceData;
	}
	public String getIdCheck() {
		return idCheck;
	}
	public void setIdCheck(String idCheck) {
		this.idCheck = idCheck;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
