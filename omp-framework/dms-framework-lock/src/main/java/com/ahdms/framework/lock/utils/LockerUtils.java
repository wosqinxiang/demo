package com.ahdms.framework.lock.utils;


import com.ahdms.framework.lock.constant.LockerConstant;

/**
 * @author Katrel.Zhou
 * @date 2019/8/6 12:07
 */
public class LockerUtils {

    public static String getLockName(String lockName) {
        if (lockName.startsWith(LockerConstant.LOCKER_PREFIX)) {
            return lockName;
        }
        return LockerConstant.LOCKER_PREFIX + lockName;
    }
}
