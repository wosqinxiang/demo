package com.ahdms.framework.feign.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author yuzhou
 * @date 2018/2/28
 * @time 17:37
 * @since 
 */
public class BeanFactoryFeignClientPointcutAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private Pointcut pointcut = new FeignClientPointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

}
