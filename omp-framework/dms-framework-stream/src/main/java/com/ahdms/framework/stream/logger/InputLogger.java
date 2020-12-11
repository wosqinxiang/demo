package com.ahdms.framework.stream.logger;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:15
 */
public interface InputLogger {

    /**
     * @param payload
     * @param <T>
     */
    <T> void apply(T payload, boolean isDead);

}
