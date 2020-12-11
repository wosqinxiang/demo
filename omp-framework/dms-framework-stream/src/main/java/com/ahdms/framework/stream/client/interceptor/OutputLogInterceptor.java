package com.ahdms.framework.stream.client.interceptor;

import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.stream.client.StreamInterceptor;
import com.ahdms.framework.stream.client.StreamClientFactoryBean.SourceMetadata;
import com.ahdms.framework.stream.client.Message;
import com.ahdms.framework.stream.logger.OutputLogger;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 17:07
 */
public class OutputLogInterceptor implements StreamInterceptor {

    private OutputLogger outputLogger;

    public OutputLogInterceptor(OutputLogger outputLogger) {
        this.outputLogger = outputLogger;
    }

    @Override
    public <T extends Message> void apply(T message, Method method, SourceMetadata metadata) {
       outputLogger.apply(StreamLog.builder()
               .className(method.getDeclaringClass().getSimpleName())
               .methodName(method.getName())
               .binding(metadata.getBinding())
               .message(message)
               .build());
    }

    @Data
    @Builder
    static class StreamLog {
        private String className;
        private String methodName;
        private String binding;
        private Message message;
    }
}
