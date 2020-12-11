package com.ahdms.framework.core.commom.util;

import com.ahdms.framework.core.context.DmsContext;
import com.ahdms.framework.core.context.holder.DmsContextHolders;

import java.util.Optional;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/6 14:11
 */
public class DmsContextUtils {

    public static String getRequestId() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getRequestId)
                .orElse(null);
    }

    public static Long getUserId() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getUserId)
                .orElse(null);
    }

    public static String getRole() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getRole)
                .orElse(null);
    }

    public static String getToken() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getToken)
                .orElse(null);
    }

    public static String getClientIP() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getClientIp)
                .orElse(null);
    }

    public static String getUserAgent() {
        return Optional.ofNullable(DmsContextHolders.getContext())
                .map(DmsContext::getUserAgent)
                .orElse(null);
    }
}
