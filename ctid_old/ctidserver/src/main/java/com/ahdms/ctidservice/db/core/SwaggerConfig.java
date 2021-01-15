package com.ahdms.ctidservice.db.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
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

	@Bean
	public Docket testApi() {
		return new Docket(DocumentationType.SWAGGER_2)
		.groupName("test")
		.genericModelSubstitutes(DeferredResult.class)
		//.genericModelSubstitutes(ResponseEntity.class)
		.useDefaultResponseMessages(false)
		.forCodeGeneration(true)
		.pathMapping("/")//api测试请求地址
		.select()
		//.paths(PathSelectors.regex("/common/.*"))//过滤的接口
		.build()
		.apiInfo(testApiInfo());
	}

	private ApiInfo testApiInfo() {
		Contact contact = new Contact("admin", "http://www.ahdms.com", "xiaopu@ahdms.com");
		ApiInfo apiInfo = new ApiInfo("网证API服务器",//大标题
				"REST风格API",//小标题
				"0.1",//版本
				"www.ahdms.com",
				contact,//作者
				"主页",//链接显示文字
				""//网站链接
				);
		return apiInfo;
	}

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(testApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ahdms"))
                .paths(PathSelectors.any())
                .build();
    }
	
}
