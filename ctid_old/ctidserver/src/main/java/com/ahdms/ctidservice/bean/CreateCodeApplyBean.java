/**
 * <p>Title: CreateCodeApplyBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: CreateCodeApplyBean</p>  
 * <p>Description: 二维码赋码申请所需参数</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class CreateCodeApplyBean {
	
	private String applyData;
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

}
