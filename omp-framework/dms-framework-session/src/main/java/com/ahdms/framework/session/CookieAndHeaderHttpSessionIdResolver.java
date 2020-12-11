package com.ahdms.framework.session;


import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.session.match.CookieHttpSessionMatcher;
import com.ahdms.framework.session.match.HeaderHttpSessionMatcher;
import com.ahdms.framework.session.match.HttpSessionMatcher;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 * @see org.springframework.session.web.http.SessionRepositoryFilter
 */
public class CookieAndHeaderHttpSessionIdResolver implements HttpSessionIdResolver {

    private HeaderHttpSessionIdResolver headerHttpSessionIdResolver;
    private CookieHttpSessionIdResolver cookieHttpSessionIdResolver;
    private HttpSessionMatcher headerHttpSessionMatcher;
    private HttpSessionMatcher cookieHttpSessionMatcher;
    private HttpSessionStrategy httpSessionStrategy;

    public CookieAndHeaderHttpSessionIdResolver() {
        this(SessionConstant.HTTP_HEADER_TOKEN, SessionConstant.HTTP_COOKIE_TOKEN,
            HttpSessionStrategy.HEADER_OR_COOKIE);
    }

    public CookieAndHeaderHttpSessionIdResolver(String headerName, String cookieName,
        HttpSessionStrategy httpSessionStrategy) {
        this.headerHttpSessionIdResolver = new HeaderHttpSessionIdResolver(headerName);
        this.cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setCookieName(cookieName);
        cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);
        this.headerHttpSessionMatcher = new HeaderHttpSessionMatcher(headerName);
        this.cookieHttpSessionMatcher = new CookieHttpSessionMatcher(cookieName);
        this.httpSessionStrategy = httpSessionStrategy;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        return getHttpSessionResolver(request).resolveSessionIds(request);
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String s) {
        getHttpSessionResolver(request).setSessionId(request, response, s);
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        getHttpSessionResolver(request).expireSession(request, response);
    }

    /**
     * 根据Session策略获取对应的Session解析器
     */
    private HttpSessionIdResolver getHttpSessionResolver(HttpServletRequest request) {
        switch (this.httpSessionStrategy) {
            case ONLY_COOKIE:
                return this.cookieHttpSessionIdResolver;
            case ONLY_HEADER:
                return this.headerHttpSessionIdResolver;
            case HEADER_OR_COOKIE:
                return this.headerHttpSessionMatcher.match(request) ? this.headerHttpSessionIdResolver
                    : this.cookieHttpSessionIdResolver;
            case COOKIE_OR_HEADER:
                return this.cookieHttpSessionMatcher.match(request) ? this.cookieHttpSessionIdResolver
                    : this.headerHttpSessionIdResolver;
            default: {
                throw new FrameworkException("The session strategy is not supported.");
            }
        }
    }
}
