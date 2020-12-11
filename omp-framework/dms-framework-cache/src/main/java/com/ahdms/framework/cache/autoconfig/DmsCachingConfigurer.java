package com.ahdms.framework.cache.autoconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
@RequiredArgsConstructor
public class DmsCachingConfigurer extends CachingConfigurerSupport {
    private final CaffeineCacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;

    @Override
    public CacheResolver cacheResolver() {
        return new AnnotationCacheResolver(caffeineCacheManager, redisCacheManager);
    }
}
