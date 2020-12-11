package com.ahdms.framework.core.alert;

import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import com.ahdms.framework.core.alert.translator.SimpleAlertTranslator;
import com.ahdms.framework.core.env.DmsServerProperties;
import com.ahdms.framework.core.env.ServerInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Configuration
public class AlertAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(IAlertTranslator.class)
	public IAlertTranslator simpleAlertTranslator(DmsServerProperties serverProperties, ServerInfo serverInfo) {
		return new SimpleAlertTranslator(serverInfo, serverProperties);
	}
}
