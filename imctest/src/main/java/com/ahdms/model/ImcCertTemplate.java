package com.ahdms.model;

import com.ahdms.framework.mybatis.core.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author qinxiang
 * @date 2020-12-11 13:58
 */
@TableName("imc_cert_template")
@Data
public class ImcCertTemplate extends Entity {

    private String templateId;
    private String templateName;
    private String templateDesc;
    private Integer defaultEffectDays;
    private Integer maxEffectDays;
    private Integer standardExt;
    private Integer customerExt;
    private Integer isPublishLdap;
    private String dnIds;
    private Integer templateStatus;
    private Integer templateType;
    private Integer defaultFlag;

}
