package com.ahdms.bean.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-17 16:13
 */
@Data
public class SignDataReqVo {

    @NotNull
    private String inData;

}
