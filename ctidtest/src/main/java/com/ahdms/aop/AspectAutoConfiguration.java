package com.ahdms.aop;

import com.ahdms.aop.customizer.AspectCustomizerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 11:06
 */
@Import(AspectCustomizerConfiguration.class)
@Configuration
public class AspectAutoConfiguration {

}
