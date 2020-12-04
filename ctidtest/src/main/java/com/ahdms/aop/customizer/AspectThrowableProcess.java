package com.ahdms.aop.customizer;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:28
 */
public interface AspectThrowableProcess {

    void customizer(ProceedingJoinPoint point, Throwable throwable);


    class DefaultAspectThrowableProcess implements AspectThrowableProcess {

        @Override
        public void customizer(ProceedingJoinPoint point, Throwable throwable) {
        }
    }
}
