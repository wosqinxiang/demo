package com.ahdms.framework.seata;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/31 9:59
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@PropertySource({
        "classpath:/properties/seata.properties"
})
public class SeataConfiguration {
}
