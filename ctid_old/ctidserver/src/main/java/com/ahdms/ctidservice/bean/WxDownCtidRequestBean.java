/**
 * <p>Title: WxDownCtidRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月20日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean;

/**
 * <p>Title: WxDownCtidRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年9月20日  
 */
public class WxDownCtidRequestBean {
	
	private String token;
	private Long reqTime;
	private String signResult;
	private String cardName;
	private String cardNum;
	private String cardStart;
	private String cardEnd;
	private String authCode;
	private String openID;
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardStart() {
		return cardStart;
	}
	public void setCardStart(String cardStart) {
		this.cardStart = cardStart;
	}
	public String getCardEnd() {
		return cardEnd;
	}
	public void setCardEnd(String cardEnd) {
		this.cardEnd = cardEnd;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getReqTime() {
		return reqTime;
	}
	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}
	public String getSignResult() {
		return signResult;
	}
	public void setSignResult(String signResult) {
		this.signResult = signResult;
	}
	public String getOpenID() {
		return openID;
	}
	public void setOpenID(String openID) {
		this.openID = openID;
	}
	
}
