package com.ahdms.framework.stream.logger;

import com.ahdms.framework.core.commom.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:07
 */
@Slf4j
public class DefaultOutputLogger implements OutputLogger {

    @Override
    public <T> void apply(T payload) {
        log.info(JsonUtils.writeValueAsString(payload));
    }
}
