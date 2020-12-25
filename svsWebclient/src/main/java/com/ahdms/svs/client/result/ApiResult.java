package com.ahdms.svs.client.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinxiang
 * @date 2020-12-24 14:44
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

    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>("0","",data);
    }
}
