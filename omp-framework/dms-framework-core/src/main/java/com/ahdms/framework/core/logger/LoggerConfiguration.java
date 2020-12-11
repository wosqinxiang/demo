package com.ahdms.framework.core.logger;

import com.ahdms.framework.core.logger.mdc.MdcLogFilter;
import com.ahdms.framework.core.web.DmsWebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 *
 */
@Slf4j
@Configuration
public class LoggerConfiguration {

	@Value("${logging.access.enable:true}")
	private boolean logAble;

	@Bean
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public FilterRegistrationBean mdcLogFilter() {
		FilterRegistrationBean<MdcLogFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new MdcLogFilter());
 		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registrationBean.addUrlPatterns(DmsWebMvcConfigurer.API_MAPPING, DmsWebMvcConfigurer.RMI_MAPPING);
		return registrationBean;
	}

	@Bean
	public ControllerAccessLogger controllerAccessLogger() {
		return new DefaultControllerAccessLogger(logAble);
	}

}
