package com.ahdms.web;

import com.ahdms.aop.MD5Auth;
import com.ahdms.model.User;
import com.ahdms.result.AppResponse;
import com.ahdms.utils.CtidContextUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qinxiang
 * @date 2020-12-04 15:39
 */
@RestController
public class TestController {

    @GetMapping("/test")
    @MD5Auth
    public AppResponse test(@RequestBody User user, HttpServletRequest request){
        User user1 = CtidContextUtils.getUser();

        return AppResponse.success(user1);
    }

    @GetMapping("/test2")
    @MD5Auth
    public AppResponse test2(@RequestBody User user){
        User user1 = CtidContextUtils.getUser();

        return AppResponse.success(user1);
    }


}
