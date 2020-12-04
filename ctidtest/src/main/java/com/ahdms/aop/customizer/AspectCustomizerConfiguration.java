package com.ahdms.aop.customizer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 11:09
 */
@Configuration
public class AspectCustomizerConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AspectBeforeProcess aspectBeforeProcess() {
        return new AspectBeforeProcess.DefaultAspectBeforeProcess();
    }

    @Bean
    @ConditionalOnMissingBean
    public AspectThrowableProcess aspectThrowableProcess() {
        return new AspectThrowableProcess.DefaultAspectThrowableProcess();
    }

    @Bean
    @ConditionalOnMissingBean
    public AspectAfterProcess aspectAfterProcess() {
        return new AspectAfterProcess.DefaultAspectAfterProcess();
    }
}

