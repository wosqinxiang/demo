package com.ahdms.ctidservice.util;

/**
 * @author qinxiang
 * @date 2021-01-11 9:16
 */
public class Base64Utils {

    public static byte[] decodeFromString(String src){
        return com.ahdms.framework.core.commom.util.Base64Utils.decodeFromString(src);
    }

    public static String encodeToString(byte[] src) {
        return com.ahdms.framework.core.commom.util.Base64Utils.encodeToString(src);
    }

}
