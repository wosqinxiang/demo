package com.ahdms.framework.stream.logger;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:05
 */
public interface OutputLogger {

    <T> void apply(T payload);
}
