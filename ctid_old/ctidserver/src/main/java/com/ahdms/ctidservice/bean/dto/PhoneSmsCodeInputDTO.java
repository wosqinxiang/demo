package com.ahdms.ctidservice.bean.dto;

import com.ahdms.ctidservice.bean.basic.AppSdkBasicBean;

/**
 * @author qinxiang
 * @date 2020-07-21 9:35
 */
public class PhoneSmsCodeInputDTO extends AppSdkBasicBean {

    private String smsCode;

    private String mobile;

    private String bsn;

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }
}
