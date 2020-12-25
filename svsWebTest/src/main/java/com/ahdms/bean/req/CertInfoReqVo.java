package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-21 13:48
 */
@Data
public class CertInfoReqVo {

    @NotNull
    private String appCode;

}
