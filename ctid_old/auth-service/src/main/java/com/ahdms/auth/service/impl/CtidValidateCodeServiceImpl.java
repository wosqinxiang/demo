package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.ValidateCodeRequestReturnBean;
import com.ahdms.api.service.CtidValidateCodeService;
import com.ahdms.auth.common.*;
import com.ahdms.auth.core.DataSignature;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.ValidateCodeApplyData;
import com.ahdms.auth.model.ValidateCodeApplyReturnData;
import com.ahdms.auth.model.ValidateCodeRequestData;
import com.ahdms.auth.model.ValidateCodeRequestReturnData;
import com.alibaba.dubbo.config.annotation.Service;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(group = "${bjca.group}", version = "${dubbo.version}")
@Component
public class CtidValidateCodeServiceImpl implements CtidValidateCodeService {

    Logger logger = LoggerFactory.getLogger(CtidValidateCodeServiceImpl.class);

    @Autowired
    private DataSignature ds;

    @Autowired
    private PKCS7Tool p7Tool;

    @Autowired
    private Configuration ctidUrl;

    @Autowired
    private AuthConstant authConstant;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Override
    public ApiResult<ApplyReturnBean> validateCodeApply(String applyData, Integer authMode) {
        try {
            ValidateCodeApplyData data = new ValidateCodeApplyData();
            ValidateCodeApplyData.BizPackageBean bizPackage = new ValidateCodeApplyData.BizPackageBean();
            bizPackage.setApplyData(applyData);
            bizPackage.setCustNum(authConstant.getCustomerNumber());
            bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
            bizPackage.setAuthCodeControlVersion(authConstant.getAuthCodeControlVersion());
            bizPackage.setCardReaderVersion(authConstant.getCardReaderVersion());
            bizPackage.setLiveDetectCtrlVer(authConstant.getLiveDetectionControlVersion());
            bizPackage.setAuthMode(authMode);

            String sign = ds.signDataByP7DetachForJitWithCount(JsonUtils.bean2Json(bizPackage), 3);
            if (sign == null) {
                return ApiResult.error("签名失败！请重试！");
            }
            data.setSign(sign);
            data.setBizPackage(bizPackage);

            String httpResponse = restTemplateUtils.post(ctidUrl.getValidateCodeApplyUrl(), data);

//			String httpResponse = hs.HttpResponse(ctidUrl.getValidateCodeApplyUrl(), JSONObject.fromObject(data));

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            ValidateCodeApplyReturnData.BizPackageBean biz = JsonUtils.json2Bean(bizPackageStr, ValidateCodeApplyReturnData.BizPackageBean.class);
            if (biz.getSuccess()) {
                ApplyReturnBean applyReturnBean = new ApplyReturnBean(biz.getBizSerialNum(), biz.getRandomNumber());
                return ApiResult.ok(applyReturnBean);
            } else {
                return ApiResult.error(biz.getErrorDesc());
            }
        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("服务器异常!请重试!");
    }

    @Override
    public ApiResult<ValidateCodeRequestReturnBean> validateCodeRequest(String bizSerialNum, Integer authMode,
                                                                        String authCode, String photoData, String idcardAuthData) {
        try {
            ValidateCodeRequestData requestData = new ValidateCodeRequestData();
            ValidateCodeRequestData.BizPackageBean bizPackage = new ValidateCodeRequestData.BizPackageBean();
            bizPackage.setBizSerialNum(bizSerialNum);
            bizPackage.setCustNum(authConstant.getCustomerNumber());
            bizPackage.setRequestTime(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
            bizPackage.setAuthCode(authCode);  //认证码
            bizPackage.setAuthMode(authMode);
            bizPackage.setAuthApplyRetainData(""); //认证保留数据 非必填
            bizPackage.setIdcardAuthData(idcardAuthData); //id验证数据
            bizPackage.setPhotoData(photoData);  //照片数据

            String sign = ds.signDataByP7DetachForJitWithCount(JSONObject.fromObject(bizPackage).toString(), 3);
            if (sign == null) {
                return ApiResult.error("签名失败！请重试！");
            }
            requestData.setSign(sign);
            requestData.setBizPackage(bizPackage);

            String httpResponse = restTemplateUtils.post(ctidUrl.getValidateCodeRequestUrl(), requestData);
//			String httpResponse = hs.HttpResponse(ctidUrl.getValidateCodeRequestUrl(), JSONObject.fromObject(requestData));
            logger.debug(">>>二维码验码返回:{}", httpResponse);
            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            ValidateCodeRequestReturnData.BizPackageBean biz = JsonUtils.json2Bean(bizPackageStr, ValidateCodeRequestReturnData.BizPackageBean.class);

            if (biz.getSuccess()) {
                String pid = biz.getAuthResultRetainData();
                ValidateCodeRequestReturnBean bean = new ValidateCodeRequestReturnBean(pid);
                return ApiResult.ok(bean);
            } else {
                logger.info("二维码验码返回....{}",httpResponse);
                return ApiResult.error(biz.getErrorDesc());
            }

        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("服务器异常!请重试!");
    }

}
