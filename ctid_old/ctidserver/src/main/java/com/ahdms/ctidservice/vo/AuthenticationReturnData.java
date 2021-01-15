package com.ahdms.ctidservice.vo;


/**
 * 
 * 认证请求 应答数据
 *
 */
public class AuthenticationReturnData {
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
		
		private String customNumber;
        private String appName;
        private String timeStamp;
        private String businessSerialNumber;
        private String authResult;
        private Object authResultRetainData;
        private String success;
        private String errorDesc;
        
		public String getCustomNumber() {
			return customNumber;
		}
		public void setCustomNumber(String customNumber) {
			this.customNumber = customNumber;
		}
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public String getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getBusinessSerialNumber() {
			return businessSerialNumber;
		}
		public void setBusinessSerialNumber(String businessSerialNumber) {
			this.businessSerialNumber = businessSerialNumber;
		}
		public String getAuthResult() {
			return authResult;
		}
		public void setAuthResult(String authResult) {
			this.authResult = authResult;
		}
		public Object getAuthResultRetainData() {
			return authResultRetainData;
		}
		public void setAuthResultRetainData(Object authResultRetainData) {
			this.authResultRetainData = authResultRetainData;
		}
		public String getSuccess() {
			return success;
		}
		public void setSuccess(String success) {
			this.success = success;
		}
		public String getErrorDesc() {
			return errorDesc;
		}
		public void setErrorDesc(String errorDesc) {
			this.errorDesc = errorDesc;
		}
        
    }
}
