package com.ahdms.billing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.ServiceLogQuery;
import com.ahdms.billing.model.report.ServiceLogReport;
import com.ahdms.billing.service.ServiceLogService;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "api/serviceLog")
public class ServiceLogController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogController.class);

    @Autowired
    private ServiceLogService serviceLogService;

    @Autowired
    private ServiceLogMapper serviceLogMapper;

//    @RequestMapping(value = "/addServiceLog", method = {RequestMethod.POST})
//    @ApiOperation(value = "新增业务日志", notes = "新增业务日志")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "username", value = "客户名称", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "typeId", value = "服务类型Id(1:身份认证服务 2:电子认证服务)", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "channelEncode", value = "渠道编码", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "result", value = "操作结果(0:成功 1:失败)", required = true, dataType = "Integer"),
//            @ApiImplicitParam(paramType = "query", name = "message", value = "失败原因", required = false, dataType = "String")})
    public Result<Object> addServiceLo(HttpServletRequest request,
                                     @RequestParam(required = true) String username,
                                     @RequestParam(required = true) String typeId,
                                     @RequestParam(required = true) String serviceEncode,
                                     @RequestParam(required = true) String channelEncode,
                                     @RequestParam(required = true) Integer result,
                                     @RequestParam(required = false) String message)
            throws Exception {
        Result<Object> res = new Result<>();
        logger.debug(
                "username=" + username + ",typeId=" + typeId + ",serviceEncode=" + serviceEncode + ",channelEncode=" + channelEncode + ",result=" + result + ",message=" + message);
        ServiceLog serviceLog = new ServiceLog();
        serviceLog.setId(UUIDGenerator.getUUID());
        serviceLog.setOperationtime(new Date());
        serviceLog.setUsername(username);
        serviceLog.setTypeId(typeId);
        serviceLog.setServiceEncode(serviceEncode);
        serviceLog.setChannelEncode(channelEncode);
        serviceLog.setResult(result);
        serviceLog.setMessage(message);
        try {
            res = serviceLogService.addServiceLog(serviceLog);
            res.setCode(0);
            res.setData(serviceLog);
            res.setMessage("新增成功");
        } catch (Exception e) {
            res.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return res;
    }

    @RequestMapping(value = "/serviceLogList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询业务日志列表", notes = "查询业务日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页页码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页大小", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "服务类型ID(1:身份认证服务 2:电子认证服务)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelEncode", value = "渠道编码", required = false, dataType = "String")
    })
    public Result<Object> serviceLogList(HttpServletRequest request,
                                         @RequestParam(required = true) int page,
                                         @RequestParam(required = true) int size,
                                         @RequestParam(required = false) String beginDate,
                                         @RequestParam(required = false) String endDate,
                                         @RequestParam(required = false) String username,
                                         @RequestParam(required = false) String typeId,
                                         @RequestParam(required = false) String serviceEncode,
                                         @RequestParam(required = false) String channelEncode)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            ServiceLogQuery serviceLogQuery = new ServiceLogQuery();

            if (beginDate!= null && !beginDate.equals("")) {
                serviceLogQuery.setBeginDate(new Date(Long.parseLong(beginDate)));
            }

            if (endDate != null && !endDate.equals("")) {
                serviceLogQuery.setEndDate(new Date(Long.parseLong(endDate)));
            }

            serviceLogQuery.setUsername(username);
            serviceLogQuery.setTypeId(typeId);
            serviceLogQuery.setServiceEncode(serviceEncode);
            serviceLogQuery.setChannelEncode(channelEncode);

            result = serviceLogService.findAll(page, size, serviceLogQuery);
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="业务日志导出")
    @RequestMapping(value = "/export", method = {RequestMethod.GET})
    @ApiOperation(value = "导出业务日志报表", notes = "导出业务日志报表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "客户名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "服务类型ID(1:身份认证服务 2:电子认证服务)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceEncode", value = "服务编码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelEncode", value = "渠道编码", required = false, dataType = "String")
    })
    public void serviceLog(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(required = false) String beginDate,
                                     @RequestParam(required = false) String endDate,
                                     @RequestParam(required = false) String username,
                                     @RequestParam(required = false) String typeId,
                                     @RequestParam(required = false) String serviceEncode,
                                     @RequestParam(required = false) String channelEncode)
            throws Exception {
        Result<Object> result = new Result<>();
        ServiceLogQuery serviceLogQuery = new ServiceLogQuery();

        if (beginDate!= null && !beginDate.equals("")) {
            serviceLogQuery.setBeginDate(new Date(Long.parseLong(beginDate)));
        }

        if (endDate != null && !endDate.equals("")) {
            serviceLogQuery.setEndDate(new Date(Long.parseLong(endDate)));
        }

        serviceLogQuery.setUsername(username);
        serviceLogQuery.setTypeId(typeId);
        serviceLogQuery.setServiceEncode(serviceEncode);
        serviceLogQuery.setChannelEncode(channelEncode);

        try {
            List<ServiceLog> list = serviceLogMapper.findAll(serviceLogQuery);
            ArrayList<ServiceLogReport> data = new ArrayList<>();
            Integer i=1;

            for(ServiceLog serviceLog : list){

                ServiceLogReport serviceLogReport = new ServiceLogReport();
                serviceLogReport.setExxxNO(String.valueOf(i++));
                serviceLogReport.setExxxTime(serviceLog.getOperationtime());
                serviceLogReport.setCustomerName(serviceLog.getUsername());

                if (serviceLog.getServiceType() != null) {
                    serviceLogReport.setServiceType(serviceLog.getServiceType().getTypeName());
                }
                if (serviceLog.getServiceInfo() != null) {
                    serviceLogReport.setServiceName(serviceLog.getServiceInfo().getServiceName());
                }
                if (serviceLog.getChannelInfo() != null) {
                    serviceLogReport.setChannelName(serviceLog.getChannelInfo().getChannelName());
                }

                data.add(serviceLogReport);
            }

            String fileName = "业务日志" + "-" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename= \"" + fileName + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel");

            EasyExcel.write(response.getOutputStream(), ServiceLogReport.class).sheet("业务日志").doWrite(data);

            result.setCode(0);
            result.setMessage("导出成功");

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }

    }


}
