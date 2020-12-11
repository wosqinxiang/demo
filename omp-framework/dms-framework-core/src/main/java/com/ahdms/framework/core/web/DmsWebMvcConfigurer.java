package com.ahdms.framework.core.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 15:38
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DmsWebMvcConfigurer implements WebMvcConfigurer {


	public final static String API_MAPPING = "/api/*";
	public final static String RMI_MAPPING = "/rmi/*";
	public final static Pattern API_PATTERN = Pattern.compile("^\\/api\\/.*", Pattern.CASE_INSENSITIVE);
	public final static Pattern RMI_PATTERN = Pattern.compile("^\\/rmi\\/.*", Pattern.CASE_INSENSITIVE);

	public static boolean isApiRequest(HttpServletRequest request) {
		return API_PATTERN.matcher(request.getServletPath()).matches();
	}

	public static boolean isRmiRequest(HttpServletRequest request) {
		return RMI_PATTERN.matcher(request.getServletPath()).matches();
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(API_MAPPING);
	}


}
