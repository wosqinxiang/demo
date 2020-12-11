package com.ahdms.framework.core.exception;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.alert.IArgsAlertAble;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.web.response.IResultCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 15:38
 */
public class BusinessException extends RuntimeException implements IArgsAlertAble {
    private static final long serialVersionUID = -7374847755494906434L;

    @Getter
    @Setter
    private Map<String, Object> values;

    @Getter
    private Object code;

    @Getter
    @Setter
    private Object[] args;

    public BusinessException(IAlertAble alertAble) {
        this(alertAble.getCode(), alertAble.getMessage());
    }

    public BusinessException(IAlertAble alertAble, Object[] args) {
        this(alertAble);
        setArgs(args);
    }

    @Deprecated
    public BusinessException(IResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg());
    }

    @Deprecated
    public BusinessException(IResultCode resultCode, Object[] args) {
        this(resultCode);
        setArgs(args);
    }

    public BusinessException(Object code, String message) {
        this(code, message, null);
    }

    public BusinessException(Object code, String message, Object[] args) {
        this(code, message, null, args);
    }


    public BusinessException(Object code, String message, Exception cause, Object[] args) {
        super(StringUtils.format(message, args), cause);
        this.code = code;
    }
}
