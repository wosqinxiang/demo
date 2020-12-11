package com.ahdms.framework.session.match;

import com.ahdms.framework.core.commom.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 */
public class HeaderHttpSessionMatcher implements HttpSessionMatcher {

    private final String headerName;

    public HeaderHttpSessionMatcher(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return StringUtils.isNotBlank(request.getHeader(this.headerName));
    }
}
