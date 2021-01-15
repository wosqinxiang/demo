package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.aop.Billing;
import com.ahdms.ctidservice.bean.vo.ApiAuthReqVo;
import com.ahdms.ctidservice.service.IApiAuthService;
import com.ahdms.ctidservice.service.IAuthIdCardService;
import com.ahdms.framework.core.web.response.ResultAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-05 10:27
 */
@Service
public class ApiAuthServiceImpl implements IApiAuthService {

    @Autowired
    private IAuthIdCardService authIdCardService;

    @Override
    public CtidAuthReturnBean auth(String cardName, String cardNum, String photoData) {
        ResultAssert.throwFail("001","测试");
        return null;
    }
}
