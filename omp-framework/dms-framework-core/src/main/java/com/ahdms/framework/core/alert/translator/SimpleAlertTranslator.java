package com.ahdms.framework.core.alert.translator;

import com.ahdms.framework.core.env.DmsServerProperties;
import com.ahdms.framework.core.env.ServerInfo;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:43
 */
public class SimpleAlertTranslator extends AbstractAlertTranslator {

    public SimpleAlertTranslator(ServerInfo serverInfo, DmsServerProperties serverProperties) {
        super(serverInfo, serverProperties);
    }

    @Override
    public String doTranslation(String code, String message, Object[] args) {
        return Optional.ofNullable(message).map(m -> {
            Locale locale = LocaleContextHolder.getLocale();
            MessageFormat format = new MessageFormat(m, locale);
            return format.format(args);
        }).orElse(null);
    }
}
