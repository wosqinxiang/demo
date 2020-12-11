package com.ahdms.bean;

import lombok.Data;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 16:41
 */
@Data
public class CertTemplateRspVo {

    private String templateId;
    private String templateName;
    private String templateDesc;
    private Integer defaultEffectDays;
    private Integer maxEffectDays;
    private Integer isPublishLdap;
    private List<CertDnRspVo> dnIds;
    private Integer templateStatus;
    private Integer templateType;
    private Integer defaultFlag;

    private List<StandardExtMenu> standardExts;
    private List<CustomerExtRspVo> customerExts;

}
