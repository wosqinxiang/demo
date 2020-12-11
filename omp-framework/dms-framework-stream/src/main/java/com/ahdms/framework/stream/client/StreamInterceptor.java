package com.ahdms.framework.stream.client;

import com.ahdms.framework.stream.client.StreamClientFactoryBean.SourceMetadata;

import java.lang.reflect.Method;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 16:47
 */
public interface StreamInterceptor {
    <T extends Message> void apply(T message, Method method, SourceMetadata metadata);
}
