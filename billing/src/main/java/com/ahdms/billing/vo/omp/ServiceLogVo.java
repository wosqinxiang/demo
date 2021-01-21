package com.ahdms.billing.vo.omp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-07-24 11:08
 */
@Data
public class ServiceLogVo {

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("产品ID")
    private String productId;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("时间")
    private String operationTime;

    @ApiModelProperty("结果")
    private String result;

    @ApiModelProperty("错误信息")
    private String message;

    @ApiModelProperty("流水号")
    private String serialNum;


}
