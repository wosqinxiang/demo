package com.ahdms.ctidservice.config;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name="apollo.enable", havingValue="true")
@EnableApolloConfig
public class ApolloConfig {
	

}
