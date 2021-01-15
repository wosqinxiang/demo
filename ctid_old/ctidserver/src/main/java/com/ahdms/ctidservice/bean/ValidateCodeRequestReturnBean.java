/**
 * <p>Title: ValidateCodeRequestReturnBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: ValidateCodeRequestReturnBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月20日  
 */
public class ValidateCodeRequestReturnBean {
	private String pid;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public ValidateCodeRequestReturnBean() {
		super();
	}

	public ValidateCodeRequestReturnBean(String pid) {
		super();
		this.pid = pid;
	}
	
}
