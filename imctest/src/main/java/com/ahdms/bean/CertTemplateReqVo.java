package com.ahdms.bean;

import lombok.Data;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 15:56
 */
@Data
public class CertTemplateReqVo {

    private String templateName;
    private String templateDesc;
    private Integer defaultEffectDays;
    private Integer maxEffectDays;
    private Integer isPublishLdap;
    private String dnIds;
    private Integer templateStatus;
    private Integer templateType;
    private Integer defaultFlag;

    private List<StandardExtReqVo> standardExts;
    private List<CustomerExtReqVo> customerExts;

}
