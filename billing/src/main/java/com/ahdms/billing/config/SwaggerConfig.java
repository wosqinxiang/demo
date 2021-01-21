package com.ahdms.billing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable:true}")
    private boolean swaggerEnable;
    
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerEnable)
                .apiInfo(testApiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.ahdms.billing.controller")).paths(PathSelectors.any())
                .build();
    }

    private ApiInfo testApiInfo() {
        Contact contact = new Contact("qx", "http://www.ahdms.com", "qinxiang@ahdms.com");
        ApiInfo apiInfo = new ApiInfo("运营计费系统接口", // 大标题
                "", // 小标题
                "0.1", // 版本
                "www.ahdms.com", contact, // 作者
                "主页", // 链接显示文字
                ""// 网站链接
        );
        return apiInfo;
    }

}
