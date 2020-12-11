package com.ahdms.framework.core.context;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Getter
@Builder
@ToString
public class DmsContext {

    private String requestId;
    private String token;
    private Long userId;
    private String role;
    private String clientIp;
    private String userAgent;
}
