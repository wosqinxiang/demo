package com.ahdms.billing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubboConfig;

import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.ahdms.billing.dao")
@EnableDubboConfig
public class BillingApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BillingApplication.class, args);
	}
	
	@Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
	
}
