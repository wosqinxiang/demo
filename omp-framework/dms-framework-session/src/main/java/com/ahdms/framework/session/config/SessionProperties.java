package com.ahdms.framework.session.config;

import com.ahdms.framework.session.HttpSessionStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * session使用策略扩展
 *
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 * @see org.springframework.boot.autoconfigure.session.SessionProperties
 */
@ConfigurationProperties(prefix = "dms.session")
public class SessionProperties {

    private HttpSessionStrategy strategy;

    private String headerName;

    private String cookieName;

    public HttpSessionStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(HttpSessionStrategy strategy) {
        this.strategy = strategy;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }
}
