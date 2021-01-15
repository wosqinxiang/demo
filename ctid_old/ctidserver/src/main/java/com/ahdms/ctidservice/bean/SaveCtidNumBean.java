/**
 * <p>Title: SaveCtidNumBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月21日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: SaveCtidNumBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月21日  
 */
public class SaveCtidNumBean {
	
	private String appId;
	private String ctidIndex;
	private String ctidNum;
	private String ctidValidDate;
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
	public String getCtidIndex() {
		return ctidIndex;
	}
	public void setCtidIndex(String ctidIndex) {
		this.ctidIndex = ctidIndex;
	}
	public String getCtidNum() {
		return ctidNum;
	}
	public void setCtidNum(String ctidNum) {
		this.ctidNum = ctidNum;
	}
	public String getCtidValidDate() {
		return ctidValidDate;
	}
	public void setCtidValidDate(String ctidValidDate) {
		this.ctidValidDate = ctidValidDate;
	}
	
}
