package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.AppSdkBasicBean;
import com.ahdms.ctidservice.bean.model.KeyInfo;

/**
 * @author qinxiang
 * @date 2021-01-04 16:51
 */
public interface IKeyInfoService {
    KeyInfo selectByAppSdkBean(AppSdkBasicBean appSdkBasicBean);
}
