package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-18 8:42
 */
@Data
public class EncryptEnvelopeReqVo {

    @NotNull
    private String base64EncodeCert;
    @NotNull
    private String inData;
}
