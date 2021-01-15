package com.ahdms.auth.model;

/**
 * @author qinxiang
 * @date 2020-07-20 15:51
 */
public class PhoneSmsCodeReturnData {
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

    public static class BizPackageBean{
        private String customNumber;
        private String appName;
        private String businessSerialNumber;
        private String timeStamp;
        private String authResult;
        private String authResultRetainData;
        private boolean success;
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

        public String getBusinessSerialNumber() {
            return businessSerialNumber;
        }

        public void setBusinessSerialNumber(String businessSerialNumber) {
            this.businessSerialNumber = businessSerialNumber;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
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

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
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
