package com.ahdms.auth.model;

/**
 * @author qinxiang
 * @date 2020-09-16 16:58
 */
public class BasicResultData {

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
