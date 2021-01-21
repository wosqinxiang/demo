package com.ahdms.billing.controller;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.service.IOmpApiService;
import com.ahdms.billing.vo.omp.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qinxiang
 * @date 2020-07-24 13:52
 */
@RestController
@RequestMapping("/api/omp")
public class OmpApiController {

    @Autowired
    private IOmpApiService ompApiService;

    @PostMapping("logs")
    @ApiOperation(value = "获取计费系统业务日志", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<PageResult<ServiceLogVo>> getLogs(@RequestBody(required = false) ServiceLogPageReqVo serviceLogPageReqVo) {
        try {
            return ompApiService.getLogs(serviceLogPageReqVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("error");
    }

    @GetMapping("customer/product")
    @ApiOperation(value = "获取客户产品剩余次数", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<List<CustomerProductRspVo>> getCustPro() {
        Result<List<CustomerProductRspVo>> result = new Result();
        try {
            result = ompApiService.getCustPro();
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.error("error");
        }
//        String s = JSON.toJSONString(result, SerializerFeature.WriteDateUseDateFormat);
//        System.out.println(s);
//        JSONObject
        return result;

    }

    @PostMapping("customer/info")
    @ApiOperation(value = "推送客户信息至计费系统", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> pushCustomerInfo(@RequestBody CustomerInfoReqVo customerInfoReqVo) {
        try {
            return ompApiService.pushCustomerInfo(customerInfoReqVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("error");


    }

    @PostMapping("customer/order")
    @ApiOperation(value = "推送客户订单至计费系统", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> pushCustomerOrder(@RequestBody CustomerOrderReqVo customerInfoReqVo) {
        try {
            return ompApiService.pushCustomerOrder(customerInfoReqVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("error");

    }

    @PostMapping("product")
    @ApiOperation(value = "推送产品信息至计费系统", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> pushProductInfo(@RequestBody ProductInfoReqVo productInfoReqVo) {
        try {
            return ompApiService.pushProductInfo(productInfoReqVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("error");
    }

}
