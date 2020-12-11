package com.ahdms.framework.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/27 15:24
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {
    private Admin admin;

    private String accessToken;

    private Executor executor;

    @Getter
    @Setter
    public static class Executor {
        private String appName;
        private String address;
        private String ip;
        private int port;
        private String logPath;
        private int logRetentionDays;
    }

    @Getter
    @Setter
    public static class Admin {
        private String addresses;
    }
}
