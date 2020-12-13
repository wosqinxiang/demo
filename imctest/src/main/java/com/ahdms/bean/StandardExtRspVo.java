package com.ahdms.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 标准扩展项 返回
 * @author qinxiang
 * @date 2020/12/13 17:01
 */
@Data
public class StandardExtRspVo {

    @ApiModelProperty("选项Id")
    private Integer id;
    @ApiModelProperty("选项的key")
    private String itemKey;
    @ApiModelProperty("选项的名称")
    private String itemName;
    @ApiModelProperty("选项的值(是否被选中0.选中，1.未选中)")
    private Integer itemValue;
    @ApiModelProperty("是否有子节点")
    private Integer hasChild;
    @ApiModelProperty("父节点Id")
    private Integer parentId;
    @ApiModelProperty("是否有关键选项")
    private Integer criticalItem;
    @ApiModelProperty("关键选项的值(是否被选中0.选中，1.未选中)")
    private Integer criticalValue;

    @ApiModelProperty("子选项列表")
    private List<StandardExtRspVo> childItems;

}
