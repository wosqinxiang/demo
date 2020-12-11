package com.ahdms.framework.core.commom.page;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Data
@ApiModel(description = "分页查询模型")
public class PageReqParam extends PageParam {


    @Nullable
    @ApiModelProperty("升序参数数组")
    private List<String> ascs;
    @Nullable
    @ApiModelProperty("降序参数数组")
    private List<String> descs;

}
