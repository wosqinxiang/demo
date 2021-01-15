package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidMessage;
import com.ahdms.api.service.PhoneSmsCodeService;
import com.ahdms.auth.common.*;
import com.ahdms.auth.core.DataSignature;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.CtidResponseData;
import com.ahdms.auth.model.PhoneSmsCodeData;
import com.ahdms.auth.model.PhoneSmsCodeReturnData;
import com.alibaba.dubbo.config.annotation.Service;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-07-20 15:43
 */
@Service(group="${bjca.group}",version="${dubbo.version}")
@Component
public class PhoneSmsCodeServiceImpl implements PhoneSmsCodeService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneSmsCodeServiceImpl.class);

    @Autowired
    private PKCS7Tool p7Tool;

    @Autowired
    private AuthConstant authConstant;

    @Autowired
    private DataSignature ds;

    @Autowired
    private RestTemplateUtils restTemplateUtils;

    @Autowired
    private Configuration configuration;

    @Override
    public ApiResult send(String businessSerialNumber,String mobile,String smsCode,String mode) {

        try {
            smsCode = RamdonUtils.randomNumeric(6);
            mode = "0x13";
            //加密phoneData
            PhoneSmsCodeData phoneSmsCodeData = this.getPhoneSmsCodeData(businessSerialNumber, mobile, smsCode, mode);

            String httpResult = restTemplateUtils.post(configuration.getPhoneSendUrl(), phoneSmsCodeData);
            if(null == httpResult){
                return ApiResult.error("通信超时！请重试！");
            }
            logger.debug("httpResult....{}",httpResult);

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResult);

            PhoneSmsCodeReturnData.BizPackageBean resultData = JsonUtils.json2Bean(bizPackageStr,PhoneSmsCodeReturnData.BizPackageBean.class);
            if(resultData.isSuccess()){
                String bsn = resultData.getBusinessSerialNumber();
                ApplyReturnBean applyReturnBean = new ApplyReturnBean(bsn, null);
                return ApiResult.ok(applyReturnBean);
            }else{
                logger.info("验证码发送返回....{}",httpResult);
                String errorDesc = resultData.getErrorDesc();
                return ApiResult.error(errorDesc);
            }

        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return ApiResult.error("系统繁忙请重试!");
    }

    @Override
    public ApiResult auth(String businessSerialNumber,String mobile,String smsCode,String mode) {
        try {
            mode = "0x13";
            //加密phoneData
            PhoneSmsCodeData phoneSmsCodeData = this.getPhoneSmsCodeData(businessSerialNumber, mobile, smsCode, mode);

            String httpResult = restTemplateUtils.post(configuration.getPhoneAuthUrl(), phoneSmsCodeData);
            if(null == httpResult){
                return ApiResult.error("通信超时！请重试！");
            }

            //响应数据验签
            String bizPackageStr = p7Tool.signVerifier(httpResult);
            logger.debug("返回值>>"+bizPackageStr);

            PhoneSmsCodeReturnData.BizPackageBean resultData = JsonUtils.json2Bean(bizPackageStr,PhoneSmsCodeReturnData.BizPackageBean.class);
            if(resultData.isSuccess()){
                String authResult = resultData.getAuthResult();
                JSONObject resultJsonObj = JSONObject
                        .fromObject(resultData.getAuthResultRetainData());
                CtidMessage ctidMessage = (CtidMessage) JSONObject.toBean(resultJsonObj, CtidMessage.class);
                if("0XXX".equals(authResult) || "00XX".equals(authResult)){
                    if("0".equals(ctidMessage.getResult())){
                        return ApiResult.ok(ctidMessage);
                    }
                }
                int code = DownloadResultFinder.findDownloadCode(ctidMessage.getResult());
                return ApiResult.error(code,DownloadResultFinder.findDownloadResult(ctidMessage.getResult()));
            }else{
                logger.info("验证码验证返回....{}",httpResult);
                String errorDesc = resultData.getErrorDesc();
                return ApiResult.error(errorDesc);
            }

        } catch (ApiException e) {
            return ApiResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return ApiResult.error("系统繁忙请重试!");
    }

    private String encodePhoneData(String phone,String smsCode){
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("vcode",smsCode);
        try {
            return p7Tool.encodeLocal(JsonUtils.bean2Json(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private PhoneSmsCodeData getPhoneSmsCodeData(String businessSerialNumber,String mobile,String smsCode,String mode) throws Exception {
        PhoneSmsCodeData data = new PhoneSmsCodeData();

        PhoneSmsCodeData.BizPackageBean biz = new PhoneSmsCodeData.BizPackageBean();
        biz.setAppName("身份时空");
        biz.setAuthMode(mode);
        biz.setBusinessSerialNumber(businessSerialNumber);
        biz.setCustomNumber(authConstant.getCustomerNumber());
        biz.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
        biz.setPhoneData(encodePhoneData(mobile,smsCode));

        String sign = ds.signDataByP7DetachForJitWithCount(JsonUtils.bean2Json(biz),3);
        if(sign==null){
            throw new ApiException("签名失败！");
        }
        data.setBizPackage(biz);
        data.setSign(sign);
        return data;
    }
}
