/**
 * <p>Title: AuthCtidRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: AuthCtidRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class AuthCtidRequestBean extends AppSdkBasicBean {
	
	private String mode;
	private String bsn;
	private String ctidIndex;
	private String userData;
	private String userDataJson;
	private String vCode;
	private String idCheck;
	private String faceData;

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getBsn() {
		return bsn;
	}
	public void setBsn(String bsn) {
		this.bsn = bsn;
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
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getIdCheck() {
		return idCheck;
	}
	public void setIdCheck(String idCheck) {
		this.idCheck = idCheck;
	}
	public String getFaceData() {
		return faceData;
	}
	public void setFaceData(String faceData) {
		this.faceData = faceData;
	}

	public String getUserDataJson() {
		return userDataJson;
	}

	public void setUserDataJson(String userDataJson) {
		this.userDataJson = userDataJson;
	}
}
