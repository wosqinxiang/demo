package com.ahdms.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 16:02
 */
@Data
public class StandardExtReqVo {

    @ApiModelProperty("选项Id")
    private Integer id;
    @ApiModelProperty("选项的key")
    private String itemKey;
    @ApiModelProperty("选项的名称")
    private String itemName;
    @ApiModelProperty("关键选项的值(是否被选中0.选中，1.未选中)")
    private Integer criticalValue;
    @ApiModelProperty("子选项的值(自选项的Key用,拼接)")
    private String childValues;
}
