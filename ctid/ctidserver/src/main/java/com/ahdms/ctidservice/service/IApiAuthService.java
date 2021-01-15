package com.ahdms.ctidservice.service;

import com.ahdms.api.model.CtidAuthReturnBean;

/**
 * @author qinxiang
 * @date 2021-01-05 10:27
 */
public interface IApiAuthService {

    CtidAuthReturnBean auth(String cardName, String cardNum, String photoData);
}
