package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.model.ConfigInfo;

/**
 * @author qinxiang
 * @date 2021-01-11 10:20
 */
public interface IConfigInfoService {
    ConfigInfo selectByKey(String cipherConfigKey);
}
