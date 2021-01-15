//package com.ahdms.ap.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//@Configuration
//@EnableSwagger2
////@ConditionalOnExpression("'${swagger.enable}' == 'true'")
//public class SwaggerConfig {
//
//	@Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(testApiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.ahdms.ap.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//	private ApiInfo testApiInfo() {
//		Contact contact = new Contact("刘奕玭", "http://www.ahdms.com", "liuyipin@ahdms.com");
//		ApiInfo apiInfo = new ApiInfo("可信身份认证服务平台",//大标题
//				"REST风格API",//小标题
//				"0.1",//版本
//				"www.ahdms.com",
//				contact,//作者
//				"主页",//链接显示文字
//				""//网站链接
//				);
//		return apiInfo;
//	}
//
//}
