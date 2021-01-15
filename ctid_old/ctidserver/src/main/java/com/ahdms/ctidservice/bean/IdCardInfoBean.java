package com.ahdms.ctidservice.bean;

public class IdCardInfoBean {
	
	private String cardName;
	private String cardNum;
	
	
	public IdCardInfoBean() {
		super();
	}
	public IdCardInfoBean(String cardName, String cardNum) {
		super();
		this.cardName = cardName;
		this.cardNum = cardNum;
	}
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
	
}
