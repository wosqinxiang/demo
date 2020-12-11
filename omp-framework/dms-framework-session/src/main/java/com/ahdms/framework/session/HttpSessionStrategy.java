package com.ahdms.framework.session;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 */
public enum HttpSessionStrategy {
    /**
     * 仅获取Cookie
     */
    ONLY_COOKIE,
    /**
     * 仅获取Header
     */
    ONLY_HEADER,
    /**
     * 先获取Header，Header没获取到再获取Cookie
     */
    HEADER_OR_COOKIE,
    /**
     * 先获取Cookie，Cookie没获取到再获取Header
     */
    COOKIE_OR_HEADER
}
