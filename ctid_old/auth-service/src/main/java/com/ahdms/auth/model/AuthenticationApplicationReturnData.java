package com.ahdms.auth.model;

/**
 *  认证申请  应答数据
 *
 */
public class AuthenticationApplicationReturnData {
	
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
		
		private String customerNumber;
        private String appName;
        private String timeStamp;
        private String businessSerialNumber;
        private String randomNumber;
        private String success;
        private String errorDesc;
        
		public String getCustomerNumber() {
			return customerNumber;
		}
		public void setCustomerNumber(String customerNumber) {
			this.customerNumber = customerNumber;
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
		public String getRandomNumber() {
			return randomNumber;
		}
		public void setRandomNumber(String randomNumber) {
			this.randomNumber = randomNumber;
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
