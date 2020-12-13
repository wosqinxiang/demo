package com.ahdms.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:15
 */
@Data
public class CustomerExtReqVo {

    @ApiModelProperty("选项Id")
    private String id;
    @ApiModelProperty("关键选项的值")
    private Integer criticalValue;
    @ApiModelProperty("是否客户端指定值")
    private Integer clientValue;
    @ApiModelProperty("是否服务端指定值")
    private Integer isServer;
    @ApiModelProperty("服务端指定的值")
    private String serverValue;
    @ApiModelProperty("是否参与实体标识编码计算")
    private Integer calculation;

}
