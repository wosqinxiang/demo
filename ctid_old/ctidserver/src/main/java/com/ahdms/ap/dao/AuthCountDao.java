package com.ahdms.ap.dao;

import com.ahdms.ap.model.AuthCount; 

public interface AuthCountDao {
    int deleteByPrimaryKey(String id);

    int insert(AuthCount record);

    int insertSelective(AuthCount record);

    AuthCount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuthCount record);

    int updateByPrimaryKey(AuthCount record);

	AuthCount selectByServer(String name); 
}