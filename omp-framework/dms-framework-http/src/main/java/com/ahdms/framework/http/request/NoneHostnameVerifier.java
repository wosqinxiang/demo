package com.ahdms.framework.http.request;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/29 16:04
 */
public class NoneHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
