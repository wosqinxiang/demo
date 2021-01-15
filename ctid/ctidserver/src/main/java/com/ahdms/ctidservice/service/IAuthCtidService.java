package com.ahdms.ctidservice.service;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.bean.CtidResult;

/**
 * @author qinxiang
 * @date 2021-01-05 9:36
 */
public interface IAuthCtidService {

    ApplyReturnBean authCtidApply(String authMode, String organizeId);

    CtidResult<CtidAuthReturnBean> authCtidRequest(String bsn, String mode, String faceData, String idcardAuthData, String authCode, String authApplyRetainData, String specialCode);
}
