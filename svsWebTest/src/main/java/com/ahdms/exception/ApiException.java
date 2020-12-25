package com.ahdms.exception;

import lombok.Getter;

/**
 * @author qinxiang
 * @date 2020-12-21 14:00
 */
public class ApiException extends RuntimeException{

    @Getter
    private String code;

    public ApiException(String code,String message){
        super(message);
    }

    public ApiException(String message){
        super(message);
    }

}
