package com.ahdms.service;

import com.ahdms.bean.model.SvsConfig;

/**
 * @author qinxiang
 * @date 2021-01-03 10:21
 */
public interface ISvsConfigService {
    void addSvsConfig(SvsConfig svsConfig) throws Exception;

    SvsConfig selectById(String svsConfigId);

    void deleteById(String svsConfigId);
}
