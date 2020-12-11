package com.ahdms.framework.mybatis.config;

import com.ahdms.framework.mybatis.annotation.TransactionEnhance;
import com.ahdms.framework.mybatis.handler.DmsLocalDateTimeTypeHandler;
import com.ahdms.framework.mybatis.handler.DmsLocalDateTypeHandler;
import com.ahdms.framework.mybatis.handler.DmsLocalTimeTypeHandler;
import com.ahdms.framework.mybatis.injector.TableBIdSqlInjector;
import com.ahdms.framework.mybatis.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.ibatis.type.EnumTypeHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * MybatisPlus 配置
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Configuration
@PropertySource({
		"classpath:/properties/mybatis.properties"
})
public class MybatisPlusAutoConfiguration {

	@Bean
	public TransactionEnhance genSpringAopWrapper() {
		return new TransactionEnhance();
	}

	/**
	 * mybatis-plus 乐观锁拦截器
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

	/**
	 * mybatis-plus分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

	/**
	 * sql 注入
	 */
	@Bean
	public ISqlInjector sqlInjector() {
		return new TableBIdSqlInjector();
	}

	/**
	 * SQL执行效率插件
	 *
	 * @return PerformanceInterceptor
	 */
	@Configuration
	@Profile({"dev", "test"})
	@ConditionalOnProperty(value = "mybatis-plus.sql-log.enable", matchIfMissing = true)
	public static class MybatisPlusSqlLog {

		@Bean
		public PerformanceInterceptor performanceInterceptor(Environment environment) {
			return new PerformanceInterceptor();
		}
	}

	/**
	 * IEnum 枚举配置
	 */
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return new MybatisPlusCustomizers();
	}

	/**
	 * 自定义配置
	 */
	public static class MybatisPlusCustomizers implements ConfigurationCustomizer {

		@Override
		public void customize(MybatisConfiguration configuration) {
			configuration.getLanguageRegistry().setDefaultDriverClass(MybatisPlusXmlLanguageDriver.class);
			configuration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
			configuration.getTypeHandlerRegistry().register(LocalDate.class, DmsLocalDateTypeHandler.class);
			configuration.getTypeHandlerRegistry().register(LocalDateTime.class, DmsLocalDateTimeTypeHandler.class);
			configuration.getTypeHandlerRegistry().register(LocalTime.class, DmsLocalTimeTypeHandler.class);
		}

	}

	/**
	 * MetaObjectHandler
	 */
	@Bean
	public MetaObjectHandler metaObjectHandler(ObjectProvider<IMetaObjectDataSources> objectProvider) {
		IMetaObjectDataSources metaObjectDataSources = objectProvider.getIfAvailable(() -> new IMetaObjectDataSources() {
		});
		return new MybatisPlusMetaObjectHandler(metaObjectDataSources);
	}

}
