/**
 * <p>Title: AuthCtidServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年8月7日
 * @version 1.0
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.CtidAuthService;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.AuthCtidService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Title: AuthCtidServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年8月7日
 */
@Service
public class AuthCtidServiceImpl implements AuthCtidService {

    Logger logger = LoggerFactory.getLogger(AuthCtidServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult<ApplyReturnBean> authCtidApply(String authMode, String serverId) {

        CtidAuthService authService = dubboUtils.getDubboService(serverId, CtidAuthService.class);
        try {
            ApiResult<ApplyReturnBean> authCtidApply = authService.authCtidApply(authMode);
            return new CtidResult<ApplyReturnBean>(authCtidApply.getCode(), authCtidApply.getMessage(), authCtidApply.getData());
        }catch (Exception e){
            return CtidResult.error(e.getMessage());
        }

    }

    @Override
    public CtidResult<CtidAuthReturnBean> authCtidRequest(String businessSerialNumber, String authMode, String photoData,
                                                          String idcardAuthData, String authCode, String authApplyRetainData, String serverId) {
        CtidAuthService authService = dubboUtils.getDubboService(serverId, CtidAuthService.class);
        ApiResult<CtidAuthReturnBean> apiResult = authService.authCtidRequest(businessSerialNumber, authMode, photoData, idcardAuthData, authCode, authApplyRetainData);
        return new CtidResult<CtidAuthReturnBean>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

}
