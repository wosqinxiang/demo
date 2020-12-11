package com.ahdms.framework.cache.cache.redis;

import com.ahdms.framework.core.commom.util.NumberUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.commom.util.StringUtils;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.Nullable;

import java.time.Duration;
import java.util.Map;

/**
 * redis cache 扩展cache name自动化配置
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
public class RedisAutoCacheManager extends RedisCacheManager {

    public RedisAutoCacheManager(RedisCacheWriter cacheWriter,
                                 RedisCacheConfiguration defaultCacheConfiguration,
                                 Map<String, RedisCacheConfiguration> initialCacheConfigurations,
                                 boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
    }

    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfig) {
        if (StringUtils.isBlank(name) || !name.contains(StringPool.HASH)) {
            return super.createRedisCache(name, cacheConfig);
        }
        String[] cacheArray = name.split(StringPool.HASH);
        if (cacheArray.length < 2) {
            return super.createRedisCache(name, cacheConfig);
        }
        String cacheName = cacheArray[0];
        if (cacheConfig != null) {
            long cacheAge = NumberUtils.toLong(cacheArray[1], -1);
            cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(cacheAge));
        }
        return super.createRedisCache(cacheName, cacheConfig);
    }

}
