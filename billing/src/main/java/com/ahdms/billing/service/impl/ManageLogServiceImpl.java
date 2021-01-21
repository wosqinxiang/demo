package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ManageLogMapper;
import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.model.ManageLogQuery;
import com.ahdms.billing.service.ManageLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class ManageLogServiceImpl implements ManageLogService {

    private final static Logger logger = LoggerFactory.getLogger(ManageLogServiceImpl.class);

    @Autowired
    private ManageLogMapper manageLogMapper;


    @Override
    public Result<Object> addManageLog(ManageLog manageLog) {
        Result<Object> result = new Result<>();
        try {
            manageLogMapper.insert(manageLog);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public Result<Object> findAll(int page, int size, ManageLogQuery manageLogQuery) {
        GridModel<ManageLog> gridModel = new GridModel<ManageLog>();
        PageHelper.startPage(page, size);
        List<ManageLog> list = manageLogMapper.findAll(manageLogQuery);
        PageInfo<ManageLog> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }
}
