package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.AuthIdCardServcie;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.IAuthIdCardService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-05 11:06
 */
@Service
public class AuthIdCardServiceImpl implements IAuthIdCardService {

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult auth(String cardName, String cardNum, String photoData, String serverId) {
        //先本地比对 两项信息
        boolean flag = authOnLocal(cardName,cardNum);

        AuthIdCardServcie authService = dubboUtils.getDubboService(serverId, AuthIdCardServcie.class);
        ApiResult<CtidAuthReturnBean> auth = authService.auth(cardName, cardNum, photoData);
        return new CtidResult(auth.getCode(), auth.getMessage(), auth.getData());
    }

    private boolean authOnLocal(String cardName, String cardNum) {
        //验证姓名、身份证号码是否合规


        String cardNumHash = CalculateHashUtils.calculateHash(cardNum.getBytes());


        return false;
    }
}
