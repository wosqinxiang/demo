/**
 * <p>Title: ValidateCodeApplyData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.ctidservice.vo;

/**
 * <p>
 * Title: ValidateCodeApplyData
 * </p>
 * <p>
 * Description: 二维码验码申请数据格式
 * </p>
 * 
 * @author qinxiang
 * @date 2019年7月16日
 */
public class ValidateCodeApplyData {

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

		private String timeStamp; // 时间戳 YYYYMMddHHmmssSSS
		private String applyData; // 申请数据通过“官方身份认证控件接口定义文档”中的 getApplyData 接口生成；
		private String custNum; // 客户号，即签名验签服务器证书号
		private String cardReaderVersion; // 读卡控件版本
		private String liveDetectCtrlVer; // 活体控件版本
		private String authCodeControlVersion; // 认证码控件版本
		private Integer authMode; // 认证模式 1- 二维码+认证码；2- 二维码+人像；3- 二维码+认证码+人像

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getApplyData() {
			return applyData;
		}

		public void setApplyData(String applyData) {
			this.applyData = applyData;
		}

		public String getCustNum() {
			return custNum;
		}

		public void setCustNum(String custNum) {
			this.custNum = custNum;
		}

		public String getCardReaderVersion() {
			return cardReaderVersion;
		}

		public void setCardReaderVersion(String cardReaderVersion) {
			this.cardReaderVersion = cardReaderVersion;
		}

		public String getLiveDetectCtrlVer() {
			return liveDetectCtrlVer;
		}

		public void setLiveDetectCtrlVer(String liveDetectCtrlVer) {
			this.liveDetectCtrlVer = liveDetectCtrlVer;
		}

		public String getAuthCodeControlVersion() {
			return authCodeControlVersion;
		}

		public void setAuthCodeControlVersion(String authCodeControlVersion) {
			this.authCodeControlVersion = authCodeControlVersion;
		}

		public Integer getAuthMode() {
			return authMode;
		}

		public void setAuthMode(Integer authMode) {
			this.authMode = authMode;
		}

	}
}
