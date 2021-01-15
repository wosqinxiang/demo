package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.api.service.CtidDownService;
import com.ahdms.auth.common.*;
import com.ahdms.auth.core.DataSignature;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.*;
import com.alibaba.dubbo.config.annotation.Service;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Service(group = "${bjca.group}", version = "${dubbo.version}",actives=5)
@Component
public class CtidDownServiceImpl implements CtidDownService {

    private static final Logger logger = LoggerFactory.getLogger(CtidDownServiceImpl.class);

    @Autowired
    private AuthDataUtil authDataUtil;

    @Autowired
    private Configuration ctidUrl;

    @Autowired
    private DataSignature ds;

    @Autowired
    private PKCS7Tool p7Tool;

    @Value("${http.count:3}")
    private Integer count;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Override
    public ApiResult<ApplyReturnBean> downCtidApply(String authMode) {
        try {
            //获取申请数据
            AuthenticationApplicationData authenticationData = authDataUtil.getAuthenticationData(authMode);
            //发送下载申请
//			String httpResponse = hs.HttpResponse(ctidUrl.getDownloadRequestUrl(), JSONObject.fromObject(authenticationData), count);
            String httpResponse = restTemplateUtils.post(ctidUrl.getDownloadRequestUrl(), authenticationData);

            if (httpResponse == null) {
                return ApiResult.error("通信超时！请重试！");
            }
            logger.debug("网证下载申请应答数据:" + httpResponse);

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
            e.printStackTrace();
        }
        return ApiResult.error("系统繁忙请重试!");
    }

    @Override
    public ApiResult<CtidMessage> downCtidRequest(String businessSerialNumber, String authMode, String photoData,
                                                  String idcardAuthData, String authCode, String authApplyRetainData) {
        try {
            //获得下载请求数据
            AuthenticationData authenticationData = authDataUtil.getAuthenticationData(businessSerialNumber, authMode, photoData, authCode, idcardAuthData, authApplyRetainData);
            //发送下载申请
//            logger.info(JSONObject.fromObject(authenticationData).toString());

            String httpResponse = restTemplateUtils.post(ctidUrl.getDownloadUrl(), authenticationData);

            if (httpResponse == null) {
                return ApiResult.error("通信超时！请重试！");
            }
            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            AuthenticationReturnData.BizPackageBean bizP = JsonUtils.json2Bean(bizPackageStr
                    , AuthenticationReturnData.BizPackageBean.class);

            logger.debug("网证下载请求应答数据:" + httpResponse);
            if ("true".equals(bizP.getSuccess())) {
                String authResult = bizP.getAuthResult();
                JSONObject resultJsonObj = JSONObject
                        .fromObject(bizP.getAuthResultRetainData());
                CtidMessage ctidMessage = (CtidMessage) JSONObject.toBean(resultJsonObj, CtidMessage.class);
                if ("0XXX".equals(authResult) || "00XX".equals(authResult)) {
                    if ("0".equals(ctidMessage.getResult())) {
                        return ApiResult.ok(ctidMessage);
                    } else {
                        return ApiResult.error(DownloadResultFinder.findDownloadResult(ctidMessage.getResult()));
                    }
                } else if ("XXXX".equals(authResult)) {
                    if ("2".equals(ctidMessage.getResult()) || "23".equals(ctidMessage.getResult())) {
                        ctidMessage.setGrsfbs(bizP.getBusinessSerialNumber());
                        return new ApiResult<>(2, DownloadResultFinder.findDownloadResult(ctidMessage.getResult()), ctidMessage);
                    }
                }
                logger.info("网证下载失败！..{}" ,httpResponse);
                int code = DownloadResultFinder.findDownloadCode(ctidMessage.getResult());
                return ApiResult.error(code, DownloadResultFinder.findDownloadResult(ctidMessage.getResult()));
            } else {
                logger.error("网证下载失败！..{}" ,httpResponse);
                return ApiResult.error("网证下载失败！请重试！");
            }
        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("系统繁忙请重试!");
    }

    @Override
    public ApiResult<CtidMessage> downCtidRequest(String businessSerialNumber, String authMode, String photoData,
                                                  String idcardAuthData, String authCode, String cardName, String cardNum, String cardStart,
                                                  String cardEnd) {
        String authApplyRetainData = null;
        try {
            ReservedDataEntity.SFXXBean sfxx = getSFXXBeanEntity(cardName, cardNum, cardStart, cardEnd);
            authApplyRetainData = p7Tool.encodeLocal(JsonUtils.bean2Json(sfxx));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downCtidRequest(businessSerialNumber, authMode, photoData, idcardAuthData, authCode, authApplyRetainData);
    }

    private ReservedDataEntity.SFXXBean getSFXXBeanEntity(String xM, String gMSFZHM, String yXQQSRQ, String yXQJZRQ) {
        ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
        sfxx.setxM(xM);
        sfxx.setgMSFZHM(gMSFZHM);
        sfxx.setyXQQSRQ(yXQQSRQ);
        sfxx.setyXQJZRQ(yXQJZRQ);
        return sfxx;
    }


}
