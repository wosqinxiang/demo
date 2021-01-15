package com.ahdms.ctidservice.service.impl;

import com.ahdms.api.model.ApiResult;
import com.ahdms.api.model.CtidAuthReturnBean;
import com.ahdms.api.service.AuthIdCardServcie;
import com.ahdms.ctidservice.common.CTIDConstans;
import com.ahdms.ctidservice.common.RedisOpsClient;
import com.ahdms.ctidservice.common.UUIDGenerator;
import com.ahdms.ctidservice.config.DubboUtils;
import com.ahdms.ctidservice.service.AuthIdCardService;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.ctidservice.util.CalculateHashUtils;
import com.ahdms.ctidservice.vo.CtidResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthIdCardServiceImpl implements AuthIdCardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthIdCardServiceImpl.class);

    @Autowired
    private DubboUtils dubboUtils;

    @Autowired
    private RedisOpsClient jedisCluster;

    @Autowired
    private TokenCipherService cipherSer;

    @Override
    public CtidResult<CtidAuthReturnBean> auth(String name, String idCard, String faceData, String serverId) {
        try {
            AuthIdCardServcie authService = dubboUtils.getDubboService(serverId, AuthIdCardServcie.class);
            ApiResult<CtidAuthReturnBean> auth = authService.auth(name, idCard, faceData);
            if (0 == auth.getCode()) {
                CtidAuthReturnBean apiAuthResult = auth.getData();
                String authResult = apiAuthResult.getAuthResult();
                if ("true".equals(authResult)) {
                    byte[] cardNumHash1 = CalculateHashUtils.calculateHash1(idCard.getBytes());
                    String encryptCardNumHash = cipherSer.encryptCTID(cardNumHash1);
                    String token = UUIDGenerator.getUUID();
                    jedisCluster.set(token, encryptCardNumHash, CTIDConstans.TOKEN_EXPIRE_SECONDS);
                    apiAuthResult.setToken(token);
                    return CtidResult.ok(apiAuthResult);
                }
            }
            return new CtidResult(auth.getCode(), auth.getMessage(), auth.getData());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return CtidResult.error("服务异常请重试");
    }

}
