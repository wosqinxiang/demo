package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.api.service.PhoneSmsCodeService;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.CtidPhoneService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2020-07-21 9:44
 */
@Service
public class CtidPhoneServiceImpl implements CtidPhoneService {

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult send(String bsn, String mobile, String specialCode) {
        PhoneSmsCodeService pcService = dubboUtils.getDubboService(specialCode, PhoneSmsCodeService.class);
        ApiResult apiResult = pcService.send(bsn, mobile, null, null);
        return new CtidResult(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

    @Override
    public CtidResult auth(String bsn, String mobile, String smsCode, String specialCode) {
        PhoneSmsCodeService pcService = dubboUtils.getDubboService(specialCode, PhoneSmsCodeService.class);
        ApiResult<CtidMessage> apiResult = pcService.auth(bsn, mobile, smsCode, null);
        return new CtidResult<CtidMessage>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }
}
