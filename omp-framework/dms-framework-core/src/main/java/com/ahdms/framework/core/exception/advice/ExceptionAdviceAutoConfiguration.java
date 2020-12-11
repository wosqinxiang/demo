package com.ahdms.framework.core.exception.advice;

import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/6 17:24
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ExceptionAdviceAutoConfiguration {
    @Bean
    public ControllerExceptionAdvice controllerExceptionAdvice(IAlertTranslator alertTranslator) {
        return new ControllerExceptionAdvice(alertTranslator);
    }
}
