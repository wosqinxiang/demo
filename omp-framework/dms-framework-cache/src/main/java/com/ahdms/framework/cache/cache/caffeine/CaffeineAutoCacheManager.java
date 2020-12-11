package com.ahdms.framework.cache.cache.caffeine;

import com.ahdms.framework.core.commom.util.NumberUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine cache 扩展
 *
 * @author Katrel.zhou
 */
public class CaffeineAutoCacheManager extends org.springframework.cache.caffeine.CaffeineCacheManager {

    public CaffeineAutoCacheManager() {
        super();
    }

    @Override
    protected Cache createCaffeineCache(String name) {
        if (StringUtils.isBlank(name) || !name.contains(StringPool.HASH)) {
            return super.createCaffeineCache(name);
        }
        String[] cacheArray = name.split(StringPool.HASH);
        if (cacheArray.length < 2) {
            return super.createCaffeineCache(name);
        }
        String cacheName = cacheArray[0];
        long cacheAge = NumberUtils.toLong(cacheArray[1], -1);
        com.github.benmanes.caffeine.cache.Cache<Object, Object> cache = Caffeine.newBuilder()
            .expireAfterWrite(cacheAge, TimeUnit.SECONDS)
            .build();
        return new CaffeineCache(cacheName, cache, isAllowNullValues());
    }

}
