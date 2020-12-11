package com.ahdms.framework.core.web.response;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.exception.BusinessException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Katrel.Zhou
 * @Date: 2019/7/10 9:36
 * @Desc: (业务代码)逻辑验证/断言工具类
 */
public abstract class ResultAssert {

    /**
     * 异常抛出
     *
     * @param rCode
     */
    @Deprecated
    public static void throwFail(IResultCode rCode) throws BusinessException {
        throwFail0(rCode);
    }

    public static void throwFail(IAlertAble alertAble) throws BusinessException {
        throwFail0(alertAble);
    }

    /**
     * 断言api异常,可输入国际化参数
     *
     * @param expression
     * @param resultCode
     * @param i18Args
     */
    @Deprecated
    public static void throwOnFalse(boolean expression, IResultCode resultCode, Object... i18Args) throws BusinessException {
        throwOnFalse0(expression, resultCode, i18Args);
    }

    public static void throwOnFalse(boolean expression, IAlertAble alertAble, Object... i18Args) throws BusinessException {
        throwOnFalse0(expression, alertAble, i18Args);
    }

    @Deprecated
    public static void throwFail(IResultCode rCode, Object... i18nArgs) throws BusinessException {
        throwFail0(rCode, i18nArgs);
    }

    public static void throwFail(IAlertAble alertAble, Object... i18nArgs) throws BusinessException {
        throwFail0(alertAble, i18nArgs);
    }

    /**
     * 断言api异常,可输入国际化参数
     *
     * @param expression
     * @param code
     * @param message
     * @param i18Args    国际化参数
     * @throws BusinessException
     */
    public static void throwOnFalse(boolean expression, String code, String message, Object... i18Args) throws BusinessException {
        throwOnFalse0(expression,
                SimpleAlertAble.builder().code(code).message(message).build(), i18Args);
    }

    public static void throwFail(String code, String message, Object... i18Args) {
        throwFail0(SimpleAlertAble.builder().code(code).message(message).build(), i18Args);
    }

    /**
     * 断言api异常,全量参数
     *
     * @param expression
     * @param resultCode
     * @param i18Args
     * @throws BusinessException
     */
    @Deprecated
    private static void throwOnFalse0(boolean expression, IResultCode resultCode, Object... i18Args) throws BusinessException {
        if (!expression) {
            throwFail0(resultCode, i18Args);
        }
    }

    private static void throwOnFalse0(boolean expression, IAlertAble alertAble, Object... i18Args) throws BusinessException {
        if (!expression) {
            throwFail0(alertAble, i18Args);
        }
    }

    @Deprecated
    private static void throwFail0(IResultCode resultCode, Object... i18Args) throws BusinessException {
        BusinessException exception = new BusinessException(resultCode);
        exception.setArgs(i18Args);
        throw exception;
    }

    private static void throwFail0(IAlertAble alertAble, Object... i18Args) throws BusinessException {
        throwFail0(alertAble.getCode(), alertAble.getMessage(), i18Args);
    }

    private static void throwFail0(Object code, String message, Object... i18Args) throws BusinessException {
        throw new BusinessException(code, message, i18Args);
    }


    @Getter
    @Setter
    @Builder
    public static class SimpleAlertAble implements IAlertAble {
        private String code;
        private String message;
    }
}
