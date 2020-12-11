package com.ahdms.framework.stream.client;

import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.commom.util.DmsContextUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.core.stream.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 15:52
 */
public class StreamClientFactoryBean implements FactoryBean<Object>, InitializingBean, MethodInterceptor {
    public static final String HEADER_NESTED_CLASS = "nestedClass";
    /**
     * value defined in @StreamClient
     */
    private String defaultBinding;

    private Class<?> type;

    private Object proxy;

    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;
    @Autowired
    private List<StreamInterceptor> interceptors;
    @Autowired
    private BindingMetadataHolder bindingMetadataHolder;

    private Map<Method, SourceMetadata> metadataCache = new HashMap<>();

    private Map<Method, MessageChannel> channelCache = new ConcurrentHashMap<>();

    @Override
    public synchronized Object getObject() throws Exception {
        if (this.proxy != null) {
            return this.proxy;
        }
        ProxyFactory factory = new ProxyFactory(this.type, this);
        this.proxy = factory.getProxy();
        return this.proxy;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Class<?> getObjectType() {
        return this.type;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ReflectionUtils.doWithMethods(type, method -> {
            Source source = AnnotationUtils.findAnnotation(method, Source.class);
            if (source != null) {
                if (StringUtils.isBlank(source.output()) && StringUtils.isBlank(this.defaultBinding)) {
                    throw new IllegalArgumentException(StringUtils.format("@StreamClient and @Source binding name are all blank in method {}.{}",
                            type.getSimpleName(), method.getName()));
                }
                if (StringUtils.isBlank(source.handler())) {

                    throw new IllegalArgumentException(StringUtils.format("@Source handler is blank in method {}.{}", type.getSimpleName(), method.getName()));
                }
                if (method.getParameterCount() != 1) {
                    throw new IllegalArgumentException("There must be one and only one argument on method \"" + method + "\" which annotation by @Source. ");
                }
                String binding = StringUtils.getDefaultIfBlank(source.output(), this.defaultBinding);
                this.metadataCache.put(method, new SourceMetadata(source.handler(), source.deadHandler(), binding));
                this.bindingMetadataHolder.addBindingMetadata(source.prefix(), source.group(), binding);
            }
        });
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();

        if (!channelCache.containsKey(method)) {
            SourceMetadata sourceMetadata = this.metadataCache.get(method);
            FrameworkException.throwOnFalse(ObjectUtils.isNotNull(sourceMetadata),
                    "@Source could not specified on method " + method);
            MessageChannel messageChannel = this.binderAwareChannelResolver.resolveDestination(sourceMetadata.binding);
            FrameworkException.throwOnFalse(ObjectUtils.isNotNull(messageChannel),
                    "Illegal binding '{}' on method " + method);
            this.channelCache.put(method, messageChannel);
        }

        Object[] arguments = invocation.getArguments();
        if (null == arguments || arguments.length != 1) {
            FrameworkException.throwFail(
                    StringUtils.format("There must be one and only one argument on method '{}' which annotation by @Source.", method));
        }

        if (null == arguments[0]) {
            FrameworkException.throwFail("Argument must not be null in method " + method);
        }

        Message message = resolveMessage(this.metadataCache.get(method), arguments[0]);
        this.doInterceptors(message, method);
        this.sendMessage(message, method);
        return message;
    }

    private void sendMessage(Message message, Method method) {
        this.channelCache.get(method).send(
                MessageBuilder
                        .withPayload(message)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .setHeader(HEADER_NESTED_CLASS, message.getBody().getClass())
                        .build());
    }

    private void doInterceptors(Message message, Method method) {
        Optional.ofNullable(this.interceptors).ifPresent(s -> {
            s.stream().forEach(i -> i.apply(message, method, this.metadataCache.get(method)));
        });
    }

    /**
     * 组装消息数据
     *
     * @param <T>
     * @param sourceMetadata
     * @param data
     * @return
     */
    private <T, D extends Message<T>> D resolveMessage(SourceMetadata sourceMetadata, T data) {
        Message<T> message = new Message<>();
        message.setUserId(DmsContextUtils.getUserId());
        message.setRole(DmsContextUtils.getRole());
        message.setRequestId(DmsContextUtils.getRequestId());
        message.setUserAgent(DmsContextUtils.getUserAgent());
        message.setClientIp(DmsContextUtils.getClientIP());
        message.setHandler(sourceMetadata.getHandler());
        message.setDeadHandler(sourceMetadata.getDeadHandler());
        message.setBody(data);
        return (D) message;
    }

    public String getDefaultBinding() {
        return defaultBinding;
    }

    public void setDefaultBinding(String defaultBinding) {
        this.defaultBinding = defaultBinding;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public void setProxy(Object proxy) {
        this.proxy = proxy;
    }

    public BinderAwareChannelResolver getBinderAwareChannelResolver() {
        return binderAwareChannelResolver;
    }

    public void setBinderAwareChannelResolver(BinderAwareChannelResolver binderAwareChannelResolver) {
        this.binderAwareChannelResolver = binderAwareChannelResolver;
    }

    @AllArgsConstructor
    @Getter
    public static class SourceMetadata {
        /**
         * 消费者处理类
         */
        private String handler;
        /**
         * DLQ消费者处理类
         */
        private String deadHandler;
        /**
         * binding name
         */
        private String binding;

    }
}
