/**
 * <p>Title: ValidateQrCodeController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年7月26日
 * @version 1.0
 */
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.db.model.CtidLog;
import com.ahdms.ctidservice.service.CtidLogService;
import com.ahdms.ctidservice.service.CtidManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: ValidateQrCodeController</p>
 * <p>Description: </p>
 *
 * @author qinxiang
 * @date 2019年7月26日
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class ValidateQrCodeController {

    @Autowired
    private CtidManageService ctidManageService;

    @Autowired
    private CtidLogService ctidLogService;

    /**
     * 二维码验码 申请接口
     *
     * @param data
     * @return
     */
    @RequestMapping("ctid/validateCodeApply")
    public CtidResult validateCodeApply(@RequestBody CtidRequestBean data) {
        return ctidManageService.validateCodeApply(data);
    }

    /**
     * 二维码验码 请求接口
     *
     * @param data
     * @param request
     * @return
     */
    @RequestMapping("ctid/validateCodeRequest")
    public CtidResult validateCodeRequest(@RequestBody CtidRequestBean data, HttpServletRequest request) {

        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageService.validateCodeRequest(data,ipAddress);

        ctidLogService.insertSelective(data, result, ipAddress, CtidLog.VALIDATE_CODE_TYPE);

        return result;
    }

}
