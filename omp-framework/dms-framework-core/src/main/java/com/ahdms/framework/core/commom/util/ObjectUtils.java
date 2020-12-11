package com.ahdms.framework.core.commom.util;

import lombok.experimental.UtilityClass;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@UtilityClass
public class ObjectUtils extends org.springframework.util.ObjectUtils {
    /**
     * 判断对象是否全不为空
     * @param values
     * @return
     */
    public static boolean allNotNull(final Object... values) {
        if (values == null) {
            return false;
        }

        for (final Object val : values) {
            if (val == null) {
                return false;
            }
        }

        return true;
    }
    /**
     * 判断对象为null
     *
     * @param object 数组
     * @return 数组是否为空
     */
    public static boolean isNull(@Nullable Object object) {
        return object == null;
    }

    /**
     * 判断对象不为null
     *
     * @param object 数组
     * @return 数组是否为空
     */
    public static boolean isNotNull(@Nullable Object object) {
        return object != null;
    }

    /**
     * 是否为 true
     *
     * @param bool boolean
     * @return boolean
     */
    public static boolean isTrue(boolean bool) {
        return bool;
    }

    /**
     * 是否为 true
     *
     * @param bool Boolean
     * @return boolean
     */
    public static boolean isTrue(@Nullable Boolean bool) {
        return Optional.ofNullable(bool).orElse(Boolean.FALSE);
    }

    /**
     * 是否为 false
     *
     * @param bool Boolean
     * @return boolean
     */
    public static boolean isFalse(boolean bool) {
        return !ObjectUtils.isTrue(bool);
    }

    /**
     * 是否为 false
     *
     * @param bool Boolean
     * @return boolean
     */
    public static boolean isFalse(@Nullable Boolean bool) {
        return !ObjectUtils.isTrue(bool);
    }

    /**
     * 判断数组不为空
     *
     * @param array 数组
     * @return 数组是否为空
     */
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return !ObjectUtils.isEmpty(array);
    }

    /**
     * 判断对象不为空
     *
     * @param obj 数组
     * @return 数组是否为空
     */
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !ObjectUtils.isEmpty(obj);
    }
}
