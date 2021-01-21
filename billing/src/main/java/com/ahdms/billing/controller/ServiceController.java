package com.ahdms.billing.controller;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.service.ServiceInfoService;
import com.ahdms.billing.service.ServiceTypeService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "api/service")
public class ServiceController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    @Autowired
    private ServiceInfoService serviceInfoService;

    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ServiceInfoMapper serviceInfoMapper;

    @SysLog(comment="新增服务")
    @RequestMapping(value = "/addService", method = {RequestMethod.POST})
    @ApiOperation(value = "新增服务", notes = "新增服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型(1:身份认证服务 2:电子认证服务)", required = true, dataType = "String")})
    public Result<Object> addService(HttpServletRequest request,
                                  @RequestParam(required = true) String serviceName,
                                  @RequestParam(required = true) String serviceEncode,
                                  @RequestParam(required = true) String serviceType)
            throws Exception {
        Result<Object> result = new Result<>();
        logger.debug(
                "serviceName=" + serviceName + ",serviceEncode=" + serviceEncode + ",serviceType=" + serviceType);

        ServiceInfo serviceNameInfo = serviceInfoService.queryByServiceName(serviceName).getData();
        if (serviceNameInfo != null) {
            result.setCode(201);
            result.setMessage("该名称已存在");
            return result;
        }

        ServiceInfo serviceEncodeInfo = serviceInfoService.queryByServiceEncode(serviceEncode).getData();
        if (serviceEncodeInfo != null) {
            result.setCode(201);
            result.setMessage("该编码已存在");
            return result;
        }

        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setId(UUIDGenerator.getUUID());
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceEncode(serviceEncode);
        serviceInfo.setServiceType(serviceType);
        try {
            result = serviceInfoService.addService(serviceInfo);
            result.setCode(0);
            result.setData(serviceInfo);
            result.setMessage("新增成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="删除服务")
    @RequestMapping(value = "/deleteServiceById", method = {RequestMethod.POST})
    @ApiOperation(value = "删除服务", notes = "删除服务")
    @ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务Id", required = true, dataType = "String")
    public Result<Object>deleteServiceById(HttpServletRequest request,
                                     @RequestParam(required = true) String serviceId)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            ServiceInfo serviceInfo = serviceInfoService.queryServiceById(serviceId).getData();
            serviceInfoService.deleteServiceById(serviceId);
            result.setCode(0);
            result.setData(serviceInfo);
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="修改服务")
    @RequestMapping(value = "/updateService", method = {RequestMethod.POST})
    @ApiOperation(value = "修改服务", notes = "修改服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务Id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型(1:身份认证服务 2:电子认证服务)", required = true, dataType = "String")})
    public Result<ServiceInfo> update(HttpServletRequest request,
                                      @RequestParam(required = true) String serviceId,
                                      @RequestParam(required = true) String serviceName,
                                      @RequestParam(required = true) String serviceEncode,
                                      @RequestParam(required = true) String serviceType)
            throws Exception {
        Result<ServiceInfo> result = new Result<>();
        logger.debug(
                "serviceName=" + serviceName + ",serviceEncode=" + serviceEncode + ",serviceType=" + serviceType);
        ServiceInfo serviceInfo=serviceInfoService.queryServiceById(serviceId).getData();

        ServiceInfo serviceNameInfo = serviceInfoService.queryByServiceName(serviceName).getData();
        if (serviceNameInfo != null) {
            if(!serviceId.equals(serviceNameInfo.getId())){
                result.setCode(201);
                result.setMessage("该名称已存在");
                return result;
            }

        }

        ServiceInfo serviceEncodeInfo = serviceInfoService.queryByServiceEncode(serviceEncode).getData();
        if (serviceEncodeInfo != null) {
            if(!serviceId.equals(serviceEncodeInfo.getId())){
                result.setCode(201);
                result.setMessage("该编码已存在");
                return result;
            }
        }

        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceEncode(serviceEncode);
        serviceInfo.setServiceType(serviceType);
        try {
            result = serviceInfoService.updateService(serviceInfo);
            result.setCode(0);
            result.setData(serviceInfo);
            result.setMessage("修改成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryServiceById", method = {RequestMethod.POST})
    @ApiOperation(value = "根据服务ID查询服务", notes = "查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务id", required = true, dataType = "String")})
    public Result<ServiceInfo> queryUserById(HttpServletRequest request,
                                          @RequestParam(required = true) String serviceId)throws Exception{
        Result<ServiceInfo> result = new Result<>();
        try {
            result = serviceInfoService.queryServiceById(serviceId);
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryByServiceCode", method = {RequestMethod.POST})
    @ApiOperation(value = "根据服务编码查询服务", notes = "根据服务编码查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "serviceCode", value = "服务编码", required = true, dataType = "String")})
    public Result<Object> queryByServiceCode(HttpServletRequest request,
                                             @RequestParam(required = true) String serviceCode) throws Exception{
        Result<Object> result = new Result<>();
        try {
            result = serviceInfoService.selectByServiceCode(serviceCode);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/serviceList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务列表(包含搜索)", notes = "查询服务列表(包含搜索)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页页码", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页大小", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型 1:身份认证服务 2:电子认证服务", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "searchType", value = "搜索类型 1:根据服务名称搜索 2:根据服务编码搜索", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceName", value = "服务名称", required = false, dataType = "String")})
    public Result<Object> findAll(HttpServletRequest request,
                                  @RequestParam(required = false,defaultValue="0") int page,
                                  @RequestParam(required = false,defaultValue="10") int size,
                                  @RequestParam(required = true) String serviceType,
                                  @RequestParam(required = false) String searchType,
                                  @RequestParam(required = false) String serviceEncode,
                                  @RequestParam(required = false) String serviceName)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            if (searchType.equals("1")) {
                result = serviceInfoService.queryLikeServiceName(page, size, serviceType, serviceName);
                result.setCode(0);
                result.setMessage("查询成功");
            } else if (searchType.equals("2")) {
                result = serviceInfoService.queryLikeServiceEncode(page, size, serviceType, serviceEncode);
                result.setCode(0);
                result.setMessage("查询成功");
            } else {
                result.setCode(0);
                result.setMessage("传入的searchType有误，搜索类型 1:根据服务名称搜索 2:根据服务编码搜索");
            }

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/serviceType", method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务类型", notes = "查询服务类型")
    public Result<Object> serviceType(HttpServletRequest request)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            result = serviceTypeService.findAll();
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryServiceNameList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务名称", notes = "查询服务名称")
    public Result<Object> queryServiceNameList(HttpServletRequest request)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            List<String> serviceNameList = new ArrayList<>();
            List<ServiceInfo> list = serviceInfoMapper.findAll();
//            for (ServiceInfo serviceInfo : list) {
//                serviceNameList.add(serviceInfo.getServiceName());
//            }
//
//            //数组去重处理
//            LinkedHashSet<String> hashSet = new LinkedHashSet<>(serviceNameList);
//            ArrayList<String> data = new ArrayList<>(hashSet);

            result.setCode(0);
            result.setData(list);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @RequestMapping(value = "/queryServiceByType", method = {RequestMethod.POST})
    @ApiOperation(value = "根据类型查询服务名称", notes = "查询服务名称")
    public Result<Object> queryServiceByType(@RequestParam String typeId)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            List<ServiceInfo> list = serviceInfoMapper.selectServiceByType(typeId);

            result.setCode(0);
            result.setData(list);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
}
