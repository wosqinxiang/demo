package com.ahdms.framework.core.commom.util;

import lombok.experimental.UtilityClass;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class ThreadUtils {

    /**
     * Thread sleep
     *
     * @param millis 时长
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
