package com.ahdms.framework.stream;

import com.ahdms.framework.stream.consumer.*;
import com.ahdms.framework.stream.input.DefaultDeadInput;
import com.ahdms.framework.stream.input.DefaultInput;
import com.ahdms.framework.stream.logger.DefaultInputLogger;
import com.ahdms.framework.stream.logger.InputLogger;
import com.ahdms.framework.stream.output.MixOutput;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.annotation.StreamMessageConverter;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.messaging.converter.MessageConverter;

import java.util.Map;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 11:44
 */
@Configuration
@ConditionalOnClass({MixOutput.class})
@Import({DefaultConsumerListener.class, DefaultDeadConsumerListener.class, MixOutput.class})
public class StreamAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InputLogger inputLogger() {
        return new DefaultInputLogger();
    }

    @Bean
    @StreamMessageConverter
    public MessageConverter mappingJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

}
