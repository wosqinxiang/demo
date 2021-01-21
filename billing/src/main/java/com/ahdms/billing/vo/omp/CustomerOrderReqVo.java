package com.ahdms.billing.vo.omp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-07-24 12:30
 */
@Data
public class CustomerOrderReqVo {

    @ApiModelProperty("客户业务主键")
    private Long customerId;

    @ApiModelProperty("产品业务主键")
    private Long productId;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("资源包次数")
    private Integer useCount;

    @ApiModelProperty("到期时间")
    private String expireTime;
}
