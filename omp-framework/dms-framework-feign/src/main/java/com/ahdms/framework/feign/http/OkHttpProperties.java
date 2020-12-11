package com.ahdms.framework.feign.http;

import lombok.Getter;
import lombok.Setter;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.concurrent.TimeUnit;

/**
 * okhttp 配置
 *
 * @author Katrel.zhou
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("dms.okhttp")
public class OkHttpProperties {
    /**
     * 最大连接数，默认：200
     */
    private int maxConnections = 200;
    /**
     * 连接存活时间，默认：900L
     */
    private long timeToLive = 900L;
    /**
     * 连接池存活时间单位，默认：秒
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    /**
     * 链接超时，默认：2000毫秒
     */
    private int connectionTimeout = 2000;
    /**
     * 是否支持重定向，默认：true
     */
    private boolean followRedirects = true;
    /**
     * 日志级别（NONE, BASIC, HEADERS, BODY;），默认：BASIC
     */
    private HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BASIC;
}
