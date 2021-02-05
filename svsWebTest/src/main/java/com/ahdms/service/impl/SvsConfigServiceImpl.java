package com.ahdms.service.impl;

import com.ahdms.bean.model.SvsConfig;
import com.ahdms.dao.ISvsConfigMapper;
import com.ahdms.exception.SVS_ServerConnectException;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.service.ISvsConfigService;
import com.ahdms.sv.SVTool;
import com.ahdms.util.Base64Utils;
import com.ahdms.util.UUIDUtils;
import org.bouncycastle.asn1.x509.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-03 10:21
 */
@Service
public class SvsConfigServiceImpl implements ISvsConfigService {

    @Autowired
    private ISvsConfigMapper svsConfigMapper;

    @Override
    public void addSvsConfig(SvsConfig svsConfig) throws Exception{
        if(StringUtils.isBlank(svsConfig.getId())){
            svsConfig.setId(UUIDUtils.getUUID());
        }
        SVTool svTool = SVTool.getInstance();
        svTool.SVS_InitServerConnect(svsConfig.getIp(), svsConfig.getPort());
        Certificate cert = svTool.SVS_GetCertInfo(svsConfig.getSerialNumber());
        svTool.SVS_SignData(svsConfig.getKeyIndex(),svsConfig.getKeyValue(),"AHdms".getBytes());
        if (StringUtils.isBlank(svsConfig.getEncryptKey())) {
            byte[] bytes = svTool.SVS_GetCTIDEncryptKey(cert);
            String encryptKey = Base64Utils.encodeToString(bytes);
            svsConfig.setEncryptKey(encryptKey);
        }
        svsConfigMapper.insert(svsConfig);

    }

    @Override
    public SvsConfig selectById(String svsConfigId) {
        return svsConfigMapper.selectById(svsConfigId);
    }

    @Override
    public void deleteById(String svsConfigId) {
        svsConfigMapper.deleteById(svsConfigId);
    }
}
