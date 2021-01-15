/**
 * Created on 2019年8月14日 by liuyipin
 */
package com.ahdms.ap.vo;
/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class ValidateCodeRequestVO {

	 String bizSerialNum ;
	 
	 int authMode ;
	 
	 String authCode;
	 
	 String photoData;
	 
	 String idcardAuthData;

	public String getBizSerialNum() {
		return bizSerialNum;
	}

	public void setBizSerialNum(String bizSerialNum) {
		this.bizSerialNum = bizSerialNum;
	}

	public int getAuthMode() {
		return authMode;
	}

	public void setAuthMode(int authMode) {
		this.authMode = authMode;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getPhotoData() {
		return photoData;
	}

	public void setPhotoData(String photoData) {
		this.photoData = photoData;
	}

	public String getIdcardAuthData() {
		return idcardAuthData;
	}

	public void setIdcardAuthData(String idcardAuthData) {
		this.idcardAuthData = idcardAuthData;
	}
	 
}

