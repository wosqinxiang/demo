package com.ahdms.framework.lock;

import com.ahdms.framework.core.commom.util.NumberUtils;
import com.ahdms.framework.lock.annotation.AnnotationDistributedLocker;
import com.ahdms.framework.lock.annotation.RedissonAnnotationDistributedLocker;
import com.ahdms.framework.lock.constant.LockerConstant;
import com.ahdms.framework.lock.locker.DistributedLockerClient;
import com.ahdms.framework.lock.locker.RedissonDistributedLockerClient;
import com.ahdms.framework.lock.annotation.spel.DistributedLockerExpressionEvaluator;
import com.ahdms.framework.lock.annotation.spel.LockExpressionEvaluator;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Katrel.Zhou
 * @Date: 2019/5/16 10:34
 * @Version 1.0.0
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@ConditionalOnBean(RedissonClient.class)
@EnableConfigurationProperties(LockAutoConfiguration.LockProperties.class)
@AutoConfigureAfter({RedissonAutoConfiguration.class})
public class LockAutoConfiguration {

    @Autowired
    private LockProperties lockProperties;

    @Bean
    @ConditionalOnMissingBean
    public AnnotationDistributedLocker distributedLocker(RedissonClient redissonClient) {
        return new RedissonAnnotationDistributedLocker(redissonClient,
                NumberUtils.getDefaultIfNull(this.lockProperties.getDefaultLeaseTime(), LockerConstant.DEFAULT_LEASE_TIME),
                NumberUtils.getDefaultIfNull(this.lockProperties.getDefaultWaitTime(), LockerConstant.DEFAULT_WAIT_TIME));
    }

    @Bean
    @ConditionalOnMissingBean
    public LockExpressionEvaluator expressionEvaluator() {
        return new DistributedLockerExpressionEvaluator();
    }

    @Bean
    public DistributedLockerClient distributedLockerClient(RedissonClient redissonClient) {
        return new RedissonDistributedLockerClient(redissonClient,
                NumberUtils.getDefaultIfNull(this.lockProperties.getDefaultLeaseTime(), LockerConstant.DEFAULT_LEASE_TIME),
                NumberUtils.getDefaultIfNull(this.lockProperties.getDefaultWaitTime(), LockerConstant.DEFAULT_WAIT_TIME));
    }

    @Configuration
    @ConditionalOnClass({AnnotationDistributedLocker.class, LockExpressionEvaluator.class})
    @Import(DistributedLockerAspect.class)
    public static class LockAspectConfiguration {

    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "dms.lock")
    public static class LockProperties {
        private Integer defaultLeaseTime;
        private Integer defaultWaitTime;
    }
}
