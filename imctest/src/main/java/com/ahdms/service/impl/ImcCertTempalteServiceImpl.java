package com.ahdms.service.impl;

import com.ahdms.bean.CertTemplateReqVo;
import com.ahdms.bean.CertTemplateRspVo;
import com.ahdms.bean.CustomerExtReqVo;
import com.ahdms.bean.StandardExtReqVo;
import com.ahdms.dao.IImcCertTemplateMapper;
import com.ahdms.dao.IImcTempalteExtensionChooseMapper;
import com.ahdms.framework.core.commom.util.BeanUtils;
import com.ahdms.framework.core.commom.util.CollectionUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.model.ImcCertTemplate;
import com.ahdms.model.ImcTempalteExtensionChoose;
import com.ahdms.service.IImcCertDnService;
import com.ahdms.service.IImcCertTempalteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author qinxiang
 * @date 2020-12-11 16:19
 */
@Service
public class ImcCertTempalteServiceImpl implements IImcCertTempalteService {
    @Autowired
    private IImcCertTemplateMapper certTemplateMapper;

    @Autowired
    private IImcTempalteExtensionChooseMapper tempalteExtensionChooseMapper;

    @Autowired
    private IImcCertDnService certDnService;

    @Override
    public void addTemplate(CertTemplateReqVo certTemplateReqVo) {
        String templateId = UUID.randomUUID().toString().replace("-","");
        List<ImcTempalteExtensionChoose> tempalteExtensionChooses = new ArrayList<>();
        ImcCertTemplate certTemplate = BeanUtils.copy(certTemplateReqVo, ImcCertTemplate.class);
        certTemplate.setTemplateId(templateId);
        //添加标准扩展项
        if(CollectionUtils.isNotEmpty(certTemplateReqVo.getStandardExts())){
            certTemplate.setStandardExt(0);
            List<StandardExtReqVo> standardExts = certTemplateReqVo.getStandardExts();
            standardExts.forEach(standardExtReqVo -> {
                ImcTempalteExtensionChoose choose = new ImcTempalteExtensionChoose();
                choose.setTemplateId(templateId);
                choose.setExtType(1);
                choose.setExtKey(standardExtReqVo.getExtKey());
                choose.setCriticalValue(standardExtReqVo.getCriticalValue());
                choose.setChildValues(standardExtReqVo.getChildValues());
                tempalteExtensionChooses.add(choose);
            });
        }else{
            certTemplate.setStandardExt(1);
        }
        //添加自定义扩展项
        if(CollectionUtils.isNotEmpty(certTemplateReqVo.getCustomerExts())){
            certTemplate.setCustomerExt(0);
            List<CustomerExtReqVo> customerExts = certTemplateReqVo.getCustomerExts();
            customerExts.forEach(customerExtReqVo -> {
                ImcTempalteExtensionChoose choose = BeanUtils.copy(customerExtReqVo,ImcTempalteExtensionChoose.class);
                choose.setTemplateId(templateId);
                choose.setExtType(2);
                tempalteExtensionChooses.add(choose);
            });
        }else{
            certTemplate.setCustomerExt(1);
        }

        //保存
        certTemplateMapper.insert(certTemplate);
        tempalteExtensionChooseMapper.insertBatch(tempalteExtensionChooses);
    }

    @Override
    public CertTemplateRspVo info(String templateId) {
        ImcCertTemplate certTemplate = certTemplateMapper.selectByBId(templateId);
        CertTemplateRspVo certTemplateRspVo = BeanUtils.copy(certTemplate, CertTemplateRspVo.class);

        String dnIds = certTemplate.getDnIds();
        certTemplateRspVo.setDnIds(certDnService.getDnRsp(dnIds));

        if(0 == certTemplate.getCustomerExt()){

        }
        if(0 == certTemplate.getStandardExt()){

        }
        return certTemplateRspVo;
    }

    private void setDns(){

    }
}
