package com.ahdms.framework.core.context;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Configuration
@EnableConfigurationProperties(DmsContextProperties.class)
public class DmsContextAutoConfiguration {

}
