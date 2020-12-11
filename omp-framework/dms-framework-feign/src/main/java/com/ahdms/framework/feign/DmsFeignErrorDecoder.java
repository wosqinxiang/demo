package com.ahdms.framework.feign;

import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.constant.Constant;
import com.ahdms.framework.core.constant.SystemError;
import com.ahdms.framework.core.exception.BusinessException;
import com.ahdms.framework.core.exception.FrameworkException;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * feign ErrorDecoder
 * 用于解析(序列化成我们框架能识别的异常方案)
 */
@Slf4j
public class DmsFeignErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            return new FrameworkException(SystemError.CLIENT_EXCEPTION);
        }
        try {
            String body = Util.toString(response.body().asReader());
            Map<String, Object> mapBody = JsonUtils.readValue(body, Map.class);
            if (mapBody.containsKey(Constant.DEFAULT_RESPONSE_CODE)) {
                String code = (String) mapBody.get(Constant.DEFAULT_RESPONSE_CODE);
                String message = (String) mapBody.get(Constant.DEFAULT_RESPONSE_MESSAGE);
                return new BusinessException(code, message);
            }
        } catch (IOException e) {
            return new FrameworkException(SystemError.CLIENT_EXCEPTION, e);
        }
        return new FrameworkException(SystemError.CLIENT_EXCEPTION);
    }
}
