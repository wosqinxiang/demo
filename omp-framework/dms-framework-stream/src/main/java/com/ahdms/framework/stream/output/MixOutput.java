package com.ahdms.framework.stream.output;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:55
 */
@Configuration
public class MixOutput {
    public static final String OUTPUT_DEFAULT = "output";
    public static final String OUTPUT_ONE = "output1";
    public static final String OUTPUT_TWO = "output2";
    public static final String OUTPUT_THREE = "output3";
    public static final String OUTPUT_DEAD = "output-dead";

    @ConditionalOnProperty(value = DefaultSender.ENABLE, matchIfMissing = true)
    @EnableBinding(DefaultSender.class)
    public interface DefaultSender {
        String ENABLE = "spring.cloud.stream.bindings." + MixOutput.OUTPUT_DEFAULT + ".enabled";

        @Output(MixOutput.OUTPUT_DEFAULT)
        MessageChannel output();
    }

    @ConditionalOnProperty(value = OneSender.ENABLE)
    @EnableBinding(OneSender.class)
    public interface OneSender {
        String ENABLE = "spring.cloud.stream.bindings." + MixOutput.OUTPUT_ONE + ".enabled";

        @Output(MixOutput.OUTPUT_ONE)
        MessageChannel output();
    }

    @ConditionalOnProperty(value = TwoSender.ENABLE)
    @EnableBinding(TwoSender.class)
    public interface TwoSender {
        String ENABLE = "spring.cloud.stream.bindings." + MixOutput.OUTPUT_TWO + ".enabled";

        @Output(MixOutput.OUTPUT_TWO)
        MessageChannel output();
    }

    @ConditionalOnProperty(value = ThreeSender.ENABLE)
    @EnableBinding(ThreeSender.class)
    public interface ThreeSender {
        String ENABLE = "spring.cloud.stream.bindings." + MixOutput.OUTPUT_THREE + ".enabled";

        @Output(MixOutput.OUTPUT_THREE)
        MixOutput output();
    }
    @ConditionalOnProperty(value = ErrorSender.ENABLE)
    @EnableBinding(ErrorSender.class)
    public interface ErrorSender {
        String ENABLE = "spring.cloud.stream.bindings." + MixOutput.OUTPUT_DEAD + ".enabled";

        @Output(MixOutput.OUTPUT_DEAD)
        MixOutput output();
    }
}
