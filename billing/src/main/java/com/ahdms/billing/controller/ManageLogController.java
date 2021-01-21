package com.ahdms.billing.controller;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ManageLogMapper;
import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.model.ManageLogQuery;
import com.ahdms.billing.model.report.ManageLogReport;
import com.ahdms.billing.service.ManageLogService;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(value = "/api/manage")
public class ManageLogController {

    private static final Logger logger = LoggerFactory.getLogger(ManageLogController.class);

    @Autowired
    private ManageLogService manageLogService;

    @Autowired
    private ManageLogMapper manageLogMapper;

//    @RequestMapping(value = "/addManageLog", method = {RequestMethod.POST})
//    @ApiOperation(value = "新增管理日志", notes = "新增管理日志")
//    @ApiImplicitParams({
//            @ApiImplicitParam(paramType = "query", name = "operator", value = "操作员", required = true, dataType = "String"),
//            @ApiImplicitParam(paramType = "query", name = "content", value = "操作内容", required = true, dataType = "String")})
    public Result<Object> addManageLog(HttpServletRequest request,
                                     @RequestParam(required = true) String operator,
                                     @RequestParam(required = true) String content)
            throws Exception {
        Result<Object> result = new Result<>();
        logger.debug(
                "operator=" + operator + ",content=" + content);
        ManageLog manageLog = new ManageLog();
        manageLog.setId(UUIDGenerator.getUUID());
        manageLog.setOperator(operator);
        manageLog.setComment(content);
        manageLog.setOperationtime(new Date());
        try {
            result = manageLogService.addManageLog(manageLog);
            result.setCode(0);
            result.setData(manageLog);
            result.setMessage("新增成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/manageLogList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询管理日志列表", notes = "查询管理日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页页码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页大小", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "operator", value = "操作员", required = false, dataType = "String"),})
    public Result<Object> manageLogList(HttpServletRequest request,
                                        @RequestParam(required = true) int page,
                                        @RequestParam(required = true) int size,
                                        @RequestParam(required = false) String beginDate,
                                        @RequestParam(required = false) String endDate,
                                        @RequestParam(required = false) String operator)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            ManageLogQuery manageLogQuery = new ManageLogQuery();

            if (beginDate!= null && !beginDate.equals("")) {
                manageLogQuery.setBeginDate(new Date(Long.parseLong(beginDate)));
            }

            if (endDate != null && !endDate.equals("")) {
                manageLogQuery.setEndDate(new Date(Long.parseLong(endDate)));
            }

            manageLogQuery.setOperator(operator);

            result = manageLogService.findAll(page, size, manageLogQuery);
            result.setCode(0);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @SysLog(comment="管理日志导出")
    @RequestMapping(value = "/manageLogExport", method = {RequestMethod.GET})
    @ApiOperation(value = "管理日志导出", notes = "管理日志导出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期(格式：时间戳毫秒)", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "operator", value = "操作员", required = false, dataType = "String"),})
    public Result<Object> manageLogList(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) String beginDate,
                                        @RequestParam(required = false) String endDate,
                                        @RequestParam(required = false) String operator)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            ManageLogQuery manageLogQuery = new ManageLogQuery();

            if (beginDate!= null && !beginDate.equals("")) {
                manageLogQuery.setBeginDate(new Date(Long.parseLong(beginDate)));
            }

            if (endDate != null && !endDate.equals("")) {
                manageLogQuery.setEndDate(new Date(Long.parseLong(endDate)));
            }

            manageLogQuery.setOperator(operator);

            List<ManageLog> list = manageLogMapper.findAll(manageLogQuery);

            ArrayList<ManageLogReport> data = new ArrayList<>();
            Integer i=1;

            for(ManageLog manageLog : list){
                ManageLogReport manageLogReport = new ManageLogReport();
                manageLogReport.setExxxNO(String.valueOf(i++));
                manageLogReport.setExxxTime(manageLog.getOperationtime());
                manageLogReport.setOperator(manageLog.getOperator());
                manageLogReport.setContent(manageLog.getComment());
                data.add(manageLogReport);
            }


            String fileName = "管理日志" + "-" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename= \"" + fileName + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel");

            EasyExcel.write(response.getOutputStream(), ManageLogReport.class).sheet("模板").doWrite(data);

            result.setCode(0);
            result.setMessage("导出成功");


        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

}
