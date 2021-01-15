package com.ahdms.ctidservice.db.dao;

import com.ahdms.ctidservice.db.model.CtidLog;

public interface CtidLogMapper {

    int deleteByPrimaryKey(String id);

    int insert(CtidLog record);

    int insertSelective(CtidLog record);

    CtidLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CtidLog record);

    int updateByPrimaryKey(CtidLog record);
}