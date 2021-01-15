package com.ahdms.ctidservice.config;

import com.ahdms.ctidservice.interceptors.Md5VerfiyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMVC配置，你可以集中在这里配置拦截器、过滤器、静态资源缓存等
 * @author Administrator
 */
@Configuration
public class CustomWebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private Md5VerfiyInterceptor md5VerfiyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(md5VerfiyInterceptor).addPathPatterns("/ctid/**");
    }

    @Qualifier(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new CtidDispatcherServlet();
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

}

