package com.ahdms.framework.core.alert;


import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/21 9:39
 */
public interface IAlertAble extends Serializable {

    Object getCode();

    String getMessage();

    /**
     * @return http code
     */
    default int getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
