package com.ahdms.billing.vo.omp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-07-24 12:40
 */
@Data
public class ProductInfoReqVo {
    @ApiModelProperty("产品ID")
    private Long productId;
    @ApiModelProperty("产品编码")
    private String productCode;
    @ApiModelProperty("产品名称")
    private String productName;
    @ApiModelProperty("供应商编码")
    private String providerCode;
    @ApiModelProperty("服务依赖方编码")
    private String dependCode;
    @ApiModelProperty("供应商名称")
    private String providerName;
    @ApiModelProperty("服务依赖方名称")
    private String dependName;

}
