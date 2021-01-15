/**
 * <p>Title: DownCtidApplyBean.java</p>  
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
 * <p>Title: DownCtidApplyBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class DownCtidApplyBean extends AppSdkBasicBean {
	
    private String authMode;

	public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }



}
