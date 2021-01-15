package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.bean.model.ConfigInfo;
import com.ahdms.ctidservice.dao.IConfigInfoMapper;
import com.ahdms.ctidservice.service.IConfigInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-11 10:21
 */
@Service
public class ConfigInfoServiceImpl implements IConfigInfoService {

    @Autowired
    private IConfigInfoMapper configInfoMapper;

    @Override
    public ConfigInfo selectByKey(String configKey) {
        return configInfoMapper.selectOne(new LambdaQueryWrapper<ConfigInfo>()
                .eq(ConfigInfo::getConfigKey,configKey));
    }
}
