package com.ahdms.billing.vo.omp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author qinxiang
 * @date 2020-07-24 11:25
 */
@Data
public class CustomerProductRspVo {

    @ApiModelProperty("客户ID")
    private String customerId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("产品ID")
    private String productId;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("剩余次数")
    private Integer remainCount;

    @ApiModelProperty("到期时间")
    @JsonFormat
    private Date expireTime;

}
