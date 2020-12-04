package com.ahdms.aop.customizer;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:26
 */
public interface AspectBeforeProcess {

    void customizer(ProceedingJoinPoint point);

    class DefaultAspectBeforeProcess implements AspectBeforeProcess {
        @Override
        public void customizer(ProceedingJoinPoint point) {
        }
    }
}
