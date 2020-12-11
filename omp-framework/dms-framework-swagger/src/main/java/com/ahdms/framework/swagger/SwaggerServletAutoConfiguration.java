package com.ahdms.framework.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.web.Swagger2Controller;

/**
 * Swagger 页面静态文件配置
 *
 * @author L.cm
 */
@Configuration
@EnableSwagger2
@ConditionalOnClass(Swagger2DocumentationConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = "dms.swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerServletAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars*")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 单项目中的 swagger word
     */
//    @Bean
//    public Swagger2WordController swagger2WordController(Environment environment,
//                                                         DocumentationCache documentationCache,
//                                                         ServiceModelToSwagger2Mapper mapper,
//                                                         JsonSerializer jsonSerializer,
//                                                         Swagger2WordService swagger2WordService) {
//        Swagger2Controller swagger2Controller = new Swagger2Controller(environment, documentationCache, mapper, jsonSerializer);
//        return new Swagger2WordController(swagger2Controller, swagger2WordService);
//    }
}
