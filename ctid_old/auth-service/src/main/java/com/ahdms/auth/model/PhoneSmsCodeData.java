package com.ahdms.auth.model;

/**
 * @author qinxiang
 * @date 2020-07-20 15:50
 */
public class PhoneSmsCodeData {
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
        private String phoneData;
        private String customNumber;
        private String appName;
        private String businessSerialNumber;
        private String timeStamp;
        private String authMode;
        public String getPhoneData() {
            return phoneData;
        }
        public void setPhoneData(String phoneData) {
            this.phoneData = phoneData;
        }
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
        public String getAuthMode() {
            return authMode;
        }
        public void setAuthMode(String authMode) {
            this.authMode = authMode;
        }
    }
}
