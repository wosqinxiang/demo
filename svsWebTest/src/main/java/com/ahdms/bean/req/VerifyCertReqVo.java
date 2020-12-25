package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-21 13:49
 */
@Data
public class VerifyCertReqVo {

    @NotNull
    private String base64EncodeCert;

}
