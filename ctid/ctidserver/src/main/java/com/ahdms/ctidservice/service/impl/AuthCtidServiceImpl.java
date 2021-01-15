package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.CtidAuthService;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.IAuthCtidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-05 9:36
 */
@Service
public class AuthCtidServiceImpl implements IAuthCtidService {

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public ApplyReturnBean authCtidApply(String authMode, String serverId) {
        CtidAuthService authService = dubboUtils.getDubboService(serverId, CtidAuthService.class);
        ApiResult<ApplyReturnBean> authCtidApply = authService.authCtidApply(authMode);
        return authCtidApply.getData();
    }

    @Override
    public CtidResult<CtidAuthReturnBean> authCtidRequest(String bsn, String authMode, String photoData, String idcardAuthData, String authCode, String authApplyRetainData, String specialCode) {
        CtidAuthService authService = dubboUtils.getDubboService(specialCode, CtidAuthService.class);
        ApiResult<CtidAuthReturnBean> apiResult = authService.authCtidRequest(bsn, authMode, photoData, idcardAuthData, authCode, authApplyRetainData);
        return new CtidResult<CtidAuthReturnBean>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }
}
