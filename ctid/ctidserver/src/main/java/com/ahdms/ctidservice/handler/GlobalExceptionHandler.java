package com.ahdms.ctidservice.handler;

import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.framework.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qinxiang
 * @date 2020-12-04 14:29
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    CtidResult handleException(Exception e){
        if(e instanceof BusinessException){
            ((BusinessException) e).getCode();
            return CtidResult.error(e.getMessage());
        }

        return CtidResult.error("服务器异常,请重试!");
    }


    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    CtidResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        return CtidResult.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }



}
