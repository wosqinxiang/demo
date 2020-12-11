package com.ahdms.framework.core.exception;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.constant.SystemError;
import lombok.Getter;

/**
 * 框架级级别异常
 *
 * @author Katrel.zhou
 */
public class FrameworkException extends RuntimeException implements IAlertAble {

    private static final long serialVersionUID = 5055293222416543609L;
    @Getter
    private Object code;

    public FrameworkException(IAlertAble alertAble) {
        this(alertAble.getCode(), alertAble.getMessage());
    }

    public FrameworkException(IAlertAble alertAble, Object... args) {
        this(alertAble.getCode(), alertAble.getMessage(), args);
    }

    public FrameworkException(IAlertAble alertAble, Throwable cause) {
        this(alertAble.getCode(), alertAble.getMessage(), cause);
    }

    public FrameworkException(IAlertAble alertAble, Throwable cause, Object... args) {
        this(alertAble.getCode(), alertAble.getMessage(), cause, args);
    }

    public FrameworkException(String message, Object... args) {
        super(StringUtils.format(message, args));
        this.code = SystemError.INTERNAL_ERROR.getCode();
    }

    public FrameworkException(Object code, String message, Object... args) {
        super(StringUtils.format(message, args));
        this.code = code;
    }

    public FrameworkException(String code, String message, Throwable cause, Object... args) {
        super(StringUtils.format(message, args), cause);
        this.code = code;
    }


    public FrameworkException(Throwable cause, String message, Object... args) {
        super(StringUtils.format(message, args), cause);
    }

    public static void throwFail(String message, Object... args) {
        throw new FrameworkException(message, args);
    }

    public static void throwFail(IAlertAble alertAble, Object... args) {
        throw new FrameworkException(alertAble, args);
    }

    public static void throwFail(IAlertAble alertAble, Throwable cause) {
        throw new FrameworkException(alertAble, cause);
    }


    public static void throwOnFalse(boolean expression, IAlertAble alertAble, Object... args) {
        if (!expression) {
            throw new FrameworkException(alertAble, args);
        }
    }

    public static void throwOnFalse(boolean expression, String message, Object... args) {
        if (!expression) {
            throw new FrameworkException(message, args);
        }
    }

    public static void throwOnFalse(boolean expression, IAlertAble alertAble, Throwable cause, Object... args) {
        if (!expression) {
            throw new FrameworkException(alertAble, cause, args);
        }
    }

}
