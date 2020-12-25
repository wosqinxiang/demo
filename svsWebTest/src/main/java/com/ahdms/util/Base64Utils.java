package com.ahdms.util;

import com.ahdms.code.ApiCode;
import com.ahdms.exception.Base64Exception;

/**
 * @author qinxiang
 * @date 2020-12-21 13:59
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

    public static byte[] decodeString(String src){
        try {
            return decodeFromString(src);
        }catch (Exception e){
            throw new Base64Exception(ApiCode.CORE_BASE64_DECODE_ERROR.getMessage());
        }
    }

}
