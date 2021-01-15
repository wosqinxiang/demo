package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.vo.CtidResult;

/**
 * @author qinxiang
 * @date 2020-07-21 9:41
 */
public interface CtidPhoneService {

    CtidResult send(String bsn,String mobile,String specialCode);

    CtidResult auth(String bsn,String mobile,String smsCode,String specialCode);
}
