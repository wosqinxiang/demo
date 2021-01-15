/**
 * <p>Title: AuthCtidValidDateRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月28日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: AuthCtidValidDateRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月28日  
 */
public class AuthCtidValidDateRequestBean {
	
	private String ctidIndex;
	private String appId;
	private String appPackage;
    
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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
