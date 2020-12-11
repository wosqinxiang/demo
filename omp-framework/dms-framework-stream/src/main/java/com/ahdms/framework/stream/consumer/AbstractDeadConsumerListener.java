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
 * @see DefaultDeadConsumerListener
 */
@Slf4j
@AllArgsConstructor
public class AbstractDeadConsumerListener {

    private Map<String, StreamDeadHandler> streamDeadHandlers;
    private InputLogger inputLogger;

    public AbstractDeadConsumerListener(ObjectProvider<Map<String, StreamDeadHandler>> streamDeadHandlerProvider,
                                        InputLogger inputLogger) {
        this.streamDeadHandlers = streamDeadHandlerProvider.getIfAvailable();
        this.inputLogger = inputLogger;
    }

    protected <T, D extends Message<T>> void onDeadMessageListener(D message) {
        inputLogger.apply(message, true);
        String deadHandlerName = message.getDeadHandler();
        if (StringUtils.isBlank(deadHandlerName)) {
            log.error("Consumer dead handler bean name is blank, illegal message : {}", message);
        }

        StreamDeadHandler<T, D> handler = this.getStreamDeadHandler(deadHandlerName);
        if (ObjectUtils.isNull(handler)) {
            log.error("Consumer dead handler {} could not be found, discard message : {},", deadHandlerName, message);
        }

        try {
            handler.onDeadMessageReceive(message);
        } catch (Throwable t) {
//            log.error("One error during handing message, caused by ==>", t);
            throw t;
        }
    }

    protected <T, D extends Message<T>> StreamDeadHandler<T, D> getStreamDeadHandler(String handlerName) {
        return this.streamDeadHandlers.get(handlerName);
    }

    @PostConstruct
    public void afterPropertySet() {
        if (CollectionUtils.isEmpty(this.streamDeadHandlers)) {
            log.warn("Cloud not found any StreamDeadHandler in application context, ignore this warning if no consumer.");
        }
    }
}
