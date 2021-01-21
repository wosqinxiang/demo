package com.ahdms.billing.vo.omp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ServiceLogPageReqVo {

    @ApiModelProperty("查询开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startDate;
    @ApiModelProperty("查询截止时间")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endDate;
    @ApiModelProperty("页码")
    private int pageNum = 1;
    @ApiModelProperty("分页大小")
    private int pageSize = 100;

}
