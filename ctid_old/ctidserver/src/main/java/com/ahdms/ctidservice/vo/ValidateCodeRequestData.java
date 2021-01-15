/**
 * <p>Title: ValidateCodeRequestData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;


/**
 * <p>Title: ValidateCodeRequestData</p>  
 * <p>Description: 二维码验码请求数据格式</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 */
public class ValidateCodeRequestData {
	
	private BizPackageBean bizPackage;
	private String sign;

	public BizPackageBean getBizPackage() {
		return bizPackage;
	}

	public void setBizPackage(BizPackageBean bizPackage) {
		this.bizPackage = bizPackage;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public static class BizPackageBean {
		
		private String bizSerialNum; //业务流水号
		private String requestTime; //请求时间  YYYYMMddHHmmssSSS
		private String custNum; //客户号  签名验签服务器证书号
		private Integer authMode; //认证模式 1- 二维码+认证码；2- 二维码+人像；3- 二维码+认证码+人像；
		private String authCode; //认证码
		private String photoData; //照片数据
		private String idcardAuthData; //通过 “官方身份认证控件接口定义文档”中的getAuthQRCodeData 接口生成；
		private String authApplyRetainData; //认证保留数据 非必填
		public String getBizSerialNum() {
			return bizSerialNum;
		}
		public void setBizSerialNum(String bizSerialNum) {
			this.bizSerialNum = bizSerialNum;
		}
		public String getRequestTime() {
			return requestTime;
		}
		public void setRequestTime(String requestTime) {
			this.requestTime = requestTime;
		}
		public String getCustNum() {
			return custNum;
		}
		public void setCustNum(String custNum) {
			this.custNum = custNum;
		}
		public Integer getAuthMode() {
			return authMode;
		}
		public void setAuthMode(Integer authMode) {
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
		public String getAuthApplyRetainData() {
			return authApplyRetainData;
		}
		public void setAuthApplyRetainData(String authApplyRetainData) {
			this.authApplyRetainData = authApplyRetainData;
		}
		
	}
	
}
