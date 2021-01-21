package com.ahdms.billing.vo.omp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-07-24 11:29
 */
@Data
public class CustomerInfoReqVo {

    @ApiModelProperty("业务主键")
    private Long customerId;

    @ApiModelProperty("账号名")
    private String username;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("服务权限信息ID")
    private String secretId;

    @ApiModelProperty("服务权限信息KEY")
    private String secretKey;
}
