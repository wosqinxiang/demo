/**
 * <p>Title: DownCtidServiceImpl.java</p>
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
import com.ahdms.api.model.CtidMessage;
import com.ahdms.api.service.CtidDownService;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.DownCtidService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>Title: DownCtidServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年8月7日
 */
@Service
public class DownCtidServiceImpl implements DownCtidService {

    Logger logger = LoggerFactory.getLogger(DownCtidServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;


    @Override
    public CtidResult<ApplyReturnBean> downCtidApply(String authMode, String serverId) {

        CtidDownService ctidDownService = dubboUtils.getDubboService(serverId, CtidDownService.class);
        ApiResult<ApplyReturnBean> downCtidApply = ctidDownService.downCtidApply(authMode);
        return new CtidResult<ApplyReturnBean>(downCtidApply.getCode(), downCtidApply.getMessage(), downCtidApply.getData());
    }

    @Override
    public CtidResult<CtidMessage> downCtidRequest(String businessSerialNumber, String authMode, String photoData,
                                                   String idcardAuthData, String authCode, String authApplyRetainData, String serverId) {

        CtidDownService ctidDownService = dubboUtils.getDubboService(serverId, CtidDownService.class);
        ApiResult<CtidMessage> downCtidRequest = ctidDownService.downCtidRequest(businessSerialNumber, authMode, photoData, idcardAuthData, authCode, authApplyRetainData);
        return new CtidResult<CtidMessage>(downCtidRequest.getCode(), downCtidRequest.getMessage(), downCtidRequest.getData());
    }

    @Override
    public CtidResult<CtidMessage> downCtidRequest(String businessSerialNumber, String authMode, String photoData,
                                                   String idcardAuthData, String authCode, String cardName, String cardNum, String cardStart, String cardEnd,
                                                   String serverId) {
        CtidDownService ctidDownService = dubboUtils.getDubboService(serverId, CtidDownService.class);
        ApiResult<CtidMessage> downCtidRequest = ctidDownService.downCtidRequest(businessSerialNumber, authMode, photoData, idcardAuthData, authCode, cardName, cardNum, cardStart, cardEnd);
        return new CtidResult<>(downCtidRequest.getCode(), downCtidRequest.getMessage(), downCtidRequest.getData());
    }

}
