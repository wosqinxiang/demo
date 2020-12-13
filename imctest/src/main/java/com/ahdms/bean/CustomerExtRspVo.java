package com.ahdms.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:48
 */
@Data
public class CustomerExtRspVo {

    @ApiModelProperty("选项Id")
    private Integer id;
    private String extKey;
    private String itemName;
    private Integer criticalValue;
    private Integer clientValue;
    private Integer isServer;
    private String serverValue;
    private Integer calculation;
}
