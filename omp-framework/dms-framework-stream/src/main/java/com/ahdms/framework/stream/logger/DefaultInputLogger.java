package com.ahdms.framework.stream.logger;

import com.ahdms.framework.core.commom.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:16
 */
@Slf4j
public class DefaultInputLogger implements InputLogger {

    @Override
    public <T> void apply(T payload, boolean isDead) {
        log.info("[{}] - {}", isDead ? "dead":"normal", JsonUtils.writeValueAsString(payload));
    }

}
