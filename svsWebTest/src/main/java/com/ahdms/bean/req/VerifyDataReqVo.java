package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-17 16:14
 */
@Data
public class VerifyDataReqVo {

    @NotNull
    private String base64SignValue;
    @NotNull
    private String base64EncodeCert;
    @NotNull
    private String inData;

    private int verifyLevel;

}
