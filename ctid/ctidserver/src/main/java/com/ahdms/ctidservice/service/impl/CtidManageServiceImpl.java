package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.ctidservice.aop.Billing;
import com.ahdms.ctidservice.bean.AuthCtidApplyBean;
import com.ahdms.ctidservice.bean.AuthCtidRequestBean;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.model.CtidInfos;
import com.ahdms.ctidservice.bean.model.KeyInfo;
import com.ahdms.ctidservice.bean.vo.CtidRequestBean;
import com.ahdms.ctidservice.context.CtidContextUtils;
import com.ahdms.ctidservice.context.holder.CtidContextHolder;
import com.ahdms.ctidservice.service.IAuthCtidService;
import com.ahdms.ctidservice.service.ICtidInfosService;
import com.ahdms.ctidservice.service.ICtidManageService;
import com.ahdms.ctidservice.util.JsonUtils;
import com.ahdms.jf.model.JFChannelEnum;
import com.ahdms.jf.model.JFServiceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-05 9:28
 */
@Service
public class CtidManageServiceImpl implements ICtidManageService {

    @Autowired
    private IAuthCtidService authCtidService;

    @Autowired
    private ICtidInfosService ctidInfosService;

    @Override
    public ApplyReturnBean authCtidApply(CtidRequestBean dcaBean) {
        KeyInfo keyInfo = CtidContextUtils.getKeyInfo();
        AuthCtidApplyBean authCtidApplyBean = JsonUtils.readValue(dcaBean.getBizPackage(), AuthCtidApplyBean.class);
        ApplyReturnBean applyReturnBean= authCtidService.authCtidApply(authCtidApplyBean.getAuthMode(),keyInfo.getOrganizeId());
        return applyReturnBean;
    }

    @Override
    @Billing(service = JFServiceEnum.CTID_AUTH,channel = JFChannelEnum.APP_SDK)
    public CtidResult authCtidRequest(CtidRequestBean dcaBean) {
        AuthCtidRequestBean biz = JsonUtils.readValue(dcaBean.getBizPackage(), AuthCtidRequestBean.class);
        String bsn = biz.getBsn();
        String ctidIndex = biz.getCtidIndex();
        String faceData = biz.getFaceData();
        String idCheck = biz.getIdCheck();
        String mode = biz.getMode();

        CtidInfos ctidInfos = ctidInfosService.selectById(ctidIndex);

        CtidResult<CtidAuthReturnBean> authCtidRequest = authCtidService.authCtidRequest(bsn, mode, faceData,
                idCheck, null, null, CtidContextUtils.getSpecialCode());
//        CtidContextHolder.getContext().

        return authCtidRequest;
    }
}
