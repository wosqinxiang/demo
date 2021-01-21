package com.ahdms.billing.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.service.ChannelInfoService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value = "/api/channel")
public class ChannelController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Autowired
    private ChannelInfoService channelInfoService;

    @Autowired
    private ChannelInfoMapper channelInfoMapper;

    @RequestMapping(value = "/addChannel", method = {RequestMethod.POST})
    @ApiOperation(value = "新增渠道", notes = "新增渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "channelName", value = "渠道名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelCode", value = "渠道编码", required = true, dataType = "String")})
    @SysLog(comment="新增渠道")
    public Result<Object> addChannel(HttpServletRequest request,
                                  @RequestParam(required = true) String channelName,
                                  @RequestParam(required = true) String channelCode)
            throws Exception {
        Result<Object> result = new Result<>();
        logger.debug(
                "channelName=" + channelName + ",channelCode=" + channelCode);

        ChannelInfo channelNameInfo = channelInfoService.selectByChannelName(channelName).getData();
        if (channelNameInfo != null) {
            result.setCode(201);
            result.setMessage("该名称已存在");
            return  result;
        }

        ChannelInfo channelEncodeInfo = channelInfoService.selectByChannelEncode(channelCode).getData();
        if (channelEncodeInfo != null) {
            result.setCode(201);
            result.setMessage("该编码已存在");
            return  result;
        }

        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setId(UUIDGenerator.getUUID());
        channelInfo.setChannelName(channelName);
        channelInfo.setChannelCode(channelCode);
        try {
            result = channelInfoService.addChannel(channelInfo);
            result.setCode(0);
            result.setData(channelInfo);
            result.setMessage("新增成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="删除渠道")
    @RequestMapping(value = "/deleteChannelById", method = {RequestMethod.POST})
    @ApiOperation(value = "删除渠道", notes = "删除渠道")
    @ApiImplicitParam(paramType = "query", name = "channelId", value = "渠道Id", required = true, dataType = "String")
    public Result<ChannelInfo>delete(HttpServletRequest request,
                                     @RequestParam(required = true) String channelId)
            throws Exception {
        Result<ChannelInfo> result = new Result<>();
        try {
            ChannelInfo channelInfo = channelInfoService.getChannelById(channelId).getData();
            channelInfoService.deleteChannelById(channelId);
            result.setCode(0);
            result.setData(channelInfo);
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="修改渠道")
    @RequestMapping(value = "/updateChannel", method = {RequestMethod.POST})
    @ApiOperation(value = "修改渠道", notes = "修改渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "channelId", value = "渠道ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelName", value = "渠道名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelCode", value = "渠道编码", required = true, dataType = "String")})
    public Result<ChannelInfo> update(HttpServletRequest request,
                                      @RequestParam(required = true) String channelId,
                                      @RequestParam(required = true) String channelName,
                                      @RequestParam(required = true) String channelCode)
            throws Exception {
        Result<ChannelInfo> result = new Result<>();
        logger.debug("channelId=" + channelId +",channelName=" + channelName + ",channelCode=" + channelCode);
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.setId(channelId);
        channelInfo.setChannelName(channelName);
        channelInfo.setChannelCode(channelCode);
        try {
            result = channelInfoService.updateChannel(channelInfo);
            result.setCode(0);
            result.setData(channelInfo);
            result.setMessage("修改成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryChannelById", method = {RequestMethod.POST})
    @ApiOperation(value = "查询渠道", notes = "查询渠道")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "channelId", value = "渠道id", required = true, dataType = "String")})
    public Result<ChannelInfo> queryChannelById(HttpServletRequest request,
                                             @RequestParam(required = true) String channelId)throws Exception{
        Result<ChannelInfo> result = new Result<>();
        try {
            result = channelInfoService.getChannelById(channelId);
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/channelList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询渠道列表(包含搜索)", notes = "查询渠道列表(包含搜索)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页页码", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页大小", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "searchType", value = "搜索类型 1:根据渠道名称搜索 2:根据渠道编码搜索", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelName", value = "渠道名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "channelEncode", value = "渠道编码", required = false, dataType = "String")})
    public Result<Object> findAll(HttpServletRequest request,
                                  @RequestParam(required = false,defaultValue="0") int page,
                                  @RequestParam(required = false,defaultValue="10") int size,
                                  @RequestParam(required = true) String searchType,
                                  @RequestParam(required = false) String channelName,
                                  @RequestParam(required = false) String channelEncode)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            if (searchType.equals("1")) {
                result = channelInfoService.selectLikeChannelName(page, size, channelName);
                result.setCode(0);
                result.setMessage("查询成功");
            } else if (searchType.equals("2")){
                result = channelInfoService.selectLikeChannelEncode(page, size, channelEncode);
                result.setCode(0);
                result.setMessage("查询成功");
            } else {
                result.setCode(0);
                result.setMessage("传入的searchType有误，搜索类型 1:根据渠道名称搜索 2:根据渠道编码搜索");
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryChannelNameList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询渠道名称", notes = "查询渠道名称")
    public Result<Object> queryServiceNameList(HttpServletRequest request)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            List<String> channelNameList = new ArrayList<>();
            List<ChannelInfo> list = channelInfoMapper.findAll();
//            for (ChannelInfo channelInfo : list) {
//                channelNameList.add(channelInfo.getChannelName());
//            }
//
//            //数组去重处理
//            LinkedHashSet<String> hashSet = new LinkedHashSet<>(channelNameList);
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

}
