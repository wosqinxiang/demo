package com.ahdms.framework.core.constant;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.web.response.IResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:56
 */
@AllArgsConstructor
@Getter
public enum SystemError implements IAlertAble {
    INTERNAL_ERROR("SYS00001", "系统开小差了，请稍后再试"),
    ILLEGAL_PARAM("SYS00400", "请求内容校验失败，请确认后再提交"),
    NO_HANDLER_FOUND_ERROR("SYS00404", "请求的资源不存在，请确认后重试"),

    CLIENT_EXCEPTION("SYS00501", "远程服务调用失败，请稍后再试"),
    REMOTE_EXCEPTION("SYS00502", "远程服务调用失败，请稍后再试"),
    ;

    private Object code;
    private String message;

}
