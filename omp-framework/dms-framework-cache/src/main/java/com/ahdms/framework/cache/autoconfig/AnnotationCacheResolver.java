package com.ahdms.framework.cache.autoconfig;

import com.ahdms.framework.cache.annotation.RedisMergedAnnotation;
import com.ahdms.framework.core.commom.util.AnnotationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 注解的缓存处理器
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@RequiredArgsConstructor
public class AnnotationCacheResolver implements CacheResolver {
    private final CaffeineCacheManager caffeineCacheManager;
    private final RedisCacheManager redisCacheManager;

    @Override
    public Collection<? extends Cache> resolveCaches(@NonNull CacheOperationInvocationContext<?> context) {
        Method method = context.getMethod();
        Set<String> cacheNames = context.getOperation().getCacheNames();
        // 判断是否组合的 RedisMergedAnnotation 注解
        RedisMergedAnnotation redisCache = AnnotationUtils.findAnnotation(method, RedisMergedAnnotation.class);
        if (redisCache != null) {
            return cacheNames.stream()
                .map(redisCacheManager::getCache)
                .collect(Collectors.toSet());
        }
        return cacheNames.stream()
            .map(caffeineCacheManager::getCache)
            .collect(Collectors.toSet());
    }
}
