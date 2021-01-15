package com.ahdms.ctidservice.web.controller;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.service.CtidManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qinxiang
 * @date 2020-07-21 8:41
 */
@RestController
@RequestMapping(value = "ctid/phone", method = {RequestMethod.GET, RequestMethod.POST})
public class PhoneSmsCodeController {
    @Autowired
    private CtidManageService ctidManageService;

    /**
     * 发送验证码接口(返回业务流水号)
     *
     * @param dcaBean
     * @return
     */
    @RequestMapping("send")
    public CtidResult sendSmsCode(@RequestBody CtidRequestBean dcaBean) {
        return ctidManageService.sendSmsCode(dcaBean);
    }

    /**
     * 验证验证码接口(返回网证数据以及PID)
     *
     * @param dcaBean
     * @return
     */
    @RequestMapping("auth")
    public CtidResult authSmsCode(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        return ctidManageService.authSmsCode(dcaBean, IpUtils.getIpAddress(request));
    }
}
