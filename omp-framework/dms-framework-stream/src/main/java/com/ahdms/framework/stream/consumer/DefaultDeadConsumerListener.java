package com.ahdms.framework.stream.consumer;


import com.ahdms.framework.stream.client.Message;
import com.ahdms.framework.stream.input.DefaultDeadInput;
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
@Conditional(DefaultDeadConsumerListener.DeadConsumerCondition.class)
@EnableBinding(value = {DefaultDeadInput.class})
public class DefaultDeadConsumerListener extends AbstractDeadConsumerListener {

    public DefaultDeadConsumerListener(ObjectProvider<Map<String, StreamDeadHandler>> streamDeadHandlerProvider, InputLogger inputLogger) {
        super(streamDeadHandlerProvider, inputLogger);
    }

    @StreamListener(DefaultDeadInput.DEFAULT_DEAD_INPUT)
    public <T, D extends Message<T>> void onDeadMessage(D message) {
        super.onDeadMessageListener(message);
    }

    public static class DeadConsumerCondition implements Condition {
        private final static String DEAD_INPUT_ENABLE = "spring.cloud.stream.bindings." + DefaultDeadInput.DEFAULT_DEAD_INPUT + ".destination";
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return context.getEnvironment().containsProperty(DEAD_INPUT_ENABLE);
        }
    }
}
