package com.ahdms.framework.core.aspect;

import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import com.ahdms.framework.core.aspect.customizer.AspectAfterProcess;
import com.ahdms.framework.core.aspect.customizer.AspectBeforeProcess;
import com.ahdms.framework.core.aspect.customizer.AspectCustomizerConfiguration;
import com.ahdms.framework.core.aspect.customizer.AspectThrowableProcess;
import com.ahdms.framework.core.logger.ControllerAccessLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 11:06
 */
@Import(AspectCustomizerConfiguration.class)
@Configuration
public class AspectAutoConfiguration {

}
