/**
 * <p>Title: IdcardAuthData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月17日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

import java.io.Serializable;

/**
 * <p>Title: IdcardAuthData</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年7月17日  
 */
public class IdcardAuthData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String organizeID;
	private String appID;
	private String randomNumber;
	
	public String getOrganizeID() {
		return organizeID;
	}
	public void setOrganizeID(String organizeID) {
		this.organizeID = organizeID;
	}
	public String getAppID() {
		return appID;
	}
	public void setAppID(String appID) {
		this.appID = appID;
	}
	public String getRandomNumber() {
		return randomNumber;
	}
	public void setRandomNumber(String randomNumber) {
		this.randomNumber = randomNumber;
	}
	
}
