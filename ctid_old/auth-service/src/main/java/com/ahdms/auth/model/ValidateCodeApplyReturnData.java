/**
 * <p>Title: ValidateCodeApplyData.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年7月16日  
 * @version 1.0  
*/
package com.ahdms.auth.model;

/**
 * <p>
 * Title: ValidateCodeApplyData
 * </p>
 * <p>
 * Description: 二维码验码申请应答数据格式
 * </p>
 * 
 * @author qinxiang
 * @date 2019年7月16日
 */
public class ValidateCodeApplyReturnData {

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
		
		private String timeStamp; //时间戳  YYYYMMddHHmmssSSS
		private Boolean success;  //申请结果
		private String errorCode; //异常码  正常为 0
		private String errorDesc; //错误信息描述
		private String bizSerialNum; //业务流水号
		private String randomNumber; //随机数
		
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
		public String getBizSerialNum() {
			return bizSerialNum;
		}
		public void setBizSerialNum(String bizSerialNum) {
			this.bizSerialNum = bizSerialNum;
		}
		public String getRandomNumber() {
			return randomNumber;
		}
		public void setRandomNumber(String randomNumber) {
			this.randomNumber = randomNumber;
		}
		
	}
}
