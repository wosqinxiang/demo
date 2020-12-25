package com.ahdms.result;

import com.ahdms.code.ApiCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinxiang
 * @date 2020-12-16 18:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResult<T> {

    private String code;
    private String message;
    private T data;

    public static <T> ApiResult<T> error(String message){
        return new ApiResult<>("1",message,null);
    }

    public static <T> ApiResult<T> error(ApiCode apiCode){
        return new ApiResult<>(apiCode.getCode(),apiCode.getMessage(),null);
    }

    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>("0","",data);
    }
}
