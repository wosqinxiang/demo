package com.ahdms.framework.session.match;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 */
public interface HttpSessionMatcher {

    /**
     * token解析匹配
     */
    boolean match(HttpServletRequest request);
}
