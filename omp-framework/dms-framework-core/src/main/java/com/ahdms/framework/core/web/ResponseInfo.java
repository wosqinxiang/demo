package com.ahdms.framework.core.web;

import com.ahdms.framework.core.commom.util.DateUtils;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/22 16:42
 */
@Data
@Builder
public class ResponseInfo<T> {
    private String action = "response";
    private String requestId;
    private Long duration;
    private Integer status;
    private T result;
}
