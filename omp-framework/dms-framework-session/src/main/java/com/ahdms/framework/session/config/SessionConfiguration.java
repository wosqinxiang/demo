package com.ahdms.framework.session.config;

import com.ahdms.framework.session.CookieAndHeaderHttpSessionIdResolver;
import com.ahdms.framework.session.HttpSessionStrategy;
import com.ahdms.framework.session.SessionConstant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionIdResolver;

import java.util.Optional;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 */
@Configuration
@EnableConfigurationProperties({SessionProperties.class, RedisSessionProperties.class})
@EnableRedisHttpSession(
        redisNamespace = "spring:session:DMS",
        redisFlushMode = RedisFlushMode.ON_SAVE,
        maxInactiveIntervalInSeconds = 3600)
@AutoConfigureAfter(SessionRedisConfiguration.class)
public class SessionConfiguration {

    private final String headerName;

    private final String cookieName;

    private final HttpSessionStrategy httpSessionStrategy;

    private final JedisConnectionFactory redisConnectionFactory;

    public SessionConfiguration(SessionProperties sessionProperties,
                                JedisConnectionFactory redisConnectionFactory) {
        this.headerName = Optional.ofNullable(sessionProperties.getHeaderName())
                .orElse(SessionConstant.HTTP_HEADER_TOKEN);
        this.cookieName = Optional.ofNullable(sessionProperties.getCookieName())
                .orElse(SessionConstant.HTTP_COOKIE_TOKEN);
        this.httpSessionStrategy = Optional.ofNullable(sessionProperties.getStrategy())
                .orElse(HttpSessionStrategy.HEADER_OR_COOKIE);
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpSessionIdResolver httpSessionStrategy() {
        return new CookieAndHeaderHttpSessionIdResolver(this.headerName, this.cookieName, this.httpSessionStrategy);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        //jsr310,localeDate 等java8 解决
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<Object, Object> sessionRedisOperations() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(this.springSessionDefaultRedisSerializer());

        redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
