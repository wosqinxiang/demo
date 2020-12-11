package com.ahdms.framework.stream.consumer;

import com.ahdms.framework.core.commom.util.CollectionUtils;
import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.stream.client.Message;
import com.ahdms.framework.stream.logger.InputLogger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 父类消息消费监听对象，框架默认监听 DefaultInput.DEFAULT_INPUT通道
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/17 9:43
 * @see DefaultConsumerListener
 */
@Slf4j
@AllArgsConstructor
public class AbstractConsumerListener {

    private Map<String, StreamHandler> streamHandlers;
    private InputLogger inputLogger;

    public AbstractConsumerListener(ObjectProvider<Map<String, StreamHandler>> streamHandlerProvider,
                                    InputLogger inputLogger) {
        this.streamHandlers = streamHandlerProvider.getIfAvailable();
        this.inputLogger = inputLogger;
    }

    protected <T, D extends Message<T>> void onMessageListener(D message) {
        inputLogger.apply(message, false);
        String handlerName = message.getHandler();
        if (StringUtils.isBlank(handlerName)) {
            log.error("Consumer handler bean name is blank, illegal message : {}", message);
        }

        StreamHandler<T, D> handler = this.getStreamHandler(handlerName);
        if (ObjectUtils.isNull(handler)) {
            log.error("Consumer handler {} could not be found, discard message : {},", handlerName, message);
        }

        try {
            handler.onMessageReceive(message);
        } catch (Throwable t) {
//            log.error("One error during handing message, caused by ==>", t);
            throw t;
        }
    }

    protected <T, D extends Message<T>> StreamHandler<T, D> getStreamHandler(String handlerName) {
        return this.streamHandlers.get(handlerName);
    }

    @PostConstruct
    public void afterPropertySet() {
        if (CollectionUtils.isEmpty(this.streamHandlers)) {
            log.warn("Cloud not found any StreamHandler in application context, ignore this warning if no consumer.");
        }
    }
}
