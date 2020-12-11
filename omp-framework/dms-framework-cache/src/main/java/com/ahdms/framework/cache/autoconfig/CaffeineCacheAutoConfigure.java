package com.ahdms.framework.cache.autoconfig;


import com.ahdms.framework.cache.cache.caffeine.CaffeineAutoCacheManager;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 本地化缓存自动配置
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Configuration
public class CaffeineCacheAutoConfigure {

    @Primary
    @Bean("caffeineCacheManager")
    public CaffeineCacheManager cacheManager(CacheProperties cacheProperties,
                                             ObjectProvider<Caffeine<Object, Object>> caffeine,
                                             ObjectProvider<CaffeineSpec> caffeineSpec,
                                             ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager cacheManager = createCacheManager(cacheProperties, caffeine,
            caffeineSpec, cacheLoader);
        List<String> cacheNames = cacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        }
        return cacheManager;
    }

    private CaffeineCacheManager createCacheManager(CacheProperties cacheProperties,
                                                    ObjectProvider<Caffeine<Object, Object>> caffeine,
                                                    ObjectProvider<CaffeineSpec> caffeineSpec,
                                                    ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        CaffeineCacheManager cacheManager = new CaffeineAutoCacheManager();
        setCacheBuilder(cacheProperties, caffeineSpec.getIfAvailable(), caffeine.getIfAvailable(), cacheManager);
        cacheLoader.ifAvailable(cacheManager::setCacheLoader);
        return cacheManager;
    }

    private void setCacheBuilder(CacheProperties cacheProperties,
                                 CaffeineSpec caffeineSpec, Caffeine<Object, Object> caffeine,
                                 CaffeineCacheManager cacheManager) {
        String specification = cacheProperties.getCaffeine().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (caffeineSpec != null) {
            cacheManager.setCaffeineSpec(caffeineSpec);
        } else if (caffeine != null) {
            cacheManager.setCaffeine(caffeine);
        }
    }

}
