package com.ahdms.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 15:56
 */
@Data
public class CertTemplateReqVo {

    @ApiModelProperty("模板名称")
    private String templateName;
    @ApiModelProperty("模板描述")
    private String templateDesc;
    @ApiModelProperty("默认天数")
    private Integer defaultEffectDays;
    @ApiModelProperty("最大天数")
    private Integer maxEffectDays;
    @ApiModelProperty("是否发布至LDAP")
    private Integer isPublishLdap;
    @ApiModelProperty("dn选项")
    private String dnIds;
    @ApiModelProperty("模板状态")
    private Integer templateStatus;
    @ApiModelProperty("模板类型")
    private Integer templateType;
    @ApiModelProperty("是否默认模板")
    private Integer defaultFlag;

    @ApiModelProperty("标准扩展项")
    private List<StandardExtReqVo> standardExts;
    @ApiModelProperty("自定义扩展项")
    private List<CustomerExtReqVo> customerExts;

}
