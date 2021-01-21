package com.ahdms.billing.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.model.*;
import com.ahdms.billing.model.report.ServiceLogForUserServiceData;
import com.ahdms.billing.service.ServiceTypeService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ReportMapper;
import com.ahdms.billing.model.report.DataJSON;
import com.ahdms.billing.model.report.ResultData;
import com.ahdms.billing.utils.DateUtils;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api/report")
public class DataReportController {
    private static final Logger logger = LoggerFactory.getLogger(DataReportController.class);

    @Autowired
    private ReportMapper reportMapper;

    @SysLog(comment="导出报表-按用户导出")
    @RequestMapping(value = "/export", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "导出报表", notes = "导出报表-按用户导出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间(yyyy-MM-dd)", required = true, dataType = "String")})
    public Result exportForUser(HttpServletResponse response, @RequestParam String date)
            throws Exception {
        //生成表头
        Report report = new Report();
        report.setDate(date);
        report.setType(3);
        Report result = reportMapper.selectByReport(report);
        if(result == null){
        	return Result.error("无数据");
        }
//        logger.error("统计报表JSON：{}", JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
        ResultData resultData = JSON.parseObject(result.getContent(), ResultData.class);
        try {
            String fileName = "用户服务统计" + "-" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename= \"" + fileName + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel");
            ExcelWriter excelWriter = EasyExcelFactory.getWriter(response.getOutputStream());
            // 表单
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName("第一个Sheet");
            // 创建一个表格
            Table table = new Table(1);
            // 动态添加 表头 headList --> 所有表头行集合
            List<List<String>> headList = new ArrayList<>();
            List<String> headTitle = new ArrayList<>();
            headTitle.add(date + "客户服务报表");//最顶部标题
            headTitle.add("客户名称");
            headList.add(headTitle);
            for (ServiceType type : resultData.getTableTitle()) {
                for (int i = 0; i < type.getServiceInfos().size(); i++) {
                    List<String> headTitle_type = new ArrayList<>();
                    headTitle_type.add(date + "客户服务报表");//最顶部标题
                    headTitle_type.add(type.getTypeName());
                    headTitle_type.add(type.getServiceInfos().get(i).getServiceName());
                    headList.add(headTitle_type);
                }
                List<String> headTitle_sum = new ArrayList<>();
                headTitle_sum.add(date + "客户服务报表");//最顶部标题
                headTitle_sum.add(type.getTypeName());
                headTitle_sum.add("合计");
                headList.add(headTitle_sum);
            }
            table.setHead(headList);

            List<List<Object>> list = new ArrayList<>();
            for (List<DataJSON> datas : resultData.getTableContent()) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    row.add(datas.get(i).getContent());
                }
                list.add(row);
            }
            
            excelWriter.write1(list, sheet, table);
            // 记得 释放资源
            excelWriter.finish();
            return null;
        } catch (Exception e) {
            logger.error("报错了：{}", e);
        }
        return Result.error("导出失败！");
    }
    
    @SysLog(comment="导出报表-按渠道导出")
    @RequestMapping(value = "/exportForService", method = {RequestMethod.POST,RequestMethod.GET})
    @ApiOperation(value = "导出报表", notes = "导出报表-按渠道导出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间(yyyy-MM-dd)", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true, dataType = "String")})
    public Result exportForService(HttpServletResponse response, @RequestParam String date, @RequestParam(required = false) String username)
            throws Exception {
        //生成表头
        Report report = new Report();
        report.setDate(date);
        report.setType(4);
        report.setUsername(username);
        Report result = reportMapper.selectByReport(report);
        if(result == null){
        	return Result.error("无数据");
        }
        
//        logger.error("统计报表JSON：{}", JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
        ResultData resultData = JSON.parseObject(result.getContent(), ResultData.class);
        try {
            String fileName = "渠道服务统计" + "-" + DateUtils.format(new Date(), "yyyyMMddHHmmss");
            fileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename= \"" + fileName + ".xlsx" + "\"");
            response.setContentType("application/vnd.ms-excel");
            ExcelWriter excelWriter = EasyExcelFactory.getWriter(response.getOutputStream());
            // 表单
            Sheet sheet = new Sheet(1, 0);
            sheet.setSheetName("第一个Sheet");
            // 创建一个表格
            Table table = new Table(1);
            // 动态添加 表头 headList --> 所有表头行集合
            List<List<String>> headList = new ArrayList<>();
            ArrayList<DataJSON> dataContent = new ArrayList<>();
            List<String> headTitle = new ArrayList<>();
            headTitle.add(date + "客户服务报表");//最顶部标题
            headTitle.add("渠道名称");
            headList.add(headTitle);
            DataJSON dataJSON = new DataJSON();
            dataJSON.setName("渠道名称");
            dataContent.add(dataJSON);
            for (ServiceType type : resultData.getTableTitle()) {
                for (int i = 0; i < type.getServiceInfos().size(); i++) {
                    List<String> headTitle_type = new ArrayList<>();
                    headTitle_type.add(date + "客户服务报表");//最顶部标题
                    headTitle_type.add(type.getTypeName());
                    headTitle_type.add(type.getServiceInfos().get(i).getServiceName());
                    headList.add(headTitle_type);
                }
                List<String> headTitle_sum = new ArrayList<>();
                headTitle_sum.add(date + "客户服务报表");//最顶部标题
                headTitle_sum.add(type.getTypeName());
                headTitle_sum.add("合计");
                headList.add(headTitle_sum);
            }
            table.setHead(headList);
            List<List<Object>> list = new ArrayList<>();
            for (List<DataJSON> datas : resultData.getTableContent()) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    row.add(datas.get(i).getContent());
                }
                list.add(row);
            }
            excelWriter.write1(list, sheet, table);
            // 记得 释放资源
            excelWriter.finish();
            return null;
        } catch (Exception e) {
            logger.error("报错了：{}", e);
        }
        return Result.error("导出失败！");
//        logger.error("统计报表JSON：{}", JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));

    }

    @RequestMapping(value = "/queryExportData", method = {RequestMethod.POST})
    @ApiOperation(value = "查询报表数据", notes = "查询报表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间：YYYY-MM-DD or YYYY-MM", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "exportType", value = "类型：3 用户服务统计 4 渠道统计", required = true, dataType = "String")})
    public Result<ResultData> queryExportData(HttpServletResponse response, @RequestParam String date, @RequestParam(required = true) int exportType)
            throws Exception {
        Report report = new Report();
        report.setDate(date);
        report.setType(exportType);
        Report result = reportMapper.selectByReport(report);
        if(result == null){
        	return Result.error("无数据");
        }
        ResultData resultData = JSON.parseObject(result.getContent(), ResultData.class);
        resultData.setDate(date);
        resultData.setType(""+exportType);
        return Result.success(resultData);
    }

    @RequestMapping(value = "/queryExportforServiceType", method = {RequestMethod.POST})
    @ApiOperation(value = "查询报表数据", notes = "查询报表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间：YYYY-MM-DD or YYYY-MM", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "exportType", value = "类型：3 用户服务统计 4 渠道统计", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型", required = true, dataType = "String")})
    public Result<Object> queryExportForType(HttpServletResponse response, @RequestParam String date,
                                                 @RequestParam(required = true) int exportType,@RequestParam(required = true) String serviceType)
            throws Exception {
        Report report = new Report();
        report.setDate(date);
        report.setType(exportType);
        Report result = reportMapper.selectByReport(report);
        if(result == null){
        	return Result.error("无数据");
        }
        ResultData resultData = JSON.parseObject(result.getContent(), ResultData.class);
        List<ServiceType> serviceTypes=resultData.getTableTitle();
        List<DataJSON> list=new ArrayList<>();
        for (List<DataJSON> dataJSONS:resultData.getTableContent()) {
            int sum=0;
            DataJSON dataJSON=new DataJSON();
            for (int i=0;i<dataJSONS.size();i++){
                if(i==0){
                    dataJSONS.get(0);
                    dataJSON.setId(dataJSONS.get(i).getId());
                    dataJSON.setName(dataJSONS.get(i).getContent());
                    System.out.println(dataJSONS.get(i).getContent());
                }else{
                    for (ServiceType type:serviceTypes) {
                        if(type.getId().equals(serviceType)){
                            if(type.getServiceInfos()!=null){
                                for (ServiceInfo serviceInfo:type.getServiceInfos()) {
                                    if(serviceInfo.getServiceEncode().equals(dataJSONS.get(i).getId())){
                                        sum=sum+Integer.parseInt(dataJSONS.get(i).getContent());
                                    }
                                }
                            }

                        }
                    }
                }
            }
            dataJSON.setContent(""+sum);
            list.add(dataJSON);
        }
        return Result.success(list);
    }
    @RequestMapping(value = "/queryExportDetail", method = {RequestMethod.POST})
    @ApiOperation(value = "查询报表数据详情", notes = "查询报表数据详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间：YYYY-MM-DD or YYYY-MM", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "exportType", value = "类型：3 用户服务统计 4 渠道统计", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型", required = true, dataType = "String")})
    public Result<Object> queryExportForTypeByUser(HttpServletResponse response,@RequestParam(required = true) String id, @RequestParam(required = true) String date,
                                             @RequestParam(required = true) int exportType,@RequestParam(required = true) String serviceType)
            throws Exception {
        Report report = new Report();
        report.setDate(date);
        report.setType(exportType);
        Report result = reportMapper.selectByReport(report);
        ResultData resultData = JSON.parseObject(result.getContent(), ResultData.class);
        List<ServiceType> serviceTypes=resultData.getTableTitle();
        List<DataJSON> list=new ArrayList<>();
        for (List<DataJSON> dataJSONS:resultData.getTableContent()) {
            if(dataJSONS.get(0).getId().equals(id)){
                for (int i=0;i<dataJSONS.size();i++){
                    for (ServiceType type:serviceTypes) {
                        if(type.getId().equals(serviceType)){
                            if(type.getServiceInfos()!=null){
                                for (ServiceInfo serviceInfo:type.getServiceInfos()) {
                                    if(serviceInfo.getServiceEncode().equals(dataJSONS.get(i).getId())){
                                        DataJSON dataJSON=new DataJSON();
                                        dataJSON.setName(dataJSONS.get(i).getName());
                                        dataJSON.setContent(dataJSONS.get(i).getContent());
                                        list.add(dataJSON);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success(list);
    }


    @Autowired
    private ServiceInfoMapper serviceInfoMapper;

    @Autowired
    private ChannelInfoMapper channelInfoMapper;
    @Autowired
    private ServiceTypeService serviceTypeService;

    @Autowired
    private ServiceLogMapper serviceLogMapper;

    @RequestMapping(value = "/create", method = {RequestMethod.POST})
    @ApiOperation(value = "查询报表数据详情", notes = "查询报表数据详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "date", value = "时间：YYYY-MM-DD or YYYY-MM", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "reportType", value = "服务类型", required = true, dataType = "String")})
    public void createDataForServiceByUser(HttpServletResponse response,@RequestParam(required = true) String date,@RequestParam(required = true) String username,@RequestParam(required = true) int reportType) {
        try {
            String dayStr = null;
            String monthStr = null;
            if(0 == reportType){
                dayStr = date;
            }else{
                monthStr = date;
            }
            //生成表头
            Result<Object> objectResult = serviceTypeService.findAll();
            List<ServiceType> serviceType = (List<ServiceType>) objectResult.getData();
            List<List<ServiceInfo>> serviceInfos = new ArrayList<>();
            for (ServiceType type : serviceType) {
                List<ServiceInfo> serviceInfosByType = serviceInfoMapper.selectServiceByType(type.getId());
                serviceInfos.add(serviceInfosByType);
                type.setServiceInfos(serviceInfosByType);
            }
            List<ChannelInfo> channelInfos = channelInfoMapper.findAll();
            List<ServiceLogForUserServiceData> serviceLogForUserServiceData=serviceLogMapper.queryServiceLogByDateAndUser(dayStr,monthStr,username);
//            logger.error(JSON.toJSONString(serviceLogForUserServiceData, SerializerFeature.DisableCircularReferenceDetect));
            List<List<DataJSON>> dataJSONS = new ArrayList<>();//内容数据
            ArrayList<DataJSON> dataContent = new ArrayList<>();
            DataJSON dataJSON = new DataJSON();
            dataJSON.setName("渠道名称");
            dataContent.add(dataJSON);
            for (ServiceType type : serviceType) {
                for (int i = 0; i < serviceInfos.size(); i++) {
                    for (int j = 0; j < serviceInfos.get(i).size(); j++) {
                        if (serviceInfos.get(i).get(j).getServiceType().equals(type.getId())) {
                            DataJSON dataJSON_service = new DataJSON();
                            dataJSON_service.setName(serviceInfos.get(i).get(j).getServiceName());
                            dataJSON_service.setId(serviceInfos.get(i).get(j).getServiceEncode());
                            dataJSON_service.setContent("0");
                            dataContent.add(dataJSON_service);
                        }
                    }
                }
                DataJSON dataJSON_sum = new DataJSON();
                dataJSON_sum.setName("合计");
                dataJSON_sum.setContent("0");
                dataContent.add(dataJSON_sum);
            }
            ChannelInfo channel = new ChannelInfo();
            channel.setChannelName("合计");
            channelInfos.add(channel);
            //表内容
            List<Integer> serviceSums = new ArrayList<>();//服务合计数据
            for (int i = 0; i < dataContent.size() - 1; i++) {
                serviceSums.add(0);
            }
            for (ChannelInfo channelInfo : channelInfos) {
                ArrayList<DataJSON> dataContentForUser = new ArrayList<>();
                for (DataJSON d : dataContent) {
                    DataJSON da = new DataJSON();
                    da.setContent(d.getContent());
                    da.setId(d.getId());
                    da.setName(d.getName());
                    dataContentForUser.add(da);
                }
                int sum = 0;
                for (int i = 0; i < dataContentForUser.size(); i++) {
                    if (i == 0) {//第一列写入用户名称（固定列）
                        dataContentForUser.get(i).setContent(channelInfo.getChannelName());
                        dataContentForUser.get(i).setId(channelInfo.getChannelCode());
                    } else {
                        if (channelInfo.getId() == null) {//若id为null，则此行数据为合计数据
                            dataContentForUser.get(i).setContent("" + serviceSums.get(i - 1));
                        } else {
                            if (dataContentForUser.get(i).getId() != null) {
                                for (ServiceLogForUserServiceData slfusd : serviceLogForUserServiceData) {
                                    if (dataContentForUser.get(i).getId() != null) {
                                        if (dataContentForUser.get(i).getId().equals(slfusd.getServiceId())) {
                                            if (slfusd.getChannelEncode().equals(channelInfo.getChannelCode())) {
                                                dataContentForUser.get(i).setContent("" + slfusd.getCount());
                                                serviceSums.set(i - 1, serviceSums.get(i - 1) + slfusd.getCount());
                                                sum = sum + slfusd.getCount();
                                            }
                                        }
                                    }
                                }
                            } else {//只有合计列 id为空
                                dataContentForUser.get(i).setContent("" + sum);
                                serviceSums.set(i - 1, serviceSums.get(i - 1) + sum);
                                sum = 0;//合计一组结束 sum清零
                            }
                        }

                    }
                }
                dataJSONS.add(dataContentForUser);
            }
            List<List<Object>> list = new ArrayList<>();
            for (List<DataJSON> datas : dataJSONS) {
                List<Object> row = new ArrayList<>();
                for (int i = 0; i < datas.size(); i++) {
                    row.add(datas.get(i).getContent());
                }
                list.add(row);
            }

            ResultData resultData = new ResultData();
            resultData.setTableTitle(serviceType);
            resultData.setTableContent(dataJSONS);
            Report report = new Report();
            report.setId(UUIDGenerator.getUUID());
            report.setType(4);
            report.setDate(date);
            report.setUsername(username);
            report.setContent(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
//            logger.error(JSON.toJSONString(resultData, SerializerFeature.DisableCircularReferenceDetect));
            insertOrUpdate(report);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    private void insertOrUpdate(Report report){
        Report _report = reportMapper.selectByReport(report);
        if(_report != null){
            report.setId(_report.getId());
            reportMapper.updateByPrimaryKeySelective(report);
        }else{
            report.setId(UUIDGenerator.getUUID());
            reportMapper.insertSelective(report);
        }
    }
}
