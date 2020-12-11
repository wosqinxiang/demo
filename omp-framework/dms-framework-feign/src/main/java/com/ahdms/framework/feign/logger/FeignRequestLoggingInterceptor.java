package com.ahdms.framework.feign.logger;

import com.ahdms.framework.feign.logger.FeignAccessLogger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/22 16:47
 */
@AllArgsConstructor
public class FeignRequestLoggingInterceptor implements RequestInterceptor {

    private FeignAccessLogger feignAccessLogger;

    @Override
    public void apply(RequestTemplate template) {
        feignAccessLogger.request(template);
    }
}
