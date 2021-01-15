package com.ahdms.auth.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.ApplyReturnBean;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.AuthIdCardServcie;
import com.ahdms.api.service.CtidAuthService;
import com.ahdms.auth.common.ImageUtils;
import com.ahdms.auth.common.JsonUtils;
import com.ahdms.auth.core.PKCS7Tool;
import com.ahdms.auth.model.ReservedDataEntity;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Service(group = "${bjca.group}", version = "${dubbo.version}")
@Component
public class AuthIdCardServiceImpl implements AuthIdCardServcie {

    @Autowired
    private CtidAuthService authCtidService;

    @Autowired
    private PKCS7Tool p7Tool;

    @Override
    public ApiResult auth(String name, String idCard, String faceData) {
        String mode = "0x42";
        if (StringUtils.isBlank(faceData) || "null".equalsIgnoreCase(faceData.trim())) {
            mode = "0x40";
        } else {
            boolean isImage = ImageUtils.isImage(faceData);
            if (!isImage) {
                return ApiResult.error("人像数据有误！");
            }
        }
        ApiResult<ApplyReturnBean> authCtidApply = authCtidService.authCtidApply(mode);
        if (0 == authCtidApply.getCode()) {
            ApplyReturnBean data = authCtidApply.getData();
            String bsn = data.getBusinessSerialNumber();
            ReservedDataEntity reservedDataEntity = getReservedDataEntity(name, idCard, null, null);
            String authApplyRetainData = null;
            try {
                authApplyRetainData = p7Tool.encodeLocal(JsonUtils.bean2Json(reservedDataEntity));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ApiResult<CtidAuthReturnBean> authResult = authCtidService.authCtidRequest(bsn, mode, faceData, null, null, authApplyRetainData);
            return authResult;
        } else {
            return ApiResult.error(authCtidApply.getMessage());
        }
    }

    public static ReservedDataEntity getReservedDataEntity(String xM, String gMSFZHM, String yXQQSRQ, String yXQJZRQ) {
        ReservedDataEntity reservedData = new ReservedDataEntity();
        ReservedDataEntity.SFXXBean sfxx = new ReservedDataEntity.SFXXBean();
        sfxx.setxM(xM);
        sfxx.setgMSFZHM(gMSFZHM);
        sfxx.setyXQQSRQ(yXQQSRQ);
        sfxx.setyXQJZRQ(yXQJZRQ);
        reservedData.setsFXX(sfxx);
        reservedData.setwZXX(new ReservedDataEntity.WZXXBean());
        reservedData.setzP(new ReservedDataEntity.ZPBean());
        return reservedData;
    }

}
