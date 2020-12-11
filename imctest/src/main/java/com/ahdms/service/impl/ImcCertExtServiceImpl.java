package com.ahdms.service.impl;

import com.ahdms.bean.CertExtensionRspVo;
import com.ahdms.dao.IImcCertExtensionMapper;
import com.ahdms.model.ImcCertExtension;
import com.ahdms.service.IImcCertExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qinxiang
 * @date 2020-12-11 15:19
 */
@Service
public class ImcCertExtServiceImpl implements IImcCertExtService {

    @Autowired
    private IImcCertExtensionMapper certExtensionMapper;

    @Override
    public List<CertExtensionRspVo> listMenu() {
        List<CertExtensionRspVo> result = new ArrayList<>();
        List<ImcCertExtension> imcCertExtensions = certExtensionMapper.selectList(null);
        imcCertExtensions.forEach(certExt -> {
            CertExtensionRspVo certExtensionRspVo = new CertExtensionRspVo();

            certExtensionRspVo.setKeyValue(certExt.getIsRequired());
            certExtensionRspVo.setId(certExt.getId());
            certExtensionRspVo.setName(certExt.getName());

            result.add(certExtensionRspVo);

        });

        return result;
    }

}
