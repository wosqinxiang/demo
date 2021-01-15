/**
 * <p>Title: CreateCodeRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: CreateCodeRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class CreateCodeRequestBean {
	
	private String checkData;
	private String bsn;
	private String appId;
	private String appPackage;
    
    public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
	public String getCheckData() {
		return checkData;
	}
	public void setCheckData(String checkData) {
		this.checkData = checkData;
	}
	public String getBsn() {
		return bsn;
	}
	public void setBsn(String bsn) {
		this.bsn = bsn;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
}
