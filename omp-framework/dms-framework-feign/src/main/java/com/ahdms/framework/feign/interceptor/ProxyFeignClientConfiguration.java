package com.ahdms.framework.feign.interceptor;

import com.ahdms.framework.feign.logger.DefaultFeignAccessLogger;
import com.ahdms.framework.feign.logger.FeignAccessLogger;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuzhou
 * @date 2018/2/28
 * @time 17:42
 * @since 
 */
@Configuration
public class ProxyFeignClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FeignAccessLogger feignAccessLogger() {
        return new DefaultFeignAccessLogger();
    }

    @Bean
    public MethodInterceptor feignResponseLoggingInterceptor(FeignAccessLogger feignAccessLogger) {
        return new FeignResponseLoggingInterceptor(feignAccessLogger);
    }


    @Bean
    public AbstractPointcutAdvisor feignClientPointcutAdvisor(MethodInterceptor feignResponseLoggingInterceptor) {
        BeanFactoryFeignClientPointcutAdvisor advisor = new BeanFactoryFeignClientPointcutAdvisor();
        advisor.setAdvice(feignResponseLoggingInterceptor);
        advisor.setOrder(1);
        return advisor;
    }
}
