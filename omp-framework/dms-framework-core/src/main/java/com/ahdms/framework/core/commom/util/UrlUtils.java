package com.ahdms.framework.core.commom.util;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class UrlUtils extends org.springframework.web.util.UriUtils {

    /**
     * url编码，默认为 utf-8
     *
     * @param source 字符串
     * @return 编码之后的url
     */
    public static String encode(String source) {
        return encode(source, Charsets.UTF_8);
    }

    /**
     * url解码，默认为 utf-8
     *
     * @param source 字符串
     * @return 解码之后的url
     */
    public static String decode(String source) {
        return StringUtils.uriDecode(source, Charsets.UTF_8);
    }
}
