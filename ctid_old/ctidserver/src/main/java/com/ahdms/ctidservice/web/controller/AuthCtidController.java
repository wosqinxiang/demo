/**
 * <p>Title: AuthCtidController.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2019</p>
 * <p>Company: www.ahdms.com</p>
 *
 * @author qinxiang
 * @date 2019年7月29日
 * @version 1.0
 */
package com.ahdms.ctidservice.web.controller;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.bean.dto.WxApplyCtidInputDTO;
import com.ahdms.ctidservice.bean.dto.WxAuthCardInputDTO;
import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.db.model.CtidLog;
import com.ahdms.ctidservice.service.CtidLogService;
import com.ahdms.ctidservice.service.CtidManageService;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: AuthCtidController</p>  
 * <p>Description: </p>  
 * @author qinxiang
 * @date 2019年7月29日
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class AuthCtidController {
    Logger logger = LoggerFactory.getLogger(AuthCtidController.class);

    @Autowired
    private CtidManageService ctidManageServcie;

    @Autowired
    private CtidLogService ctidLogService;

    /**
     * 认证申请(获取随机数以及业务流水号)
     * @param dcaBean
     * @return
     */
    @RequestMapping("ctid/authCtidApply")
    public CtidResult authCtidApply(@RequestBody CtidRequestBean dcaBean) {

        return ctidManageServcie.authCtidApply(dcaBean);
    }

    /**
     * 网证认证
     * @param dcaBean
     * @param request
     * @return
     */
    @RequestMapping("ctid/authCtidRequest")
    public CtidResult authCtidRequest(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.authCtidRequest(dcaBean,ipAddress);

        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.AUTH_TYPE);

        return result;
    }

    /**
     * 非网证认证
     * @param dcaBean
     * @param request
     * @return
     */
    @RequestMapping("ctid/authCard")
    public CtidResult authCard(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        CtidResult result = ctidManageServcie.authCard(dcaBean, request);

        return result;
    }

    /**
     * 非网证认证 v2.0版本
     * @param dcaBean
     * @param request
     * @return
     */
    @RequestMapping("ctid/authCardNew")
    public CtidResult v2AuthCard(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        CtidResult result = ctidManageServcie.v2AuthCard(dcaBean, request);

        return result;
    }

    @RequestMapping("ctid/applyCtidRequest")
    public CtidResult authCtidRequestNew(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.authCtidRequestNew(dcaBean,ipAddress);

        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.AUTH_TYPE);

        return result;
    }

    @RequestMapping("ctid/getPid")
    public CtidResult getPid(@RequestParam String token, HttpServletRequest request) {
        CtidResult result = ctidManageServcie.getPid(token);


        return result;
    }

    /**
     *
     * <p>Title: </p>
     * <p>Description: 通过PC端控件网证认证</p>
     * @param dcaBean
     * @return
     */
    @RequestMapping("ctid/authCtidInfo")
    public CtidResult authCtidInfo(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.authCtidInfo(dcaBean,ipAddress);

        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.AUTH_TYPE);

        return result;
    }

    /**
     * 通过PC端控件申请AID网证认证(返回token)
     * @param dcaBean
     * @param request
     * @return
     */
    @RequestMapping("ctid/applyCtidInfo")
    public CtidResult applyCtidInfo(@RequestBody CtidRequestBean dcaBean, HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        CtidResult result = ctidManageServcie.applyCtidInfo(dcaBean,ipAddress);

        ctidLogService.insertSelective(dcaBean, result, ipAddress, CtidLog.AUTH_TYPE);

        return result;
    }


    @RequestMapping("ctid/authCtidValidDate")
    public CtidResult authCtidValidDate(@RequestBody CtidRequestBean dcaBean) {
        CtidResult result = ctidManageServcie.authCtidValidDate(dcaBean);
        return result;
    }

    /**
     * 小程序SDK申請AID网证认证(返回token)
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("ctid/wxApplyCtid")
    public CtidResult wxApplyCtid(@RequestBody WxApplyCtidInputDTO data,HttpServletRequest request) {

        try {
            return ctidManageServcie.wxApplyCtid(data,IpUtils.getIpAddress(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CtidResult.error("系统繁忙！请重试！");
    }

    /**
     * 小程序SDK身份认证认证(返回token)
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("ctid/wxAuthCard")
    public CtidResult wxAuthCard(@RequestBody WxAuthCardInputDTO data, HttpServletRequest request) {

        try {
            return ctidManageServcie.wxAuthCard(data, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CtidResult.error("系统繁忙！请重试！");
    }

    @RequestMapping("/ctid/ra/authCtidRequest")
    public String authCtidRequest(HttpServletRequest request) throws Exception{

        try {
            request.setCharacterEncoding("UTF-8");
            //接收参数
            Enumeration<?> pNames = request.getParameterNames();
            Map<String, String> params = new HashMap<String, String>();
            while (pNames.hasMoreElements()) {
                String pName = (String) pNames.nextElement();
                params.put(pName, request.getParameter(pName));
            }
            String appId = params.get("appID");
            String mode = params.get("mode");
            String bsn = params.get("bsn");
            String ret = ctidManageServcie.authCtidRequest(params);
            try {
                ctidLogService.insertAuthCtidLog(ret, appId, mode, IpUtils.getIpAddress(request),bsn);
            } catch (Exception e) {
                logger.error("记录网证日志失败！");
            }
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errRet = ctidManageServcie.getDefaultAuthErrorString(e.getMessage());
            return errRet;
        }
    }



}
