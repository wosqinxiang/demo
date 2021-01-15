package com.ahdms.ctidservice.context.holder;

import com.ahdms.ctidservice.context.CtidContext;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author qinxiang
 * @date 2021-01-04 11:31
 */
public class CtidContextHolder {

    private static final ThreadLocal<CtidContext> contextLocal = new ThreadLocal<>();

    public static CtidContext getContext() {
        return contextLocal.get();
    }

    /**
     * 为什么暴露给外界：让外界能在自启动线程中正确设定callcontext
     */
    public static void setContext(CtidContext ctidContext) {
        if (ctidContext == null) {
            contextLocal.remove();
        } else {
            contextLocal.set(ctidContext);
        }
    }

    public static void removeContext() {
        contextLocal.remove();
    }

    public static <T> T getAttrByFunc(Function<CtidContext, T> supplier) {
        return Optional.ofNullable(getContext())
                .map(supplier)
                .orElse(null);
    }

}
