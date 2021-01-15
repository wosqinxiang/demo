/**
 * <p>Title: CreateCodeServiceImpl.java</p>
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
import com.ahdms.api.model.CreateCodeRequestReturnBean;
import com.ahdms.api.service.CtidCreateCodeService;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.CreateCodeService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>Title: CreateCodeServiceImpl</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年8月20日
 */
@Service
public class CreateCodeServiceImpl implements CreateCodeService {
    Logger logger = LoggerFactory.getLogger(CreateCodeServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;

    @Override
    public CtidResult<ApplyReturnBean> createCodeApply(String applyData, String serverId) {
        CtidCreateCodeService createCodeService = dubboUtils.getDubboService(serverId, CtidCreateCodeService.class);
        ApiResult<ApplyReturnBean> createCodeApply = createCodeService.createCodeApply(applyData);
        return new CtidResult<ApplyReturnBean>(createCodeApply.getCode(), createCodeApply.getMessage(), createCodeApply.getData());
    }

    @Override
    public CtidResult<CreateCodeRequestReturnBean> createCodeRequest(String bizSerialNum, String checkData, Integer isPic, String serverId) {
        CtidCreateCodeService createCodeService = dubboUtils.getDubboService(serverId, CtidCreateCodeService.class);
        ApiResult<CreateCodeRequestReturnBean> createCodeRequest = createCodeService.createCodeRequest(bizSerialNum, checkData, isPic);
        return new CtidResult<CreateCodeRequestReturnBean>(createCodeRequest.getCode(), createCodeRequest.getMessage(), createCodeRequest.getData());
    }


}
