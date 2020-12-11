package com.ahdms.framework.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 10:02
 */
@Data
@Builder
public class ExceptionInfo {
    @JsonIgnore
    private Integer status;
    private String requestId;
    private String code;
    private String message;
    private String detailMessage;
    private Date timestamp;

}
