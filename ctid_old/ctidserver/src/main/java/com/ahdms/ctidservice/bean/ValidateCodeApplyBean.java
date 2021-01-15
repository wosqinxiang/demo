/**
 * <p>Title: ValidateCodeApplyBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: ValidateCodeApplyBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 */
public class ValidateCodeApplyBean {
	
	private String applyData;
	private Integer mode;
	private String appId;
	private String appPackage;
    
    public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}
	
	public String getApplyData() {
		return applyData;
	}
	public void setApplyData(String applyData) {
		this.applyData = applyData;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
}
