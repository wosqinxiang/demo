package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.ServiceLogQuery;
import com.ahdms.billing.service.ServiceLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class ServiceLogServiceImpl implements ServiceLogService {

    private final static Logger logger = LoggerFactory.getLogger(ServiceLogServiceImpl.class);

    @Autowired
    private ServiceLogMapper serviceLogMapper;

    @Override
    public Result<Object> addServiceLog(ServiceLog serviceLog) {
        Result<Object> result = new Result<>();
        try {
            serviceLogMapper.insert(serviceLog);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<Object> findAll(int page, int size, ServiceLogQuery serviceLogQuery) {
        GridModel<ServiceLog> gridModel = new GridModel<ServiceLog>();
        PageHelper.startPage(page, size);
        List<ServiceLog> list = serviceLogMapper.findAll(serviceLogQuery);
        PageInfo<ServiceLog> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }
}
