package com.ahdms;

import com.ahdms.framework.core.aspect.AspectAutoConfiguration;
import com.ahdms.framework.core.aspect.ControllerAspect;
import com.ahdms.framework.core.context.DmsServletConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author qinxiang
 * @date 2020-12-16 18:06
 */
@SpringBootApplication(exclude = { AspectAutoConfiguration.class, ControllerAspect.class,DmsServletConfiguration.class})
@MapperScan("com.ahdms.dao")
@EnableTransactionManagement
public class SvsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvsTestApplication.class,args);
    }

}
