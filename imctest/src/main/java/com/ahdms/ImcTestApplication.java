package com.ahdms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;

/**
 * @author qinxiang
 * @date 2020-12-11 13:56
 */
@SpringBootApplication(exclude = RedisReactiveAutoConfiguration.class, scanBasePackages = {"com.ahdms.controller","com.ahdms.dao","com.ahdms.service"})
@MapperScan("com.ahdms.dao")
public class ImcTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImcTestApplication.class,args);
    }

}
