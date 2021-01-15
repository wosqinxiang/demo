package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.bean.AppSdkBasicBean;
import com.ahdms.ctidservice.bean.model.KeyInfo;
import com.ahdms.ctidservice.dao.IKeyInfoMapper;
import com.ahdms.ctidservice.service.IKeyInfoService;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-04 16:52
 */
@Service
public class KeyInfoServiceImpl implements IKeyInfoService {

    @Autowired
    private IKeyInfoMapper keyInfoMapper;

    @Override
    public KeyInfo selectByAppSdkBean(AppSdkBasicBean appSdkBasicBean) {
        return keyInfoMapper.selectOne(new LambdaQueryWrapper<KeyInfo>()
                .eq(KeyInfo::getAppId, appSdkBasicBean.getAppId())
                .eq(StringUtils.isNotBlank(appSdkBasicBean.getAppPackage()), KeyInfo::getAppPackage, appSdkBasicBean.getAppPackage()));
    }
}
