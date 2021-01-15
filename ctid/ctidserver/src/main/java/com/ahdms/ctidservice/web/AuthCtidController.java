package com.ahdms.ctidservice.web;

import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.vo.CtidRequestBean;
import com.ahdms.ctidservice.context.CtidContextUtils;
import com.ahdms.ctidservice.service.ICtidManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qinxiang
 * @date 2021-01-04 11:24
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Api("auth-ctid")
public class AuthCtidController {

    @Autowired
    private ICtidManageService ctidManageService;

    /**
     * 认证申请(获取随机数以及业务流水号)
     * @param dcaBean
     * @return
     */
    @RequestMapping("ctid/authCtidApply")
    public CtidResult authCtidApply(@RequestBody CtidRequestBean dcaBean) {
        ApplyReturnBean applyReturnBean = ctidManageService.authCtidApply(dcaBean);
        return CtidResult.successSign(applyReturnBean,CtidContextUtils.getKeyInfo().getSecretKey());
    }

    /**
     * 网证认证
     * @param dcaBean
     * @param request
     * @return
     */
    @RequestMapping("ctid/authCtidRequest")
    public CtidResult authCtidRequest(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
//        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageService.authCtidRequest(dcaBean);
//
//        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.AUTH_TYPE);
//
//        return result;
        return null;
    }

}
