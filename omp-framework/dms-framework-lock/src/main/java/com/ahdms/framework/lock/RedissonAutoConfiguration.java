package com.ahdms.framework.lock;

import com.ahdms.framework.core.commom.util.NumberUtils;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.lock.constant.LockerConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.classreading.MethodMetadataReadingVisitor;

/**
 * redisson configuration
 *
 * @Author: Katrel.Zhou
 * @Date: 2019/5/16 10:34
 * @Version 1.0.0
 */
@Configuration
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;

    @Bean
    @ConditionalOnMissingBean(Config.class)
    @Conditional(RedissonServerModeCondition.class)
    public Config singleConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(LockerConstant.REDISSON_SINGLE_PREFIX + redissonProperties.getAddress());
        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            config.useSingleServer().setPassword(redissonProperties.getPassword());
        }
        config.useSingleServer().setDatabase(NumberUtils.getDefaultIfNull(redissonProperties.getDatabase(), LockerConstant.DEFAULT_DATABASE));
        config.useSingleServer().setConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useSingleServer().setConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useSingleServer().setIdleConnectionTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_IDLE_TIMEOUT));
        config.useSingleServer().setConnectTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_CONNECTION_TIMEOUT));
        config.useSingleServer().setTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getTimeout(), LockerConstant.DEFAULT_TIMEOUT));
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    @Conditional(RedissonServerModeCondition.class)
    public Config masterSlaveConfig() {
        Config config = new Config();
        config.useMasterSlaveServers().setMasterAddress(redissonProperties.getMasterAddress());

        config.useMasterSlaveServers().addSlaveAddress(redissonProperties.getSlaveAddress());
        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            config.useMasterSlaveServers().setPassword(redissonProperties.getPassword());
        }
        config.useMasterSlaveServers().setDatabase(NumberUtils.getDefaultIfNull(redissonProperties.getDatabase(), LockerConstant.DEFAULT_DATABASE));
        config.useMasterSlaveServers().setMasterConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useMasterSlaveServers().setMasterConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useMasterSlaveServers().setSlaveConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useMasterSlaveServers().setSlaveConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useMasterSlaveServers().setIdleConnectionTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_IDLE_TIMEOUT));
        config.useMasterSlaveServers().setConnectTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_CONNECTION_TIMEOUT));
        config.useMasterSlaveServers().setTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getTimeout(), LockerConstant.DEFAULT_TIMEOUT));
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    @Conditional(RedissonServerModeCondition.class)
    public Config sentinelConfig() {
        Config config = new Config();
        config.useSentinelServers().setMasterName(redissonProperties.getMasterName());
        config.useSentinelServers().addSentinelAddress(redissonProperties.getSentinelAddress());
        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            config.useSentinelServers().setPassword(redissonProperties.getPassword());
        }
        config.useSentinelServers().setDatabase(NumberUtils.getDefaultIfNull(redissonProperties.getDatabase(), LockerConstant.DEFAULT_DATABASE));
        config.useSentinelServers().setMasterConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useSentinelServers().setMasterConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useSentinelServers().setSlaveConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useSentinelServers().setSlaveConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useSentinelServers().setIdleConnectionTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_IDLE_TIMEOUT));
        config.useSentinelServers().setConnectTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_CONNECTION_TIMEOUT));
        config.useSentinelServers().setTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getTimeout(), LockerConstant.DEFAULT_TIMEOUT));
        return config;
    }

    @Bean
    @ConditionalOnMissingBean(Config.class)
    @Conditional(RedissonServerModeCondition.class)
    public Config clusterConfig() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress(redissonProperties.getNodeAddress());
        if (StringUtils.isNotBlank(redissonProperties.getPassword())) {
            config.useClusterServers().setPassword(redissonProperties.getPassword());
        }
        config.useClusterServers().setMasterConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useClusterServers().setMasterConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useClusterServers().setSlaveConnectionPoolSize(NumberUtils.getDefaultIfNull(redissonProperties.getPoolSize(), LockerConstant.DEFAULT_POOL_SIZE));
        config.useClusterServers().setSlaveConnectionMinimumIdleSize(NumberUtils.getDefaultIfNull(redissonProperties.getIdleSize(), LockerConstant.DEFAULT_IDLE_SIZE));
        config.useClusterServers().setIdleConnectionTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_IDLE_TIMEOUT));
        config.useClusterServers().setConnectTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getConnectionTimeout(), LockerConstant.DEFAULT_CONNECTION_TIMEOUT));
        config.useClusterServers().setTimeout(NumberUtils.getDefaultIfNull(redissonProperties.getTimeout(), LockerConstant.DEFAULT_TIMEOUT));
        return config;
    }

    @Bean
    @ConditionalOnBean(Config.class)
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(Config config) {
        return Redisson.create(config);
    }

    public static class RedissonServerModeCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            String mode = context.getEnvironment().getProperty("dms.redisson.mode");
            if (StringUtils.isBlank(mode)) {
                throw new IllegalArgumentException("dms.redisson.mode can't empty.");
            }
            String methodName = ((MethodMetadataReadingVisitor) metadata).getMethodName().toLowerCase();
            return methodName.startsWith(mode.toLowerCase());
        }

    }
}
