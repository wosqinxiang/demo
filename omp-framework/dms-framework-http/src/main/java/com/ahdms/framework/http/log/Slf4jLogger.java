package com.ahdms.framework.http.log;

import lombok.extern.slf4j.Slf4j;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttp Slf4j logger
 *
 * @author Katrel.zhou
 */
@Slf4j
public class Slf4jLogger implements HttpLoggingInterceptor.Logger {

    public static final HttpLoggingInterceptor.Logger INSTANCE = new Slf4jLogger();

    @Override
    public void log(String message) {
        log.info(message);
    }
}
