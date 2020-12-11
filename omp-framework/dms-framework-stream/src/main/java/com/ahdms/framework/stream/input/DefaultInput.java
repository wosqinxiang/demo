package com.ahdms.framework.stream.input;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:55
 */
public interface DefaultInput {
    String DEFAULT_INPUT = "input";

    @Input(DefaultInput.DEFAULT_INPUT)
    SubscribableChannel defaultInput();
}
