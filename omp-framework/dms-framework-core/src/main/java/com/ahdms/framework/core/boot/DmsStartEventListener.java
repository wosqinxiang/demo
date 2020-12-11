package com.ahdms.framework.core.boot;

import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.logger.env.EnvLogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Slf4j
@Configuration
public class DmsStartEventListener {

    @Async
    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        WebServerApplicationContext applicationContext = event.getApplicationContext();
        Environment environment = applicationContext.getEnvironment();
        String appName = environment.getProperty("spring.application.name");
        int localPort = event.getWebServer().getPort();
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        log.warn("\n[{}] boot success，port:[{}]，profile:[{}]---", appName, localPort, profile);
        if (!StringUtils.equalsIgnoreCase(profile, EnvLogLevel.pro.name())) {
            log.warn(String.format("\nhttp://localhost:%s/doc.html", localPort));
        }
    }
}
