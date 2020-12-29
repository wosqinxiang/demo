package com.ahdms.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Slf4j
@Configuration
public class SvsServletConfiguration extends WebMvcConfigurerAdapter {

//    @Autowired
//    private SvsContextFilter svsContextFilter;

    @Autowired
    private SvsInterceptor svsInterceptor;

//    @Bean
//    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//    public FilterRegistrationBean svsCallContextFilter() {
//        FilterRegistrationBean<SvsContextFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(svsContextFilter);
//        registrationBean.addUrlPatterns("/api/*");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return registrationBean;
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(svsInterceptor).addPathPatterns("/api/**"); // 这个链接来的请求进行拦截
//        registry.addInterceptor(authInterceptor).addPathPatterns("/**").excludePathPatterns("/api/**","/swagger-ui.html","swagger-ui.html/**"); // 这个链接来的请求进行拦截

    }

}
