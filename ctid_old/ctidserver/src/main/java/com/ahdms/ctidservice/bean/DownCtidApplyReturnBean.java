/**
 * <p>Title: DownCtidApplyReturnBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

import com.ahdms.api.model.ApplyReturnBean;

/**
 * <p>Title: DownCtidApplyReturnBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class DownCtidApplyReturnBean extends ApplyReturnBean{
	
	private String ctidInfo;
	
	private String ctidIndex;

	public String getCtidInfo() {
		return ctidInfo;
	}

	public void setCtidInfo(String ctidInfo) {
		this.ctidInfo = ctidInfo;
	}

	public String getCtidIndex() {
		return ctidIndex;
	}

	public void setCtidIndex(String ctidIndex) {
		this.ctidIndex = ctidIndex;
	}
	
}
