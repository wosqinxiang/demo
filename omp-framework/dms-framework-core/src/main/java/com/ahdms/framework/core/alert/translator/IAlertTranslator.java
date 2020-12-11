package com.ahdms.framework.core.alert.translator;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.web.response.IResultCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:38
 */
public interface IAlertTranslator {

    AlertInfo translate(IAlertAble alertAble, Object[] args);

    AlertInfo translate(String code, String message, Object[] args);

    @Getter
    @Setter
    @Builder
    class AlertInfo {
        private Object code;
        private String message;
    }
}
