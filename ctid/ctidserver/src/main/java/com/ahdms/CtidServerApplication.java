/**
 * Created on 2018年7月9日 by xiaopu
 */
package com.ahdms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import io.micrometer.core.instrument.MeterRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableDubbo
@MapperScan("com.ahdms.ctidservice.dao")
public class CtidServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CtidServerApplication.class, args);
	}
	
	@Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(
            @Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }
	
}
