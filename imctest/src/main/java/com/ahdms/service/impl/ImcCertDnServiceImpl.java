package com.ahdms.service.impl;

import com.ahdms.bean.CertDnRspVo;
import com.ahdms.dao.IImcCertDnMapper;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.model.ImcCertDn;
import com.ahdms.service.IImcCertDnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 16:55
 */
@Service
public class ImcCertDnServiceImpl implements IImcCertDnService {

    @Autowired
    private IImcCertDnMapper certDnMapper;

    @Override
    public List<CertDnRspVo> list() {
        List<ImcCertDn> imcCertDns = certDnMapper.selectList(null);
        List<CertDnRspVo> certDnRspVos = new ArrayList<>();
        imcCertDns.forEach(imcCertDn -> {
            CertDnRspVo certDnRspVo = new CertDnRspVo();
            String s = StringUtils.isBlank(imcCertDn.getDefaultValue()) ? "" : imcCertDn.getDefaultValue();
            certDnRspVo.setValue(imcCertDn.getName() + "=" + s);
            certDnRspVo.setIsChoose(imcCertDn.getIsRequired());
            certDnRspVo.setIsRequired(imcCertDn.getIsRequired());
            certDnRspVo.setId(imcCertDn.getId());
            certDnRspVos.add(certDnRspVo);
        });
        return certDnRspVos;
    }

    @Override
    public List<CertDnRspVo> getDnRsp(String dnIds) {
        String[] dnIdArr = StringUtils.split(dnIds,",");
        List<CertDnRspVo> list = list();
        list.stream().forEach(certDn -> {
            if(contain(dnIdArr,String.valueOf(certDn.getId()))){
                certDn.setIsChoose(0);
            }
        });
        return list;
    }

    private boolean contain(String[] arr,String id){
        return Arrays.stream(arr).anyMatch(id::equals);
    }
}
