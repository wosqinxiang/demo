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
@Configuration
public class SvsServletConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private SvsInterceptor svsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(svsInterceptor).addPathPatterns("/api/svs/**");
    }

}
