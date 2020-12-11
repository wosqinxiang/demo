package com.ahdms.framework.core.web.response;

import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ResponseMessageResolver defaultResponseMessageResolver(IAlertTranslator alertTranslator) {
        return new DefaultResponseMessageResolver(alertTranslator);
    }
}
