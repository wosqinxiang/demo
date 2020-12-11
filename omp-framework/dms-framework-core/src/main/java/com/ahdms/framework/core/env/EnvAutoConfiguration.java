package com.ahdms.framework.core.env;

import com.ahdms.framework.core.commom.util.BeanLoaderUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 平台环境配置
 *
 * @author Katrel.Zhou
 * @version 1.0.0
 * @date 2019/5/20 10:30
 */
@Configuration
@EnableConfigurationProperties(DmsServerProperties.class)
public class EnvAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public BeanLoaderUtils extensionLoader() {
		return new BeanLoaderUtils();
	}

	@Bean
	@ConditionalOnMissingBean
	public DmsEnvironmentAware environmentAware(DmsServerProperties dmsServerProperties) {
		return new DmsEnvironmentAware(dmsServerProperties);
	}

	@Bean
	@Primary
	public ServerInfo envServerInfo(ServerProperties serverProperties) {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setServerProperties(serverProperties);
		return serverInfo;
	}
}
