package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.dao.ICtidLogMapper;
import com.ahdms.ctidservice.service.ICtidLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-12 14:43
 */
@Service
public class CtidLogServiceImpl implements ICtidLogService {

    @Autowired
    private ICtidLogMapper ctidLogMapper;

}
