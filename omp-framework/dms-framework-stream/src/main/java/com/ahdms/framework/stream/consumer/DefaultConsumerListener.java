package com.ahdms.framework.stream.consumer;


import com.ahdms.framework.stream.client.Message;
import com.ahdms.framework.stream.input.DefaultInput;
import com.ahdms.framework.stream.logger.InputLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:13
 */
@Slf4j
@Configuration
@Conditional(DefaultConsumerListener.ConsumerCondition.class)
@EnableBinding(value = {DefaultInput.class})
public class DefaultConsumerListener extends AbstractConsumerListener {

    public DefaultConsumerListener(ObjectProvider<Map<String, StreamHandler>> streamHandlerProvider, InputLogger inputLogger) {
        super(streamHandlerProvider, inputLogger);
    }

    @StreamListener(DefaultInput.DEFAULT_INPUT)
    public <T, D extends Message<T>> void onMessage(D message) {
        super.onMessageListener(message);
    }

    public static class ConsumerCondition implements Condition {
        private final static String INPUT_ENABLE = "spring.cloud.stream.bindings." + DefaultInput.DEFAULT_INPUT + ".destination";
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return context.getEnvironment().containsProperty(INPUT_ENABLE);
        }
    }
}
