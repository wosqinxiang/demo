package com.ahdms.context;

import com.ahdms.framework.core.context.DmsContextFilter;
import com.ahdms.framework.core.web.DmsWebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Slf4j
@Configuration
public class SvsServletConfiguration {

    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean svsCallContextFilter() {
        FilterRegistrationBean<SvsContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SvsContextFilter());
        registrationBean.addUrlPatterns("/api/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
