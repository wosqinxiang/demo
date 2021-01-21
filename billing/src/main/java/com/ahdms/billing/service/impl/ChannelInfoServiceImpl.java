package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.model.ChannelInfo;
import com.ahdms.billing.service.ChannelInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class ChannelInfoServiceImpl implements ChannelInfoService {

    private final static Logger logger = LoggerFactory.getLogger(ChannelInfoServiceImpl.class);

    @Autowired
    ChannelInfoMapper channelInfoMapper;

    @Override
    public Result<Object> addChannel(ChannelInfo channelInfo) {
        Result<Object> result = new Result<>();
        try {
            channelInfoMapper.insert(channelInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<ChannelInfo> getChannelById(String id) {
        Result<ChannelInfo> result = new Result<>();
        ChannelInfo channelInfo = channelInfoMapper.selectByPrimaryKey(id);
        result.setData(channelInfo);
        return result;
    }

    @Override
    public Result<ChannelInfo> updateChannel(ChannelInfo channelInfo) {
        Result<ChannelInfo> result = new Result<>();
        try {
            channelInfoMapper.updateByPrimaryKey(channelInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<ChannelInfo> deleteChannelById(String id) {
        Result<ChannelInfo> result = new Result<>();
        channelInfoMapper.deleteByPrimaryKey(id);
        return result;
    }

    @Override
    public Result<Object> selectLikeChannelName(int page, int size, String channelName) {
        GridModel<ChannelInfo> gridModel = new GridModel<ChannelInfo>();
        PageHelper.startPage(page, size);
        List<ChannelInfo> list = channelInfoMapper.selectLikeChannelName(channelName);
        PageInfo<ChannelInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<Object> selectLikeChannelEncode(int page, int size, String channelEncode) {
        GridModel<ChannelInfo> gridModel = new GridModel<ChannelInfo>();
        PageHelper.startPage(page, size);
        List<ChannelInfo> list = channelInfoMapper.selectLikeChannelEncode(channelEncode);
        PageInfo<ChannelInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<Object> findAll(int page, int size) {
        GridModel<ChannelInfo> gridModel = new GridModel<ChannelInfo>();
        PageHelper.startPage(page, size);
        List<ChannelInfo> list = channelInfoMapper.findAll();
        PageInfo<ChannelInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<ChannelInfo> selectByChannelName(String channelName) {
        Result<ChannelInfo> result = new Result<>();
        ChannelInfo channelInfo = channelInfoMapper.queryByChannelName(channelName);
        result.setData(channelInfo);
        return result;
    }

    @Override
    public Result<ChannelInfo> selectByChannelEncode(String channelEncode) {
        Result<ChannelInfo> result = new Result<>();
        ChannelInfo channelInfo = channelInfoMapper.queryByChannelEncode(channelEncode);
        result.setData(channelInfo);
        return result;
    }
}
