package com.ahdms.framework.feign.interceptor;

import feign.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author yuzhou
 * @date 2018/2/28
 * @time 17:12
 * @since 
 */
public class FeignClientPointcut extends StaticMethodMatcherPointcut {
    private static final Logger logger = LoggerFactory.getLogger(FeignClientPointcut.class);

    //private static final Map<ClientKey, Boolean> matchCaches = new ConcurrentHashMap<>();

    @Override
    public boolean matches(Method method, Class<?> targetClass) {

        boolean matches =  matchesFeignClient(targetClass);

        if (matches) {
            matches = matchesClientMethod(method);
        }

        if (matches && logger.isInfoEnabled()) {
            logger.debug("Intercepted feign client {}, class name is {}.", targetClass.getInterfaces()[0], targetClass);
        }

        return matches;

    }

    private boolean matchesFeignClient(Class<?> targetClass) {
        Class<?>[] interfaces = targetClass.getInterfaces();
        if(interfaces == null | interfaces.length != 1) {
            return false;
        }
        boolean b = interfaces[0].isAnnotationPresent(FeignClient.class);
        return b;
    }

    private boolean matchesClientMethod(Method method) {
        if (method.getDeclaringClass() == Object.class ||
                (method.getModifiers() & Modifier.STATIC) != 0 ||
                Util.isDefault(method)) {
            return false;
        }
        return true;
    }

    static class ClientKey {
        private Class<?> clazz;
        private Method method;

        public ClientKey(Class<?> clazz, Method method) {
            this.clazz = clazz;
            this.method = method;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClientKey clientKey = (ClientKey) o;

            if (clazz != null ? !clazz.equals(clientKey.clazz) : clientKey.clazz != null) return false;
            return method != null ? method.equals(clientKey.method) : clientKey.method == null;
        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (method != null ? method.hashCode() : 0);
            return result;
        }
    }
}
