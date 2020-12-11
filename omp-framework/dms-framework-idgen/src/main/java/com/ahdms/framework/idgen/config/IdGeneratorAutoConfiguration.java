package com.ahdms.framework.idgen.config;

import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.id.IdGeneratorManager;
import com.ahdms.framework.idgen.IdGenerator;
import com.ahdms.framework.idgen.MixStrategyGeneratorManager;
import com.ahdms.framework.idgen.constant.RedisMode;
import com.ahdms.framework.idgen.local.LocalIdGenerator;
import com.ahdms.framework.idgen.redis.consecutive.ConsecutiveRedisStringIdGenerator;
import com.ahdms.framework.idgen.redis.piecemeal.PiecemealRedisStringIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

/***
 * {@link EnableAutoConfiguration Auto-configuration} for {@link IdGenerator}.
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
@Configuration
@ConditionalOnClass(IdGenerator.class)
@EnableConfigurationProperties(IdGenProperties.class)
@Import(IdGeneratorRedisConfiguration.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class IdGeneratorAutoConfiguration {

    private IdGenProperties idGenProperties;

    @Autowired
    public IdGeneratorAutoConfiguration(IdGenProperties idGenProperties) {
        this.idGenProperties = idGenProperties;
    }

    @PostConstruct
    public void afterPropertySet() {
        if (ObjectUtils.isNull(idGenProperties.getRedisMode())) {
            idGenProperties.setRedisMode(RedisMode.SINGLE);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public IdGeneratorManager mixStrategyGeneratorManager(IdGenProperties idGenProperties, List<IdGenerator> idGenerators) {
        return new MixStrategyGeneratorManager(idGenProperties, idGenerators);
    }

    @Bean
    public IdGenerator localIdGenerator() {
        return new LocalIdGenerator();
    }

//    @Bean
//    public IdGenerator piecemealHashIdGenerator(RedisTemplate<String, Long> idgenRedisTemplate, IdGenProperties idGenProperties) {
//        try {
//            RedisClusterConnection redisClusterConnection = idgenRedisTemplate.getConnectionFactory().getClusterConnection();
//            if (null != redisClusterConnection) {
//                idGenProperties.setRedisMode(RedisMode.CLUSTER);
//            }
//        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
//        } catch (Exception e) {
//        }
//        return new PiecemealRedisHashIdGenerator(idgenRedisTemplate, idGenProperties);
//    }

    @Bean
    public IdGenerator piecemealIdGenerator(RedisTemplate<String, Long> idgenRedisTemplate) {
        try {
            RedisClusterConnection redisClusterConnection = idgenRedisTemplate.getConnectionFactory().getClusterConnection();
            if (null != redisClusterConnection) {
                idGenProperties.setRedisMode(RedisMode.CLUSTER);
            }
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
        } catch (Exception e) {
        }
        return new PiecemealRedisStringIdGenerator(idgenRedisTemplate);
    }

    @Bean
    public IdGenerator consecutiveIdGenerator(RedisTemplate<String, Long> idgenRedisTemplate) {
        try {
            RedisClusterConnection redisClusterConnection = idgenRedisTemplate.getConnectionFactory().getClusterConnection();
            if (null != redisClusterConnection) {
                idGenProperties.setRedisMode(RedisMode.CLUSTER);
            }
        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
        } catch (Exception e) {
        }
        return new ConsecutiveRedisStringIdGenerator(idgenRedisTemplate);
    }


}



