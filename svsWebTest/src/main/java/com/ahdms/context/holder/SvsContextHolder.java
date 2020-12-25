package com.ahdms.context.holder;

import com.ahdms.context.SvsContext;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author qinxiang
 * @date 2020-12-25 14:35
 */
public class SvsContextHolder {

    private static final ThreadLocal<SvsContext> contextLocal = new ThreadLocal<>();

    public static SvsContext getContext() {
        return contextLocal.get();
    }

    /**
     * 为什么暴露给外界：让外界能在自启动线程中正确设定callcontext
     */
    public static void setContext(SvsContext svsContext) {
        if (svsContext == null) {
            contextLocal.remove();
        } else {
            contextLocal.set(svsContext);
        }
    }

    public static void removeContext() {
        contextLocal.remove();
    }

    public static <T> T getAttrByFunc(Function<SvsContext, T> supplier) {
        return Optional.ofNullable(getContext())
                .map(supplier)
                .orElse(null);
    }

}
