package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.model.CtidInfos;

/**
 * @author qinxiang
 * @date 2021-01-13 14:30
 */
public interface ICtidInfosService {
    CtidInfos selectById(String ctidIndex);
}
