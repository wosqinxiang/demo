package com.ahdms.auth.common;

import com.ahdms.auth.core.DataSignature;
import com.ahdms.auth.exception.ApiException;
import com.ahdms.auth.model.AuthenticationApplicationData;
import com.ahdms.auth.model.AuthenticationApplicationReturnData;
import com.ahdms.auth.model.AuthenticationData;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class AuthDataUtil {
    Logger logger = LoggerFactory.getLogger(AuthDataUtil.class);

    @Autowired
    private DataSignature ds;

    @Autowired
    private AuthConstant authConstant;

    private static final int COUNT = 3;

    // 获得 认证申请 数据包
    public AuthenticationApplicationData getAuthenticationData(String authMode) throws Exception {
        AuthenticationApplicationData authenticationData = new AuthenticationApplicationData();
        AuthenticationApplicationData.BizPackageBean authApplyBizPackage = getAuthApplyBizPackage(authMode);
        // 对数据签名
        JSONObject jsonObj = JSONObject.fromObject(authApplyBizPackage);
        String sign = ds.signDataByP7DetachForJitWithCount(jsonObj.toString(), COUNT);
        if (sign == null) {
            throw new ApiException("签名失败！");
        }
        authenticationData.setBizPackage(authApplyBizPackage);
        authenticationData.setSign(sign);
        return authenticationData;
    }

    private AuthenticationApplicationData.BizPackageBean getAuthApplyBizPackage(String authMode) {
        AuthenticationApplicationData.BizPackageBean bizPackage = new AuthenticationApplicationData.BizPackageBean();
        try {
            bizPackage.setAppName(new String(authConstant.getAppName().getBytes("ISO-8859-1"), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            bizPackage.setAppName("身份时空");
        }
        bizPackage.setAppName("身份时空");
        bizPackage.setCustomerNumber(authConstant.getCustomerNumber());
        bizPackage.setLiveDetectionControlVersion(authConstant.getLiveDetectionControlVersion());
        bizPackage.setAuthCodeControlVersion(authConstant.getAuthCodeControlVersion());
        bizPackage.setCardReaderVersion(authConstant.getCardReaderVersion());
        bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
        bizPackage.setAuthMode(authMode);
        return bizPackage;
    }

    // 获取 认证请求 数据包
    public AuthenticationData getAuthenticationData(
            AuthenticationApplicationReturnData.BizPackageBean authApplyBizPackage, String mode, String picStr,
            String vcodeStr, String idCheck, String encodeReservedData) throws Exception {
        AuthenticationData authenticationData = new AuthenticationData();
        AuthenticationData.BizPackageBean authRequestBizPackage = getAuthRequestBizPackage(authApplyBizPackage, mode,
                picStr, vcodeStr, idCheck, encodeReservedData);
        // 对数据签名
        String sign = ds.signDataByP7DetachForJitWithCount(JsonUtils.bean2Json(authRequestBizPackage), COUNT);
        if (sign == null) {
            throw new ApiException("签名失败！");
        }
        authenticationData.setBizPackage(authRequestBizPackage);
        authenticationData.setSign(sign);
        return authenticationData;
    }

    private AuthenticationData.BizPackageBean getAuthRequestBizPackage(
            AuthenticationApplicationReturnData.BizPackageBean bizP, String mode, String picStr, String vcodeStr,
            String idCheck, String encodeReservedData) {
        AuthenticationData.BizPackageBean bizPackage = new AuthenticationData.BizPackageBean();
        bizPackage.setAppName(bizP.getAppName());
        bizPackage.setCustomNumber(bizP.getCustomerNumber());
        bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
        bizPackage.setBusinessSerialNumber(bizP.getBusinessSerialNumber());

        bizPackage.setAuthMode(mode);
        bizPackage.setAuthCode(vcodeStr);
        bizPackage.setPhotoData(picStr);
        bizPackage.setAuthApplyRetainData(encodeReservedData);
        bizPackage.setIdcardAuthData(idCheck);

        return bizPackage;
    }

    // 获取 认证请求 数据包
    public AuthenticationData getAuthenticationData(
            String businessSerialNumber, String mode, String picStr,
            String vcodeStr, String idCheck, String encodeReservedData) throws Exception {
        AuthenticationData authenticationData = new AuthenticationData();
        AuthenticationData.BizPackageBean authRequestBizPackage = getAuthRequestBizPackage(businessSerialNumber, mode,
                picStr, vcodeStr, idCheck, encodeReservedData);
        // 对数据签名
        String sign = ds.signDataByP7DetachForJitWithCount(JsonUtils.bean2Json(authRequestBizPackage), COUNT);
        if (sign == null) {
            throw new ApiException("签名失败！");
        }
        authenticationData.setBizPackage(authRequestBizPackage);
        authenticationData.setSign(sign);
        return authenticationData;
    }

    /**
     * <p>Title: </p>
     * <p>Description: </p>
     *
     * @param businessSerialNumber
     * @param mode
     * @param picStr
     * @param vcodeStr
     * @param idCheck
     * @param encodeReservedData
     * @return
     */
    private AuthenticationData.BizPackageBean getAuthRequestBizPackage(
            String businessSerialNumber, String mode, String picStr, String vcodeStr, String idCheck,
            String encodeReservedData) {
        AuthenticationData.BizPackageBean bizPackage = new AuthenticationData.BizPackageBean();
//			try {
//				bizPackage.setAppName(new String(authConstant.getAppName().getBytes("ISO-8859-1"),"UTF-8"));
//			} catch (UnsupportedEncodingException e) {
//				bizPackage.setAppName("身份时空");
//			}
        bizPackage.setAppName("身份时空");
        bizPackage.setCustomNumber(authConstant.getCustomerNumber());
        bizPackage.setTimeStamp(DateUtils.getCurrentDateTime(DateUtils.MILLS_FORMATTER));
        bizPackage.setBusinessSerialNumber(businessSerialNumber);

        bizPackage.setAuthMode(mode);
        bizPackage.setAuthCode(vcodeStr);
        bizPackage.setPhotoData(picStr);
        bizPackage.setAuthApplyRetainData(encodeReservedData);
        bizPackage.setIdcardAuthData(idCheck);
        return bizPackage;
    }

}
