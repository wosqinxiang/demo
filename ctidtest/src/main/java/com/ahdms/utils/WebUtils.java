package com.ahdms.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

    /**
     * 判断是否 ResponseBody
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     */
    public static boolean isResponseBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = WebUtils.getRequest();
        if (request == null) {
            return null;
        }
        return getCookieVal(request, name);
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     */
    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        boolean isSecure = Optional.ofNullable(WebUtils.getRequest())
                .map(HttpServletRequest::isSecure)
                .orElse(false);
        cookie.setSecure(isSecure);
        response.addCookie(cookie);
    }

    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(x -> (ServletRequestAttributes) x)
                .map(ServletRequestAttributes::getRequest)
                .orElse(null);
    }

    /**
     * 获取 HttpServletResponse
     *
     * @return {HttpServletResponse}
     */
    @Nullable
    public static HttpServletResponse getResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .map(x -> (ServletRequestAttributes) x)
                .map(ServletRequestAttributes::getResponse)
                .orElse(null);
    }

    /**
     * 获取ip
     *
     * @return {String}
     */
    @Nullable
    public static String getIP() {
        return Optional.ofNullable(WebUtils.getRequest())
                .map(WebUtils::getIP)
                .orElse(null);
    }

    private static final String[] IP_HEADER_NAMES = new String[]{
            "x-client-ip",
            "x-forwarded-for",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private static final Predicate<String> IP_PREDICATE = (ip) -> StringUtils.isBlank(ip) || StringPool.UNKNOWN.equalsIgnoreCase(ip);

    /**
     * 获取ip
     *
     * @param request HttpServletRequest
     * @return {String}
     */
    @Nullable
    public static String getIP(@Nullable HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = null;
        for (String ipHeader : IP_HEADER_NAMES) {
            ip = request.getHeader(ipHeader);
            if (!IP_PREDICATE.test(ip)) {
                break;
            }
        }
        if (IP_PREDICATE.test(ip)) {
            ip = request.getRemoteAddr();
        }
        return StringUtils.isBlank(ip) ? null : StringUtils.split(ip, ",")[0];
    }

    /**
     * 返回json
     *
     * @param response HttpServletResponse
     * @param result   结果对象
     */
    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 返回json
     *
     * @param response    HttpServletResponse
     * @param result      结果对象
     * @param contentType contentType
     */
    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(JsonUtils.toString(result));
        } catch (IOException e) {
            throw ExceptionsUtils.unchecked(e);
        }
    }


    @Nullable
    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        // 先找方法，再找方法上的类
        A annotation = handlerMethod.getMethodAnnotation(annotationType);
        if (null != annotation) {
            return annotation;
        }
        // 获取类上面的Annotation，可能包含组合注解，故采用spring的工具类
        Class<?> beanType = handlerMethod.getBeanType();
        return AnnotatedElementUtils.findMergedAnnotation(beanType, annotationType);
    }


    /**
     * 获取{@linkplain org.springframework.web.bind.annotation.RequestMapping}对应的path
     */
    public static String getBestMatchingPatternAttribute(HttpServletRequest request) {
        return (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    }
}

