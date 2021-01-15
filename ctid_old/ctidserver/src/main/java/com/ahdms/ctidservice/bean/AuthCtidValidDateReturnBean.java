/**
 * <p>Title: AuthCtidValidDateReturnBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月28日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: AuthCtidValidDateReturnBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月28日  
 */
public class AuthCtidValidDateReturnBean {
	
	private String ctidNum;
	private String ctidValidDate;
	private int status;
	
	public AuthCtidValidDateReturnBean() {
		super();
	}
	public AuthCtidValidDateReturnBean(String ctidNum, String ctidValidDate, int status) {
		super();
		this.ctidNum = ctidNum;
		this.ctidValidDate = ctidValidDate;
		this.status = status;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
