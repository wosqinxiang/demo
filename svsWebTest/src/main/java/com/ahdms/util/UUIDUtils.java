package com.ahdms.util;

import java.util.UUID;

/**
 * @author qinxiang
 * @date 2021-01-03 10:25
 */
public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
