package com.ahdms.framework.feign;

import com.ahdms.framework.feign.interceptor.ProxyFeignClientConfiguration;
import com.ahdms.framework.feign.logger.FeignRequestLoggingInterceptor;
import com.ahdms.framework.feign.logger.*;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;


/**
 * feign 增强配置
 *
 * @author Katrel.zhou
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(ProxyFeignClientConfiguration.class)
@PropertySource({
	"classpath:/properties/feignclient.properties"
})
public class DmsFeignAutoConfiguration {

	//	@Configuration("hystrixFeignConfiguration")
//	@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
//	protected static class HystrixFeignConfiguration {
//		@Bean
//		@Primary
//		@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//		@ConditionalOnProperty("feign.hystrix.enabled")
//		public Feign.Builder feignHystrixBuilder(RequestInterceptor requestInterceptor) {
//			return HystrixFeign.builder()
//				.decode404()
//				.requestInterceptor(requestInterceptor);
//		}
//	}

	@Bean
	@Profile({"dev", "test", "pre"})
	public Logger.Level feignLoggerLevel() {
		return Logger.Level.FULL;
	}

	@Bean
	public FeignLogger feignLogger() {
		return new FeignLogger();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ErrorDecoder feignErrorDecoder() {
		return new DmsFeignErrorDecoder();
	}


	@Bean("feignHeaderInterceptor")
	@ConditionalOnMissingBean
	public RequestInterceptor feignHeaderInterceptor() {
		return new DmsFeignHeaderInterceptor();
	}

	@Bean("feignRequestLoggingInterceptor")
	@ConditionalOnMissingBean
	public FeignRequestLoggingInterceptor feignRequestLoggingInterceptor(FeignAccessLogger feignAccessLogger) {
		return new FeignRequestLoggingInterceptor(feignAccessLogger);
	}
}
