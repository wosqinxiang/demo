package com.ahdms.billing.controller;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.service.WhiteIpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinxiang
 * @date 2021-01-04 14:01
 */
@RestController
public class TestController {

    @Autowired
    WhiteIpService whiteIpService;

    @RequestMapping("/test/whiteIp")
    public Result<Boolean> test(@RequestParam String userId){
        boolean sss = whiteIpService.checkUserIdAndIp(userId, "sss");
        return Result.success(sss);
    }

    @RequestMapping("/test/excelHeader")
    public Result excelHeader(){

        return null;
    }
}
