package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.bean.model.CtidInfos;
import com.ahdms.ctidservice.dao.ICtidInfosMapper;
import com.ahdms.ctidservice.service.ICtidInfosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2021-01-13 14:30
 */
@Service
public class CtidInfosServiceImpl implements ICtidInfosService {

    @Autowired
    private ICtidInfosMapper ctidInfosMapper;

    @Override
    public CtidInfos selectById(String ctidIndex) {
        return ctidInfosMapper.selectById(ctidIndex);
    }
}
