package com.ahdms.framework.feign.interceptor;

import com.ahdms.framework.feign.logger.FeignAccessLogger;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Date;

/**
 * @author yuzhou
 * @date 2018/2/28
 * @time 17:37
 * @since 2.0.0
 */
@AllArgsConstructor
public class FeignResponseLoggingInterceptor implements MethodInterceptor {

    private FeignAccessLogger feignAccessLogger;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            Date start = new Date();
            Object result = invocation.proceed();
            this.feignAccessLogger.response(result, start);
            return result;
        } catch (Exception e) {
            this.feignAccessLogger.response(e);
            throw e;
        }
    }

}
