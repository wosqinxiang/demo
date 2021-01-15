/**
 * <p>Title: CtidPCManageServiceImpl.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年9月9日
 * @version 1.0
 */
package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.*;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.CtidPCManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.ctidservice.vo.SfxxBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>Title: CtidPCManageServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年9月9日
 */
@Service
public class CtidPCManageServiceImpl implements CtidPCManageService {
    private static final Logger logger = LoggerFactory.getLogger(CtidPCManageServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult<CtidMessage> downCtidOX10(String authCode, String cardName, String cardNum, String cardStart, String cardEnd, String serverId) {

        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);

        ApiResult<CtidMessage> apiResult = ctidPCManageService.downCtidOX10(authCode, cardName, cardNum, cardStart, cardEnd);

        return new CtidResult<CtidMessage>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

    @Override
    public CtidResult<Map<String, String>> authCtid0X06(String faceData, String ctidInfo, String serverId) {
        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);
        ApiResult<CtidAuthReturnBean> apiResult = ctidPCManageService.authCtid0X06(faceData, ctidInfo);
        return new CtidResult<Map<String, String>>(apiResult.getCode(), apiResult.getMessage(), null);
    }

    @Override
    public CtidResult<CreateCodeRequestReturnBean> createCtidCode(String ctidInfo, String serverId) {
        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);
        ApiResult<CreateCodeRequestReturnBean> apiResult = ctidPCManageService.createCtidCode(ctidInfo);
        return new CtidResult<CreateCodeRequestReturnBean>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

    @Override
    public CtidResult validateCtidCode(Integer authMode, String faceData, String authCode, String qrCode, String serverId) {

        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);
        ApiResult apiResult = ctidPCManageService.validateCtidCode(authMode, faceData, authCode, qrCode);
        return new CtidResult(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

    @Override
    public CtidResult<CtidMessage> downCtidInfo(String authCode, String authApplyRetainData, String serverId) {
        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);
        ApiResult<CtidMessage> apiResult = ctidPCManageService.downCtidInfo(authCode, authApplyRetainData);
        return new CtidResult<CtidMessage>(apiResult.getCode(), apiResult.getMessage(), apiResult.getData());
    }

    /**
     * 获得网证编号以及网证有效期
     */
    @Override
    public AuthCtidValidDateReturnBean getCtidNumAndValidDate(String ctidInfo, String serverId) {
        com.ahdms.api.service.CtidPCManageService ctidPCManageService = dubboUtils.getDubboService(serverId, com.ahdms.api.service.CtidPCManageService.class);
        AuthCtidValidDateReturnBean apiResult = ctidPCManageService.getCtidNumAndValidDate(ctidInfo);
        return apiResult;
    }

    @Override
    public CtidResult<CtidMessage> downCtidInfo(String authCode, SfxxBean sfxxBean, String serverId) {
        return downCtidOX10(authCode, sfxxBean.getName(), sfxxBean.getIdCard(), sfxxBean.getStartDate(), sfxxBean.getEndDate(), serverId);
    }

}
