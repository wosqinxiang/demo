package com.ahdms.framework.feign.ribbon.support;

import com.ahdms.framework.feign.ribbon.rule.DiscoveryEnabledRule;
import com.ahdms.framework.feign.ribbon.rule.MetadataAwareRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

/**
 * Ribbon discovery filter auto configuration.
 *
 * @author Katrel.zhou
 */
@Configuration
@RequiredArgsConstructor
@Profile({"dev", "test"})
@AutoConfigureBefore(RibbonClientConfiguration.class)
@EnableConfigurationProperties(DmsRibbonProperties.class)
@ConditionalOnProperty(value = "dms.ribbon.filter.enabled", matchIfMissing = true)
public class RibbonDiscoveryRuleAutoConfiguration {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public DiscoveryEnabledRule metadataAwareRule() {
		return new MetadataAwareRule();
	}
}
