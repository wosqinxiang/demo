package com.ahdms.framework.feign.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp Slf4j logger
 *
 * @author Katrel.zhou
 */
@Slf4j
public class OkHttpSlf4jLogger implements HttpLoggingInterceptor.Logger {

    @Override
    public void log(String message) {
        log.info(message);
    }
}
