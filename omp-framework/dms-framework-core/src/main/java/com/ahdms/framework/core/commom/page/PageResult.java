package com.ahdms.framework.core.commom.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Data
@ApiModel(description = "分页查询模型")
public class PageResult<T> {
    @ApiModelProperty("页码")
    private Long pageNum;
    @ApiModelProperty("分页大小")
    private Long pageSize;
    @ApiModelProperty("总页数")
    private Long pageCount;
    @ApiModelProperty("总数")
    private Long totalCount;
    @ApiModelProperty("数据集合")
    private List<T> records;

}
