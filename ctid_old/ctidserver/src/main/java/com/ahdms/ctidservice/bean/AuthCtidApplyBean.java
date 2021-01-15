/**
 * <p>Title: AuthCtidApplyBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

import com.ahdms.ctidservice.bean.basic.AppSdkBasicBean;

/**
 * <p>Title: AuthCtidApplyBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class AuthCtidApplyBean extends AppSdkBasicBean {
	
	private String authMode;
	
	public String getAuthMode() {
		return authMode;
	}

	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

}
