/**
 * <p>Title: ValidateCodeServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年8月20日
 * @version 1.0
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.ValidateCodeRequestReturnBean;
import com.ahdms.api.service.CtidValidateCodeService;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.ValidateCodeService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Title: ValidateCodeServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年8月20日
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    Logger logger = LoggerFactory.getLogger(ValidateCodeServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult<ApplyReturnBean> validateCodeApply(String applyData, Integer authMode, String serverId) {
        CtidValidateCodeService validateCodeService = dubboUtils.getDubboService(serverId, CtidValidateCodeService.class);
        ApiResult<ApplyReturnBean> validateCodeApply = validateCodeService.validateCodeApply(applyData, authMode);
        return new CtidResult<ApplyReturnBean>(validateCodeApply.getCode(), validateCodeApply.getMessage(), validateCodeApply.getData());
    }

    @Override
    public CtidResult<ValidateCodeRequestReturnBean> validateCodeRequest(String bizSerialNum, Integer authMode, String authCode, String photoData,
                                                                         String idcardAuthData, String serverId) {
        CtidValidateCodeService validateCodeService = dubboUtils.getDubboService(serverId, CtidValidateCodeService.class);
        ApiResult<ValidateCodeRequestReturnBean> validateCodeRequest = validateCodeService.validateCodeRequest(bizSerialNum, authMode, authCode, photoData, idcardAuthData);
        return new CtidResult<ValidateCodeRequestReturnBean>(validateCodeRequest.getCode(), validateCodeRequest.getMessage(), validateCodeRequest.getData());
    }

}
