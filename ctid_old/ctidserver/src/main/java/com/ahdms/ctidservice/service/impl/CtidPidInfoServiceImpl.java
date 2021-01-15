package com.ahdms.ctidservice.service.impl;

import com.ahdms.ctidservice.common.UUIDGenerator;
import com.ahdms.ctidservice.db.dao.CtidPidInfosMapper;
import com.ahdms.ctidservice.db.model.CtidPidInfos;
import com.ahdms.ctidservice.service.CtidPidInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinxiang
 * @date 2020-12-02 13:37
 */
@Service
public class CtidPidInfoServiceImpl implements CtidPidInfoService {

    @Autowired
    private CtidPidInfosMapper ctidPidInfosMapper;

    @Override
    public void insertOrUpdate(String userInfosId, String pid, String speciallineCode) {
        CtidPidInfos ctidPidInfos = ctidPidInfosMapper.selectByUserIdAndPid(userInfosId,pid);
        if(ctidPidInfos == null){
            ctidPidInfos = new CtidPidInfos(UUIDGenerator.getUUID(),userInfosId,pid,speciallineCode);
            ctidPidInfosMapper.insertSelective(ctidPidInfos);
        }else{
            ctidPidInfos.setSpeciallineCode(speciallineCode);
            ctidPidInfosMapper.updateByPrimaryKeySelective(ctidPidInfos);
        }
    }

    @Override
    public CtidPidInfos selectByPid(String pid) {
        return selectByPidAndCode(pid,null);
    }

    @Override
    public CtidPidInfos selectByPidAndCode(String pid, String speciallineCode) {
        return ctidPidInfosMapper.queryByPidAndSpeciallineCode(pid,speciallineCode);
    }

    @Override
    public CtidPidInfos selectByCtidInfosIdAndPid(String ctidInfosId, String pid) {
        return ctidPidInfosMapper.selectByUserIdAndPid(ctidInfosId,pid);
    }
}
