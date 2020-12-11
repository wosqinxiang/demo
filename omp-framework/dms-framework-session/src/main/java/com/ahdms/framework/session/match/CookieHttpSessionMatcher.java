package com.ahdms.framework.session.match;


import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 */
public class CookieHttpSessionMatcher implements HttpSessionMatcher {

    private final String cookieName;

    public CookieHttpSessionMatcher(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public boolean match(HttpServletRequest request) {
        return !Optional.ofNullable(request.getCookies())
            .map(cookies -> Stream.of(cookies).noneMatch(c -> Objects.equals(c.getName(), this.cookieName)))
            .orElse(true);

    }
}
