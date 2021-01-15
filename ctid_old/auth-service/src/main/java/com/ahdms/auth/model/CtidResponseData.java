package com.ahdms.auth.model;

/**
 * @author qinxiang
 * @date 2020-09-07 10:22
 */
public class CtidResponseData {

    private String sign;

    private String bizPackage;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBizPackage() {
        return bizPackage;
    }

    public void setBizPackage(String bizPackage) {
        this.bizPackage = bizPackage;
    }
}
