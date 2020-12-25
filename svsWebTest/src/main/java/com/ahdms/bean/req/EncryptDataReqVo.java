package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-25 16:00
 */
@Data
public class EncryptDataReqVo {

    @NotNull
    private String inData;

}
