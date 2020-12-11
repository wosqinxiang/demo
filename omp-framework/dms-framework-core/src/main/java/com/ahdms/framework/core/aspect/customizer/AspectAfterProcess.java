package com.ahdms.framework.core.aspect.customizer;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Date;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:28
 */
public interface AspectAfterProcess {

    /**
     * 自定义后处理
     *
     * @param point
     * @param responseObject
     * @param startTime 开始时间
     * @param <T>
     */
    <T> void customizer(ProceedingJoinPoint point, T responseObject, Date startTime);


    class DefaultAspectAfterProcess implements AspectAfterProcess {

        @Override
        public <T> void customizer(ProceedingJoinPoint point, T responseObject, Date startTime) {
        }
    }
}
