package com.ahdms.framework.stream.client;

import lombok.Data;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 17:09
 */
@Data
public class Message<T> {
    private String requestId;
    private Long userId;
    private String role;
    private String clientIp;
    private String userAgent;
    private String handler;
    private String deadHandler;
    private T body;
}
