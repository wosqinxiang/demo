/**
 * <p>Title: QrCodeController.java</p>
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: QrCodeController</p>
 * <p>Description: 二维码赋码接口</p>
 *
 * @author qinxiang
 * @date 2019年7月26日
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class QrCodeController {
    Logger logger = LoggerFactory.getLogger(QrCodeController.class);

    @Autowired
    private CtidManageService ctidManageService;

    @Autowired
    private CtidLogService ctidLogService;

    /**
     * 二维码赋码 申请接口
     *
     * @param data
     * @return
     */
    @RequestMapping("ctid/createCodeApply")
    public CtidResult createCodeApply(@RequestBody CtidRequestBean data) {
        return ctidManageService.createCodeApply(data);
    }

    /**
     * 二维码赋码 请求接口
     *
     * @param data
     * @param request
     * @return
     */
    @RequestMapping("ctid/createCodeRequest")
    public CtidResult createCodeRequest(@RequestBody CtidRequestBean data, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageService.createCodeRequest(data,ipAddress);

        ctidLogService.insertSelective(data, result, ipAddress, CtidLog.CREATE_CODE_TYPE);

        return result;
    }

    @RequestMapping("ctid/createQrCode")
    public CtidResult createQrCode(@RequestBody CtidRequestBean data, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageService.createQrCode(data,ipAddress);

        ctidLogService.insertSelective(data, result, ipAddress, CtidLog.CREATE_CODE_TYPE);

        return result;
    }

}
