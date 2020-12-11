package com.ahdms.framework.core.web.response;

import java.io.Serializable;

/**
 * 状态码接口
 *
 * replaced by IAlertAble
 *
 * @author Katrel.zhou
 */
@Deprecated
public interface IResultCode extends Serializable {
    /**
     * 返回的code码
     *
     * @return code
     */
    String getCode();

    /**
     * 返回的消息
     *
     * @return 消息
     */
    String getMsg();

    /**
     * @return http code
     */
    default IHttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_ERROR;
    }
}
