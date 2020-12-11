package com.ahdms.framework.core.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@PropertySource({
	"classpath:/properties/base.properties"
})
public class DmsBaseConfiguration {
}
