package com.ahdms.billing.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/**
 * 
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@Configuration
public class WebSecurityConf extends WebSecurityConfigurerAdapter {

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	private DataSource dataSource;

	/**
	 * http://docs.spring.io/spring-security/site/docs/4.1.1.RELEASE/reference/
	 * htmlsingle/#jc-authentication-jdbc
	 * 自动在数据库建立users,authorities表并初始化基础数据,但是重启服务又会尝试建表导致报错不能启动
	 *
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource);
		// 第二次启动注释掉以下两行代码,不构建数据库和建立
		// .withDefaultSchema()
		// .withUser("admin").password("password").roles("ADMIN",
		// "CERT_MANAGER");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//        http.authorizeRequests().anyRequest().permitAll();
		//        http.csrf().disable();

		http.authorizeRequests()
		//管理员权限
		.antMatchers("/api/AdminUserInfoLogin/delUser","/api/AdminUserInfoLogin/addUser","/api/AdminUserInfoLogin/unlockUser")
		.hasRole("SUPER_ADMIN")
		//登录用户权限        "/report/export",
		.antMatchers("/api/AdminUserInfoLogin/SysUserList","/api/AdminUserInfoLogin/showSysUser","/api/AdminUserInfoLogin/logout",
				"/api/AdminUserInfoLogin/EditUserPassWord","/api/provider/addProvider","/api/provider/editProvider","/api/serviceLog/export","/api/manage/manageLogExport","/api/channel/addChannel",
				"/api/channel/deleteChannelById","/api/channel/updateChannel","/api/keyInfo/**",
				"/api/addSpecialLine","/api/updateSpecialLine","/api/findSpecialLine","/api/deleteSpecialLine","/api/addSpecialLineService","/api/deleteSpecialLineService",
				"/api/findSpecialLineService","/api/findSpecial","/api/devType/**","/api/box/**","/api/service/addService",
				"/api/service/deleteServiceById","/api/service/updateService","/api/user/updateUserService",
				"/api/user/addUser","/api/user/updateUser","/api/user/resetUserById","/api/user/updateUserStatusById","/api/user/addUserService")
//		.hasAnyRole("SUPER_ADMIN", "NORMAL_ADMIN")

//		.antMatchers("/api/user/*","/api/omp/**","/api/AdminUserInfoLogin/login",  "/api/AdminUserInfoLogin/userNameCheck", "/druid/**" ,
//				"/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/static/**")
		.permitAll();
		//                .anyRequest().authenticated();
		//        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).ignoringAntMatchers("/druid/**");
		http.csrf().disable();
		http.sessionManagement().maximumSessions(1).expiredUrl("/static/index.html#/login");
	}
}
