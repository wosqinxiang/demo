package com.ahdms.framework.http.request;

import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.http.*;
import com.ahdms.framework.http.async.AsyncCall;
import com.ahdms.framework.http.interceptor.RetryInterceptor;
import com.ahdms.framework.http.log.Slf4jLogger;
import com.ahdms.framework.http.response.HttpResponse;
import com.ahdms.framework.http.response.ResponseSpec;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.util.Assert;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * ok http 封装，请求结构体
 *
 * @author Katrel.zhou
 */
@Slf4j
public class HttpRequest {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";
    private static final MediaType APPLICATION_JSON = MediaType.get("application/json;charset=UTF-8");
    private static OkHttpClient httpClient = new OkHttpClient();
    private static HttpLoggingInterceptor globalLoggingInterceptor = null;
    private final Request.Builder requestBuilder;
    private final HttpUrl.Builder uriBuilder;
    private final String httpMethod;
    private String userAgent;
    private RequestBody requestBody;
    private Boolean followRedirects;
    private Boolean followSslRedirects;
    private HttpLoggingInterceptor.Level level;
    private CookieJar cookieJar;
    private final List<Interceptor> interceptors = new ArrayList<>();
    private EventListener eventListener;
    private Authenticator authenticator;
    private Duration connectTimeout = Duration.ofMillis(5000);
    private Duration readTimeout = Duration.ofMillis(3000);
    private Duration writeTimeout = Duration.ofMillis(3000);;
    private Proxy proxy;
    private ProxySelector proxySelector;
    private Authenticator proxyAuthenticator;
    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;

    public static HttpRequest get(final String url) {
        return new HttpRequest(new Request.Builder(), url, Method.GET.name());
    }

    public static HttpRequest get(final URI uri) {
        return get(uri.toString());
    }

    public static HttpRequest post(final String url) {
        return new HttpRequest(new Request.Builder(), url, Method.POST.name());
    }

    public static HttpRequest post(final URI uri) {
        return post(uri.toString());
    }

    public static HttpRequest patch(final String url) {
        return new HttpRequest(new Request.Builder(), url, Method.PATCH.name());
    }

    public static HttpRequest patch(final URI uri) {
        return patch(uri.toString());
    }

    public static HttpRequest put(final String url) {
        return new HttpRequest(new Request.Builder(), url, Method.PUT.name());
    }

    public static HttpRequest put(final URI uri) {
        return put(uri.toString());
    }

    public static HttpRequest delete(final String url) {
        return new HttpRequest(new Request.Builder(), url, Method.DELETE.name());
    }

    public static HttpRequest delete(final URI uri) {
        return delete(uri.toString());
    }

    private static RequestBody emptyBody() {
        return RequestBody.create(null, new byte[0]);
    }

    public HttpRequest query(String query) {
        this.uriBuilder.query(query);
        return this;
    }

    public HttpRequest queryEncoded(String encodedQuery) {
        this.uriBuilder.encodedQuery(encodedQuery);
        return this;
    }

    public HttpRequest query(String name, Object value) {
        this.uriBuilder.addQueryParameter(name, value == null ? null : String.valueOf(value));
        return this;
    }

    public HttpRequest queryMap(Map<String, Object> queryMap) {
        if (queryMap != null && !queryMap.isEmpty()) {
            queryMap.forEach(this::query);
        }
        return this;
    }

    public HttpRequest queryEncoded(String encodedName, Object encodedValue) {
        this.uriBuilder.addEncodedQueryParameter(encodedName, encodedValue == null ? null : String.valueOf(encodedValue));
        return this;
    }

    HttpRequest form(FormBody formBody) {
        this.requestBody = formBody;
        return this;
    }

    HttpRequest multipartForm(MultipartBody multipartBody) {
        this.requestBody = multipartBody;
        return this;
    }

    public FormBuilder formBuilder() {
        return new FormBuilder(this);
    }

    public MultipartFormBuilder multipartFormBuilder() {
        return new MultipartFormBuilder(this);
    }

    public HttpRequest body(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public HttpRequest bodyString(String body) {
        this.requestBody = RequestBody.create(APPLICATION_JSON, body);
        return this;
    }

    public HttpRequest bodyString(MediaType contentType, String body) {
        this.requestBody = RequestBody.create(contentType, body);
        return this;
    }

    public HttpRequest bodyString(String contentType, String body) {
        this.requestBody = RequestBody.create(MediaType.get(contentType), body);
        return this;
    }

    public HttpRequest bodyJson(Object body) {
        return bodyString(JsonUtils.toString(body));
    }

    private HttpRequest(final Request.Builder requestBuilder, String url, String httpMethod) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        Assert.notNull(httpUrl, String.format("Url 不能解析: %s: [%s]。", httpMethod.toLowerCase(), url));
        this.requestBuilder = requestBuilder;
        this.uriBuilder = httpUrl.newBuilder();
        this.httpMethod = httpMethod;
        this.userAgent = DEFAULT_USER_AGENT;
    }

    private Call internalCall(final OkHttpClient client) {
        OkHttpClient.Builder builder = client.newBuilder();
        if (this.connectTimeout != null) {
            builder.connectTimeout(this.connectTimeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        if (this.readTimeout != null) {
            builder.readTimeout(this.readTimeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        if (this.writeTimeout != null) {
            builder.writeTimeout(this.writeTimeout.toMillis(), TimeUnit.MILLISECONDS);
        }
        if (this.proxy != null) {
            builder.proxy(this.proxy);
        }
        if (this.proxySelector != null) {
            builder.proxySelector(this.proxySelector);
        }
        if (this.proxyAuthenticator != null) {
            builder.proxyAuthenticator(proxyAuthenticator);
        }
        if (this.hostnameVerifier != null) {
            builder.hostnameVerifier(hostnameVerifier);
        }
        if (this.sslSocketFactory != null && this.trustManager != null) {
            builder.sslSocketFactory(sslSocketFactory, trustManager);
        }
        if (this.authenticator != null) {
            builder.authenticator(authenticator);
        }
        if (!this.interceptors.isEmpty()) {
            builder.interceptors().addAll(this.interceptors);
        }
        if (this.eventListener != null) {
            builder.eventListener(eventListener);
        }
        if (this.cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        if (this.followRedirects != null) {
            builder.followRedirects(this.followRedirects);
        }
        if (this.followSslRedirects != null) {
            builder.followSslRedirects(this.followSslRedirects);
        }
        // 注意：日志放到最后面，就可以打印重试的日志
        if (this.level != null && HttpLoggingInterceptor.Level.NONE != this.level) {
            builder.addInterceptor(getLoggingInterceptor(level));
        } else if (globalLoggingInterceptor != null) {
            builder.addInterceptor(globalLoggingInterceptor);
        }
        // 设置 User-Agent
        this.requestBuilder.header("User-Agent", userAgent);
        // url
        requestBuilder.url(uriBuilder.build());
        String method = this.httpMethod;
        Request request;
        if (HttpMethod.requiresRequestBody(method) && requestBody == null) {
            request = this.requestBuilder.method(method, emptyBody()).build();
        } else {
            request = this.requestBuilder.method(method, requestBody).build();
        }
        return builder.build().newCall(request);
    }

    public HttpRequest retry() {
        this.interceptors.add(new RetryInterceptor());
        return this;
    }

    public HttpRequest retry(int maxAttempts, long backOffPeriod) {
        this.interceptors.add(new RetryInterceptor(maxAttempts, backOffPeriod));
        return this;
    }

    public HttpRequest retryOn(Predicate<ResponseSpec> predicate) {
        this.interceptors.add(new RetryInterceptor(predicate));
        return this;
    }

    public HttpResponse execute() {
        Call call = internalCall(httpClient);
        try {
            return new HttpResponse(call.execute());
        } catch (IOException e) {
            return new HttpResponse(call.request(), e);
        }
    }

    public AsyncCall async() {
        Call call = internalCall(httpClient);
        return new AsyncCall(call);
    }

    public HttpRequest baseAuth(String userName, String password) {
        this.authenticator = new BaseAuthenticator(userName, password);
        return this;
    }

    //// HTTP header operations
    public HttpRequest addHeader(final Map<String, String> headers) {
        this.requestBuilder.headers(Headers.of(headers));
        return this;
    }

    public HttpRequest addHeader(final String... namesAndValues) {
        Headers headers = Headers.of(namesAndValues);
        this.requestBuilder.headers(headers);
        return this;
    }

    public HttpRequest addHeader(final String name, final String value) {
        this.requestBuilder.addHeader(name, value);
        return this;
    }

    public HttpRequest setHeader(final String name, final String value) {
        this.requestBuilder.header(name, value);
        return this;
    }

    public HttpRequest removeHeader(final String name) {
        this.requestBuilder.removeHeader(name);
        return this;
    }

    public HttpRequest addCookie(final Cookie cookie) {
        this.addHeader("Cookie", cookie.toString());
        return this;
    }

    public HttpRequest cacheControl(final CacheControl cacheControl) {
        this.requestBuilder.cacheControl(cacheControl);
        return this;
    }

    public HttpRequest userAgent(final String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HttpRequest followRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public HttpRequest followSslRedirects(boolean followSslRedirects) {
        this.followSslRedirects = followSslRedirects;
        return this;
    }

    private static HttpLoggingInterceptor getLoggingInterceptor(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Slf4jLogger.INSTANCE);
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    public HttpRequest logBody() {
        this.level = HttpLoggingInterceptor.Level.BODY;
        return this;
    }

    public HttpRequest log(LogLevel logLevel) {
        this.level = logLevel.getLevel();
        return this;
    }

    public HttpRequest authenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public HttpRequest interceptor(Interceptor interceptor) {
        this.interceptors.add(interceptor);
        return this;
    }

    public HttpRequest eventListener(EventListener eventListener) {
        this.eventListener = eventListener;
        return this;
    }

    public HttpRequest cookieManager(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    //// HTTP connection parameter operations
    public HttpRequest connectTimeout(final Duration timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    public HttpRequest readTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpRequest writeTimeout(Duration writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public HttpRequest proxy(final InetSocketAddress address) {
        this.proxy = new Proxy(Proxy.Type.HTTP, address);
        return this;
    }

    public HttpRequest proxySelector(final ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return this;
    }

    public HttpRequest proxyAuthenticator(final Authenticator proxyAuthenticator) {
        this.proxyAuthenticator = proxyAuthenticator;
        return this;
    }

    public HttpRequest hostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public HttpRequest sslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.sslSocketFactory = sslSocketFactory;
        this.trustManager = trustManager;
        return this;
    }

    @Override
    public String toString() {
        return requestBuilder.toString();
    }

    public static void setHttpClient(OkHttpClient httpClient) {
        HttpRequest.httpClient = httpClient;
    }

    public static void setGlobalLog(LogLevel logLevel) {
        HttpRequest.globalLoggingInterceptor = getLoggingInterceptor(logLevel.getLevel());
    }

    static String handleValue(Object value) {
        if (value == null) {
            return StringPool.EMPTY;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }
}
