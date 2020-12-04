package com.ahdms.logger;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:36
 */
public interface ControllerAccessLogger {

    /**
     * 请求日志输出
     *
     * @param point
     */
    void request(ProceedingJoinPoint point);

    /**
     * 响应日志输出
     *
     * @param point
     * @param responseObject
     * @param startTime
     */
    void response(ProceedingJoinPoint point, Object responseObject, Date startTime);
}
