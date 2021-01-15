package com.ahdms.ap.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * SignResponseVo class
 *
 * @author ht
 * @date 2019/08/05
 */
public class SignResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 认证结果
     */
    @ApiModelProperty(value ="认证结果")
    private int authResult;

    /**
     * 认证描述
     */
    @ApiModelProperty(value ="认证描述")
    private String authDesc;

    /**
     * 认证时间
     */
    @ApiModelProperty(value ="认证时间")
    private String authDate;

    /**
     * 二维码数据 Two-dimensional code data
     */
    @ApiModelProperty(value ="二维码源数据")
    private String twoBarCodeData;

    public SignResponseVo() {
    }

    public int getAuthResult() {
        return authResult;
    }

    public void setAuthResult(int authResult) {
        this.authResult = authResult;
    }

    public String getTwoBarCodeData() {
        return twoBarCodeData;
    }

    public void setTwoBarCodeData(String twoBarCodeData) {
        this.twoBarCodeData = twoBarCodeData;
    }

    public String getAuthDesc() {
        return authDesc;
    }

    public void setAuthDesc(String authDesc) {
        this.authDesc = authDesc;
    }

    public String getAuthDate() {
        return authDate;
    }

    public void setAuthDate(String authDate) {
        this.authDate = authDate;
    }
}