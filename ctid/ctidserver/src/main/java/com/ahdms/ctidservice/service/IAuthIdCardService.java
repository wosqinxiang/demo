package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.CtidResult;

/**
 * @author qinxiang
 * @date 2021-01-05 11:06
 */
public interface IAuthIdCardService {

    CtidResult auth(String cardName,String cardNum,String photoData,String serverId);

}
