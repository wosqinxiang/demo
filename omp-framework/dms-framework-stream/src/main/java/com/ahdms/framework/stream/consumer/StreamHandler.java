package com.ahdms.framework.stream.consumer;


import com.ahdms.framework.stream.client.Message;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 15:36
 */
public interface StreamHandler<T, D extends Message<T>> {

    /**
     * 接收消息
     *
     * @param message
     */
    void onMessageReceive(D message);
}
