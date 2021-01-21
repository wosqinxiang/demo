package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.service.ServiceInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;


@Service
public class ServiceInfoServiceImpl implements ServiceInfoService {

    private final static Logger logger = LoggerFactory.getLogger(ServiceInfoServiceImpl.class);

    @Autowired
    ServiceInfoMapper serviceInfoMapper;


    @Override
    public Result<Object> addService(ServiceInfo serviceInfo) {
        Result<Object> result = new Result<>();
        try {
            serviceInfoMapper.insert(serviceInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<Object> deleteServiceById(String id) {
        Result<Object> result = new Result<>();
        serviceInfoMapper.deleteByPrimaryKey(id);
        return result;
    }

    @Override
    public Result<ServiceInfo> updateService(ServiceInfo serviceInfo) {
        Result<ServiceInfo> result = new Result<>();
        try {
            serviceInfoMapper.updateByPrimaryKey(serviceInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<ServiceInfo> queryServiceById(String id) {
        Result<ServiceInfo> result = new Result<>();
        ServiceInfo serviceInfo = serviceInfoMapper.selectByPrimaryKey(id);
        result.setData(serviceInfo);
        return result;
    }

    @Override
    public Result<Object> selectByServiceCode(String code) {
        Result<Object> result = new Result<>();
        List<ServiceInfo> list = serviceInfoMapper.selectByServiceCode(code);
        result.setData(list);
        return result;
    }

    public Result<Object> queryLikeServiceName(int page, int size, String serviceType, String serviceName){
        GridModel<ServiceInfo> gridModel = new GridModel<ServiceInfo>();
        PageHelper.startPage(page, size);
        List<ServiceInfo> list = serviceInfoMapper.selectLikeServiceName(serviceType,serviceName);
        PageInfo<ServiceInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<Object> selectByServiceType() {
        List<ServiceInfo> list = serviceInfoMapper.selectByServiceType();
        return Result.success(list);
    }

    @Override
    public Result<Object> queryLikeServiceEncode(int page, int size, String serviceType, String serviceEncode){
        GridModel<ServiceInfo> gridModel = new GridModel<ServiceInfo>();
        PageHelper.startPage(page, size);
        List<ServiceInfo> list = serviceInfoMapper.selectLikeServiceEncode(serviceType, serviceEncode);
        PageInfo<ServiceInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<Object> findAll(int page, int size) {
        GridModel<ServiceInfo> gridModel = new GridModel<ServiceInfo>();
        PageHelper.startPage(page, size);
        List<ServiceInfo> list = serviceInfoMapper.findAll();
        PageInfo<ServiceInfo> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<ServiceInfo> queryByServiceName(String serviceName) {
        Result<ServiceInfo> result = new Result<>();
        ServiceInfo serviceInfo = serviceInfoMapper.queryByServiceName(serviceName);
        result.setData(serviceInfo);
        return result;
    }

    @Override
    public Result<ServiceInfo> queryByServiceEncode(String serviceEncode) {
        Result<ServiceInfo> result = new Result<>();
        ServiceInfo serviceInfo = serviceInfoMapper.queryByServiceEncode(serviceEncode);
        result.setData(serviceInfo);
        return result;
    }
}
