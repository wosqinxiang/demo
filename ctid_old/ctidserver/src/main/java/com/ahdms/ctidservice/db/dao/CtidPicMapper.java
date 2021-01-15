package com.ahdms.ctidservice.db.dao;

import com.ahdms.ctidservice.db.model.CtidPic;

public interface CtidPicMapper {

    int deleteByPrimaryKey(String id);

    int insert(CtidPic record);

    int insertSelective(CtidPic record);

    CtidPic selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CtidPic record);

    int updateByPrimaryKey(CtidPic record);
    
    CtidPic selectByCtidHash(String ctidHash);
}