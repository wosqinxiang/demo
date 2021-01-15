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
 * <p>Description: 二维码验码请求应答数据格式</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 */
public class ValidateCodeRequestReturnData {
	
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
		private String timeStamp; //时间戳  YYYYMMddHHmmssSSS
		private Boolean success;  //申请结果
		private String errorCode; //异常码  正常为 0
		private String errorDesc; //错误信息描述
		private String authResult; //认证结果
		private String authResultRetainData; //认证结果保留数据  返回个人身份标识数据
		
		public String getBizSerialNum() {
			return bizSerialNum;
		}
		public void setBizSerialNum(String bizSerialNum) {
			this.bizSerialNum = bizSerialNum;
		}
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public Boolean getSuccess() {
			return success;
		}
		public void setSuccess(Boolean success) {
			this.success = success;
		}
		public String getErrorCode() {
			return errorCode;
		}
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}
		public String getErrorDesc() {
			return errorDesc;
		}
		public void setErrorDesc(String errorDesc) {
			this.errorDesc = errorDesc;
		}
		public String getAuthResult() {
			return authResult;
		}
		public void setAuthResult(String authResult) {
			this.authResult = authResult;
		}
		public String getAuthResultRetainData() {
			return authResultRetainData;
		}
		public void setAuthResultRetainData(String authResultRetainData) {
			this.authResultRetainData = authResultRetainData;
		}
		
	}
	
}
