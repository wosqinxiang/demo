package com.ahdms.billing.vo.omp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-07-24 13:57
 */
@Data
public class PageResult<T> {
    @ApiModelProperty("页码")
    private int pageNum;
    @ApiModelProperty("分页大小")
    private int pageSize;
    @ApiModelProperty("总页数")
    private int pageCount;
    @ApiModelProperty("总数")
    private long totalCount;
    @ApiModelProperty("数据集合")
    private List<T> records;
}
