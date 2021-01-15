/**
 * <p>Title: AuthCardInputDTO.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月27日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean.dto;

import com.ahdms.ctidservice.bean.basic.AppSdkBasicBean;

/**
 * <p>Title: AuthCardInputDTO</p>  
 * <p>Description: 两项信息+人脸认证</p>  
 * @author qinxiang  
 * @date 2019年9月27日  
 */
public class AuthCardInputDTO extends AppSdkBasicBean {
	
	private String faceData;
	private String userData;
	private String userDataJson;

	public String getFaceData() {
		return faceData;
	}
	public void setFaceData(String faceData) {
		this.faceData = faceData;
	}
	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}

	public String getUserDataJson() {
		return userDataJson;
	}

	public void setUserDataJson(String userDataJson) {
		this.userDataJson = userDataJson;
	}
}
