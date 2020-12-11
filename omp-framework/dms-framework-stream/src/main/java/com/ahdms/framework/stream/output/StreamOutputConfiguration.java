package com.ahdms.framework.stream.output;

import com.ahdms.framework.stream.client.BindingMetadataHolder;
import com.ahdms.framework.stream.client.StreamInterceptor;
import com.ahdms.framework.stream.client.interceptor.OutputLogInterceptor;
import com.ahdms.framework.stream.logger.DefaultOutputLogger;
import com.ahdms.framework.stream.logger.OutputLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:55
 */
@Configuration
public class StreamOutputConfiguration {

    @Bean
    public BindingMetadataHolder bindingMetadataHolder() {
        return new BindingMetadataHolder();
    }

    @Bean
    public OutputLogger defaultOutputLogger() {
        return new DefaultOutputLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public StreamInterceptor streamLoggerInterceptor(OutputLogger outputLogger) {
        return new OutputLogInterceptor(outputLogger);
    }

}
