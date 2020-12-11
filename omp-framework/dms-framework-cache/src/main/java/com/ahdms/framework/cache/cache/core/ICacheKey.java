package com.ahdms.framework.cache.cache.core;

import com.ahdms.framework.core.commom.util.CharPool;
import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.commom.util.StringUtils;

import java.time.Duration;

/**
 * 缓存key
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
public interface ICacheKey {
    /**
     * 获取前缀
     *
     * @return key 前缀
     */
    String getPrefix();

    /**
     * 超时时间
     *
     * @return 超时时间
     */
    default Duration getExpire() {
        return Duration.ZERO;
    }


    /**
     * 组装 key
     *
     * @param suffix 参数
     * @return cache key
     */
    default String getKey(Object... suffix) {
        String prefix = this.getPrefix();
        // 拼接参数
        if (ObjectUtils.isEmpty(suffix)) {
            return prefix;
        }
        return prefix + CharPool.COLON + StringUtils.join(suffix, StringPool.COLON);
    }

    /**
     * 组装 cache key
     *
     * @param suffix 参数
     * @return cache key
     */
    default CacheKey getFullKey(Object... suffix) {
        String prefix = this.getPrefix();
        // 拼接参数
        String key;
        if (ObjectUtils.isNotEmpty(suffix)) {
            key = prefix + CharPool.COLON + StringUtils.join(suffix, StringPool.COLON);
        } else {
            key = prefix;
        }
        Duration expire = this.getExpire();
        return new CacheKey(key, expire);
    }
}
