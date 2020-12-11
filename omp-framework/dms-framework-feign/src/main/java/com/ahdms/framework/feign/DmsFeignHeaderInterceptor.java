package com.ahdms.framework.feign;

import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.constant.Constant;
import com.ahdms.framework.core.context.HeaderConstants;
import com.ahdms.framework.core.context.holder.DmsContextHolders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import java.util.Optional;

/**
 * feign context传递
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 15:38
 */
@Slf4j
public class DmsFeignHeaderInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate requestTemplate) {

        Optional.ofNullable(DmsContextHolders.getContext()).ifPresent(dmsContext ->
                requestTemplate.header(HeaderConstants.DMS_TOKEN, dmsContext.getToken())
                        .header(HeaderConstants.DMS_USER_ID, Optional.ofNullable(dmsContext.getUserId()).map(String::valueOf).orElse(StringPool.EMPTY))
                        .header(HeaderConstants.DMS_ROLE_ID, dmsContext.getRole())
                        .header(HeaderConstants.DMS_CLIENT_IP, dmsContext.getClientIp())
                        .header(HeaderConstants.DMS_USER_AGENT, dmsContext.getUserAgent())
                        .header(HeaderConstants.DMS_USER_AGENT, dmsContext.getUserAgent()));
        String requestId = ThreadContext.get(Constant.MDC_REQUEST_ID_KEY);
        if (StringUtils.isNotBlank(requestId)) {
            requestTemplate.header(Constant.MDC_REQUEST_ID_KEY, requestId);
        }
    }

}
