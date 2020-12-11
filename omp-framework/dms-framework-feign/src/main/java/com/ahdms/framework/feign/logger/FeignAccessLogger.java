package com.ahdms.framework.feign.logger;

import feign.RequestTemplate;

import java.util.Date;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/22 16:47
 */
public interface FeignAccessLogger {

    void request(RequestTemplate template);

    <R> void response(R response, Date start);

    void response(Exception exception);
}
