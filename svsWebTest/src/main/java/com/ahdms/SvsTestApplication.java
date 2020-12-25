package com.ahdms;

import com.ahdms.framework.core.aspect.AspectAutoConfiguration;
import com.ahdms.framework.core.aspect.ControllerAspect;
import com.ahdms.framework.core.context.DmsServletConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qinxiang
 * @date 2020-12-16 18:06
 */
@SpringBootApplication(exclude = { AspectAutoConfiguration.class, ControllerAspect.class,DmsServletConfiguration.class})
public class SvsTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvsTestApplication.class,args);
    }

}
