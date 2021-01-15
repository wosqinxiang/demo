package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CreateCodeRequestReturnBean;
import com.ahdms.api.service.CtidCreateCodeService;
import com.ahdms.auth.common.*;
import com.ahdms.auth.constants.QrCodeErrorCodeFinder;
import com.ahdms.auth.core.DataSignature;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.CreateCodeApplyData;
import com.ahdms.auth.model.CreateCodeApplyReturnData;
import com.ahdms.auth.model.CreateCodeRequestData;
import com.ahdms.auth.model.CreateCodeRequestReturnData;
import com.alibaba.dubbo.config.annotation.Service;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(group = "${bjca.group}", version = "${dubbo.version}")
@Component
public class CtidCreateCodeServiceImpl implements CtidCreateCodeService {

    Logger logger = LoggerFactory.getLogger(CtidCreateCodeServiceImpl.class);

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
    public ApiResult<ApplyReturnBean> createCodeApply(String applyData) {
        ApiResult<ApplyReturnBean> result = new ApiResult<ApplyReturnBean>();
        try {
            CreateCodeApplyData data = new CreateCodeApplyData();
            CreateCodeApplyData.BizPackageBean bizPackage = new CreateCodeApplyData.BizPackageBean();
            bizPackage.setApplyData(applyData);
            bizPackage.setCustNum(authConstant.getCustomerNumber());
            bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
            String sign = ds.signDataByP7DetachForJitWithCount(JSONObject.fromObject(bizPackage).toString(), 3);
            if (sign == null) {
                return ApiResult.error("签名失败！请重试！");
            }
            data.setSign(sign);
            data.setBizPackage(bizPackage);

            String httpResponse = restTemplateUtils.post(ctidUrl.getCreateCodeApplyUrl(), data);

//			String httpResponse = hs.HttpResponse(ctidUrl.getCreateCodeApplyUrl(), JSONObject.fromObject(data));
            logger.debug("二维码申请返回数据>>>>>>" + httpResponse);

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            CreateCodeApplyReturnData.BizPackageBean responseBiz = JsonUtils.json2Bean(bizPackageStr, CreateCodeApplyReturnData.BizPackageBean.class);

            if (responseBiz.getSuccess()) {
                ApplyReturnBean applyReturnBean = new ApplyReturnBean(responseBiz.getBizSerialNum(), responseBiz.getRandomNumber());
                return ApiResult.ok(applyReturnBean);
            } else {
                return ApiResult.error(responseBiz.getErrorDesc());
            }

        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setCode(1);
            result.setMessage("服务器出错！请重试！");
            return result;
        }
    }

    @Override
    public ApiResult<CreateCodeRequestReturnBean> createCodeRequest(String bizSerialNum, String checkData,
                                                                    Integer isPic) {
        try {
            CreateCodeRequestData requestData = new CreateCodeRequestData();
            CreateCodeRequestData.BizPackageBean bizPackage = new CreateCodeRequestData.BizPackageBean();
            bizPackage.setBizSerialNum(bizSerialNum);
            bizPackage.setCheckData(checkData);
            bizPackage.setCustNum(authConstant.getCustomerNumber());
            bizPackage.setIsPic(isPic);
            bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
            String sign = ds.signDataByP7DetachForJitWithCount(JSONObject.fromObject(bizPackage).toString(), 3);
            if (sign == null) {
                return ApiResult.error("签名失败！请重试！");
            }
            requestData.setSign(sign);
            requestData.setBizPackage(bizPackage);

            String httpResponse = restTemplateUtils.post(ctidUrl.getCreateCodeRequestUrl(), requestData);
//			String httpResponse = hs.HttpResponse(ctidUrl.getCreateCodeRequestUrl(), JSONObject.fromObject(requestData));
            logger.debug("二维码请求返回数据>>>>>>" + httpResponse);

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResponse);

            CreateCodeRequestReturnData.BizPackageBean biz = JsonUtils.json2Bean(bizPackageStr, CreateCodeRequestReturnData.BizPackageBean.class);
            if (biz.getSuccess()) {
                CreateCodeRequestReturnBean bean = new CreateCodeRequestReturnBean();
                bean.setCodeContent(biz.getCodeContent());
                bean.setImgStream(biz.getImgStream());
                bean.setWidth(biz.getWidth() == null ? 0 : biz.getWidth());
                return ApiResult.ok(bean);
            } else {
                logger.error("二维码赋码请求返回数据！..{}" ,httpResponse);
                String errorCode = biz.getErrorCode();
                return ApiResult.error(QrCodeErrorCodeFinder.findCode(errorCode), biz.getErrorDesc());
            }

        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ApiResult.error("服务器异常!请重试!");
    }

}
