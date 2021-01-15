package com.ahdms.ctidservice.service;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.vo.CtidRequestBean;

/**
 * @author qinxiang
 * @date 2021-01-05 9:28
 */
public interface ICtidManageService {

    ApplyReturnBean authCtidApply(CtidRequestBean dcaBean);

    CtidResult authCtidRequest(CtidRequestBean dcaBean);
}
