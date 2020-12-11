package com.ahdms.service;

import com.ahdms.bean.CertTemplateReqVo;
import com.ahdms.bean.CertTemplateRspVo;

/**
 * @author qinxiang
 * @date 2020-12-11 16:19
 */
public interface IImcCertTempalteService {
    void addTemplate(CertTemplateReqVo certTemplateReqVo);

    CertTemplateRspVo info(String templateId);
}
