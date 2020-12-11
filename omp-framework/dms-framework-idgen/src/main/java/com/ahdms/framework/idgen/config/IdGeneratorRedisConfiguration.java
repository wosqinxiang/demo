package com.ahdms.framework.idgen.config;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.JedisClientConfigurationBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/***
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
@Configuration
@ConditionalOnClass({JedisConnection.class, RedisOperations.class, Jedis.class})
public class IdGeneratorRedisConfiguration {

    /**
     * Redis connection configuration.
     */
    @Configuration
    @ConditionalOnClass(GenericObjectPool.class)
    @EnableConfigurationProperties(IdGeneratorRedisProperties.class)
    @Conditional(IdGeneratorExclusiveRedisCondition.class)
    public static class IdGeneratorRedisConnectionConfiguration {

        private final IdGeneratorRedisProperties properties;

        private final RedisSentinelConfiguration sentinelConfiguration;

        private final RedisClusterConfiguration clusterConfiguration;

        private final ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers;

        protected IdGeneratorRedisConnectionConfiguration(IdGeneratorRedisProperties properties,
                                                          ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                                          ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                                          ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
            this.properties = properties;
            this.sentinelConfiguration = sentinelConfigurationProvider.getIfAvailable();
            this.clusterConfiguration = clusterConfigurationProvider.getIfAvailable();
            this.builderCustomizers = builderCustomizers;
        }

        @Bean
        @ConditionalOnMissingBean(name = "idGeneratorRedisConnectionFactory")
        public JedisConnectionFactory idGeneratorRedisConnectionFactory()
                throws UnknownHostException {
            return createJedisConnectionFactory();
        }

        @Bean
        public RedisTemplate<String, Long> idgenRedisTemplate() throws UnknownHostException {
            RedisTemplate<String, Long> template = new RedisTemplate<>();
            template.setConnectionFactory(idGeneratorRedisConnectionFactory());
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));
            template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
            return template;
        }

        private JedisConnectionFactory createJedisConnectionFactory() {
            JedisClientConfiguration clientConfiguration = getJedisClientConfiguration();
            if (getSentinelConfig() != null) {
                return new JedisConnectionFactory(getSentinelConfig(), clientConfiguration);
            }
            if (getClusterConfiguration() != null) {
                return new JedisConnectionFactory(getClusterConfiguration(),
                        clientConfiguration);
            }
            return new JedisConnectionFactory(getStandaloneConfig(), clientConfiguration);
        }

        private JedisClientConfiguration getJedisClientConfiguration() {
            JedisClientConfiguration.JedisClientConfigurationBuilder builder = applyProperties(
                    JedisClientConfiguration.builder());
            IdGeneratorRedisProperties.Pool pool = this.properties.getJedis().getPool();
            if (pool != null) {
                applyPooling(pool, builder);
            }
            if (StringUtils.hasText(this.properties.getUrl())) {
                customizeConfigurationFromUrl(builder);
            }
            customize(builder);
            return builder.build();
        }

        private void customizeConfigurationFromUrl(
                JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
            ConnectionInfo connectionInfo = parseUrl(this.properties.getUrl());
            if (connectionInfo.isUseSsl()) {
                builder.useSsl();
            }
        }

        private void applyPooling(IdGeneratorRedisProperties.Pool pool,
                                  JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
            builder.usePooling().poolConfig(jedisPoolConfig(pool));
        }

        private void customize(
                JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
            this.builderCustomizers.orderedStream()
                    .forEach((customizer) -> customizer.customize(builder));
        }

        private JedisPoolConfig jedisPoolConfig(IdGeneratorRedisProperties.Pool pool) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(pool.getMaxActive());
            config.setMaxIdle(pool.getMaxIdle());
            config.setMinIdle(pool.getMinIdle());
            if (pool.getTimeBetweenEvictionRuns() != null) {
                config.setTimeBetweenEvictionRunsMillis(
                        pool.getTimeBetweenEvictionRuns().toMillis());
            }
            if (pool.getMaxWait() != null) {
                config.setMaxWaitMillis(pool.getMaxWait().toMillis());
            }
            return config;
        }

        private JedisClientConfiguration.JedisClientConfigurationBuilder applyProperties(
                JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
            if (this.properties.isSsl()) {
                builder.useSsl();
            }
            if (this.properties.getTimeout() != null) {
                Duration timeout = this.properties.getTimeout();
                builder.readTimeout(timeout).connectTimeout(timeout);
            }
            return builder;
        }

        protected final RedisStandaloneConfiguration getStandaloneConfig() {
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
            if (StringUtils.hasText(this.properties.getUrl())) {
                ConnectionInfo connectionInfo = parseUrl(this.properties.getUrl());
                config.setHostName(connectionInfo.getHostName());
                config.setPort(connectionInfo.getPort());
                config.setPassword(RedisPassword.of(connectionInfo.getPassword()));
            } else {
                config.setHostName(this.properties.getHost());
                config.setPort(this.properties.getPort());
                config.setPassword(RedisPassword.of(this.properties.getPassword()));
            }
            config.setDatabase(this.properties.getDatabase());
            return config;
        }

        protected final RedisSentinelConfiguration getSentinelConfig() {
            if (this.sentinelConfiguration != null) {
                return this.sentinelConfiguration;
            }
            IdGeneratorRedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(createSentinels(sentinelProperties));
                if (this.properties.getPassword() != null) {
                    config.setPassword(RedisPassword.of(this.properties.getPassword()));
                }
                config.setDatabase(this.properties.getDatabase());
                return config;
            }
            return null;
        }

        /**
         * Create a {@link RedisClusterConfiguration} if necessary.
         *
         * @return {@literal null} if no cluster settings are set.
         */
        protected final RedisClusterConfiguration getClusterConfiguration() {
            if (this.clusterConfiguration != null) {
                return this.clusterConfiguration;
            }
            if (this.properties.getCluster() == null) {
                return null;
            }
            IdGeneratorRedisProperties.Cluster clusterProperties = this.properties.getCluster();
            RedisClusterConfiguration config = new RedisClusterConfiguration(
                    clusterProperties.getNodes());
            if (clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            if (this.properties.getPassword() != null) {
                config.setPassword(RedisPassword.of(this.properties.getPassword()));
            }
            return config;
        }

        private List<RedisNode> createSentinels(IdGeneratorRedisProperties.Sentinel sentinel) {
            List<RedisNode> nodes = new ArrayList<>();
            for (String node : sentinel.getNodes()) {
                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
                } catch (RuntimeException ex) {
                    throw new IllegalStateException(
                            "Invalid redis sentinel " + "property '" + node + "'", ex);
                }
            }
            return nodes;
        }

        protected ConnectionInfo parseUrl(String url) {
            try {
                URI uri = new URI(url);
                boolean useSsl = (url.startsWith("rediss://"));
                String password = null;
                if (uri.getUserInfo() != null) {
                    password = uri.getUserInfo();
                    int index = password.indexOf(':');
                    if (index >= 0) {
                        password = password.substring(index + 1);
                    }
                }
                return new ConnectionInfo(uri, useSsl, password);
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException("Malformed url '" + url + "'", ex);
            }
        }

        protected static class ConnectionInfo {

            private final URI uri;

            private final boolean useSsl;

            private final String password;

            public ConnectionInfo(URI uri, boolean useSsl, String password) {
                this.uri = uri;
                this.useSsl = useSsl;
                this.password = password;
            }

            public boolean isUseSsl() {
                return this.useSsl;
            }

            public String getHostName() {
                return this.uri.getHost();
            }

            public int getPort() {
                return this.uri.getPort();
            }

            public String getPassword() {
                return this.password;
            }

        }

    }

}
