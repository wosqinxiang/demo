package com.ahdms.framework.idgen.config;

import com.ahdms.framework.core.commom.util.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Optional;

/***
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public class IdGeneratorExclusiveRedisCondition extends SpringBootCondition {
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConditionMessage.Builder message = ConditionMessage.forCondition("Exclusive Redis for Id Generator Condition");
        Environment environment = context.getEnvironment();
        String url = environment.getProperty("dms.idgen.redis.url");
        String host = environment.getProperty("dms.idgen.redis.host");
        String nodes = Optional.ofNullable(environment.getProperty("dms.idgen.redis.sentinel.nodes"))
                .orElseGet(() -> environment.getProperty("dms.idgen.redis.cluster.nodes"));
        if (StringUtils.isBlank(url)
                && StringUtils.isBlank(host)
                && StringUtils.isBlank(nodes)) {
            return ConditionOutcome.noMatch(
                    message.didNotFind("dms.idgen.redis property").items("url", "host", "sentinel", "cluster"));
        }

        return ConditionOutcome.match(message.because("one of property 'dms.idgen.redis.url/host/sentinel/cluster' found. "));
    }

}
