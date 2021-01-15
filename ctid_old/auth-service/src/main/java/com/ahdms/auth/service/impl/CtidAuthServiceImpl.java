package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.CtidAuthService;
import com.ahdms.auth.common.*;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.AuthenticationApplicationData;
import com.ahdms.auth.model.AuthenticationApplicationReturnData;
import com.ahdms.auth.model.AuthenticationData;
import com.ahdms.auth.model.AuthenticationReturnData;
import com.alibaba.dubbo.config.annotation.Service;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service(group = "${bjca.group}", version = "${dubbo.version}")
@Component
public class CtidAuthServiceImpl implements CtidAuthService {

    Logger logger = LoggerFactory.getLogger(CtidAuthServiceImpl.class);

    @Autowired
    private PKCS7Tool p7Tool;

    @Autowired
    private AuthDataUtil authDataUtil;

    @Autowired
    private Configuration ctidUrl;

    @Value("${http.count:3}")
    private Integer count;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Override
    public ApiResult<ApplyReturnBean> authCtidApply(String authMode) {
        try {
            //获取申请数据
            AuthenticationApplicationData authenticationData = authDataUtil.getAuthenticationData(authMode);
            //发送下载申请
//			String httpResponse = hs.HttpResponse(ctidUrl.getAuthRequestUrl(), JSONObject.fromObject(authenticationData), count);
            String httpResponse = restTemplateUtils.post(ctidUrl.getAuthRequestUrl(), authenticationData);

            if (httpResponse == null) {
                return ApiResult.error("通信超时！请重试！");
            }
            logger.debug("网证认证申请应答数据:" + httpResponse);

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            AuthenticationApplicationReturnData.BizPackageBean downApplyBizPackage = JsonUtils.json2Bean(bizPackageStr, AuthenticationApplicationReturnData.BizPackageBean.class);
            if ("true".equals(downApplyBizPackage.getSuccess())) {
                ApplyReturnBean applyReturnBean = new ApplyReturnBean(downApplyBizPackage.getBusinessSerialNumber(), downApplyBizPackage.getRandomNumber());
                return ApiResult.ok(applyReturnBean);
            } else {
                String errorDesc = downApplyBizPackage.getErrorDesc();
                return ApiResult.error(errorDesc);
            }
        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("系统繁忙请重试!");
    }

    @Override
    public ApiResult<CtidAuthReturnBean> authCtidRequest(String businessSerialNumber, String authMode,
                                                         String photoData, String idcardAuthData, String authCode, String authApplyRetainData) {
        try {
            //获得下载请求数据
            AuthenticationData authenticationData = authDataUtil.getAuthenticationData(businessSerialNumber, authMode, photoData, authCode, idcardAuthData, authApplyRetainData);
            //发送下载申请
//			String httpResponse = hs.HttpResponse(ctidUrl.getAuthUrl(), JSONObject.fromObject(authenticationData), count);
            String httpResponse = restTemplateUtils.post(ctidUrl.getAuthUrl(), authenticationData);

            if (httpResponse == null) {
                return ApiResult.error("通信超时！请重试！");
            }
            logger.debug("网证认证请求应答数据:" + httpResponse);

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            AuthenticationReturnData.BizPackageBean bizP = JsonUtils.json2Bean(bizPackageStr, AuthenticationReturnData.BizPackageBean.class);
            if ("true".equals(bizP.getSuccess())) {

                String authResult = bizP.getAuthResult();

                CtidAuthReturnBean ctidAuthReturnBean = new CtidAuthReturnBean();

                ctidAuthReturnBean.setBsn(businessSerialNumber);
                ctidAuthReturnBean.setAuthMode(authMode);
                ctidAuthReturnBean.setAuthCode(authResult);
                ctidAuthReturnBean.setAuthDesc(AuthResultFinder.findAuthResultDesc(authResult));
                ctidAuthReturnBean.setAuthResult(AuthResultFinder.getAuthResultForApiAuth(authResult));

                JSONObject authResultRetainJO = JSONObject.fromObject(bizP.getAuthResultRetainData());
                if (authResultRetainJO.containsKey("rxfs")) {
                    ctidAuthReturnBean.setAuthNum(authResultRetainJO.getString("rxfs"));
                }
                if ("0x06".equals(authMode)) {
                    try {
                        String pid = authResultRetainJO.getString("grsfbs");
                        ctidAuthReturnBean.setPid(pid);
                    } catch (Exception e) {
                        logger.error("没有PID");
                    }
                }
                logger.info("认证结果>>" + JsonUtils.bean2Json(bizP));
                return ApiResult.ok(ctidAuthReturnBean);
            } else {
                logger.error("网证认证请求返回数据！..{}" ,httpResponse);
                return ApiResult.error(bizP.getErrorDesc());
            }
        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("系统繁忙请重试!");
    }

}
