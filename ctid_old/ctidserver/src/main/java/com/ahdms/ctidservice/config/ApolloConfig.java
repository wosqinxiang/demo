package com.ahdms.ctidservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

import com.ctrip.framework.apollo.spring.annotation.ApolloConfigRegistrar;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;

@Configuration
@ConditionalOnProperty(name="apollo.enable", havingValue="true")
@EnableApolloConfig
public class ApolloConfig {
	

}
