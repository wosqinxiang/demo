package com.ahdms.framework.feign.logger;

import com.ahdms.framework.core.commom.util.*;
import com.ahdms.framework.core.web.RequestInfo;
import com.ahdms.framework.core.web.ResponseInfo;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/22 16:51
 */
@Slf4j
public class DefaultFeignAccessLogger implements FeignAccessLogger {

    @Override
    public void request(RequestTemplate template) {
        RequestInfo requestInfo = RequestInfo.builder()
                .requestId(DmsContextUtils.getRequestId())
                .requestTime(DateUtils.getCurrentDateTime(DateUtils.STANDARD_FORMATTER))
                .clientIp(DmsContextUtils.getClientIP())
                .method(template.method())
                .uri(template.url())
                .body(convertBody(template))
                .build();
        log.info("[{}] - {}", Thread.currentThread().getId(), JsonUtils.writeValueAsString(requestInfo));
    }

    @Override
    public <R> void response(R response, Date start) {
        ResponseInfo<Object> responseInfo = ResponseInfo.builder()
                .requestId(DmsContextUtils.getRequestId())
                .duration(DateUtils.between(start, new Date()).toMillis())
                .result(response)
                .build();
        log.info("[{}] - {}", Thread.currentThread().getId(), JsonUtils.writeValueAsString(responseInfo));
    }

    @Override
    public void response(Exception exception) {
        ResponseInfo<Object> responseInfo = ResponseInfo.builder()
                .requestId(DmsContextUtils.getRequestId())
                .duration(1L)
                .result(exception.getMessage())
                .build();
        log.info("[{}] - {}", Thread.currentThread().getId(), JsonUtils.writeValueAsString(responseInfo));
    }

    private Object convertBody(RequestTemplate template) {
        Map<String, Collection<String>> queries = template.queries();
        String body = template.requestBody().asString();
        if (ObjectUtils.isNotNull(body)) {
            return body;
        }
        if (CollectionUtils.isNotEmpty(queries)) {
            return queries.entrySet().stream().map(entry -> entry.getKey() + StringPool.EQUALS + entry.getValue())
                    .collect(Collectors.joining(StringPool.AMPERSAND));
        }
        return StringPool.EMPTY;
    }

}
