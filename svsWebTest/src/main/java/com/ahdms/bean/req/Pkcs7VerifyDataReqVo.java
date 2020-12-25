package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:40
 */
@Data
public class Pkcs7VerifyDataReqVo {

    @NotNull
    private String base64Pkcs7SignData;
    @NotNull
    private String inData;
    private int originalText = 1;
    private int irls = 1;

}
