package com.ahdms.framework.core.web;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/22 16:39
 */
@Builder
@Getter
@Setter
public class RequestInfo<T> {
    private String action = "request";
    private String requestId;
    private String clientIp;
    private String method;
    private String target;
    private String uri;
    private String requestTime;
    private T body;
}
