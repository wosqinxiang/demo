package com.ahdms.framework.stream.input;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:55
 */
public interface DefaultDeadInput {
    String DEFAULT_DEAD_INPUT = "input-dead";

    @Input(DefaultDeadInput.DEFAULT_DEAD_INPUT)
    SubscribableChannel defaultDeadInput();
}
