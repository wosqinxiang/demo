package com.ahdms.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author qinxiang
 * @date 2020-12-21 15:48
 */
@Data
public class GenRandomSignDataReqVo {

    @NotNull
    private String serverRandom;
    @NotNull
    private String clientRandom;
    @ApiModelProperty(value = "模拟双向认证客户端签名时需要传入")
    private String identity;

}
