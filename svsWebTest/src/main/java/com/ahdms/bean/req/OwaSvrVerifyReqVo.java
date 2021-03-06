package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:43
 */
@Data
public class OwaSvrVerifyReqVo {

    @NotNull
    private String base64EncodeCert;
    @NotNull
    private String base64SignValue;
    @NotNull
    private String serverRandom;
    @NotNull
    private String clientRandom;
    private int verifyLevel;

}
