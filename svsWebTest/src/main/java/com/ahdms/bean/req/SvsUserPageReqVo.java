package com.ahdms.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2021-01-11 15:56
 */
@Data
public class SvsUserPageReqVo {

    @ApiModelProperty("页码")
    private Long pageNum = 1L;
    @ApiModelProperty("分页大小")
    private Long pageSize = 10L;

}
