/**
 * <p>Title: CtidRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean.vo;

/**
 * <p>Title: CtidRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月19日  
 */
public class CtidRequestBean {
	
	private String sign;
	private String bizPackage;
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBizPackage() {
		return bizPackage;
	}
	public void setBizPackage(String bizPackage) {
		this.bizPackage = bizPackage;
	}

}
