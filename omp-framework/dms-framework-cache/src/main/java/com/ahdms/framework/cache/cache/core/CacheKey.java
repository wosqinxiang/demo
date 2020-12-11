package com.ahdms.framework.cache.cache.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Getter
@ToString
@AllArgsConstructor
public class CacheKey {
    /**
     * cache key
     */
    private final String key;
    /**
     * 超时时间 秒
     */
    private final Duration expire;
}
