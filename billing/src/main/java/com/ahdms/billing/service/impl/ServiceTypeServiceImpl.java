package com.ahdms.billing.service.impl;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ServiceTypeMapper;
import com.ahdms.billing.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {

    @Autowired
    ServiceTypeMapper serviceTypeMapper;

    @Override
    public Result<Object> findAll() {
        Result<Object> result = new Result<>();
        List list = serviceTypeMapper.findAll();
        result.setData(list);
        return result;
    }
}
