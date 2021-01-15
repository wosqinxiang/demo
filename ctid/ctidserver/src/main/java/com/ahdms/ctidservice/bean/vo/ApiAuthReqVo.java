/**
 * <p>Title: ApiAuthRequestBean.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年8月8日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.bean.vo;

/**
 * <p>Title: ApiAuthRequestBean</p>  
 * <p>Description: </p>  
 * @author qinxiang  
 * @date 2019年8月8日  
 */
public class ApiAuthReqVo {
	
	private String cardName;
	
	private String cardNum;
	
	private String photoData;

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

	public String getPhotoData() {
		return photoData;
	}

	public void setPhotoData(String photoData) {
		this.photoData = photoData;
	}
	
}
