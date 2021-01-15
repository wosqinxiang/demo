package com.ahdms.ctidservice.util;

import java.util.stream.Stream;

/**
 * @author qinxiang
 * @date 2020-11-30 16:02
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static boolean isAnyBlank(String... str){
        return str == null ? true : Stream.of(str).anyMatch(StringUtils::isBlank);
    }

    public static String join(String separator,String ...s1){
        return join(s1,separator);
    }



}
