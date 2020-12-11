package com.ahdms.framework.http.request;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/29 15:53
 */
public class IgnoreCertificateSSLSocketFactory extends SSLSocketFactory {

    private SSLContext sslContext = SSLContext.getInstance("SSL");
    private TrustManager trustManager = null;

    public SSLContext getSSLContext() {
        return sslContext;
    }

    public X509TrustManager getTrustManager() {
        return (X509TrustManager) trustManager;
    }


    public IgnoreCertificateSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        trustManager = new X509TrustManager() {

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        sslContext.init(null, new TrustManager[]{trustManager}, null);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    @Override
    public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, s, i, b);
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(s, i);
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(s, i, inetAddress, i1);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return sslContext.getSocketFactory().createSocket(inetAddress, i);
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
        return sslContext.getSocketFactory().createSocket(inetAddress, i, inetAddress1, i1);
    }
}
