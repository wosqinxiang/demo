package com.ahdms.billing.dao;

import com.ahdms.billing.model.Authorities;

public interface AuthoritiesMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Authorities record);

    int insertSelective(Authorities record);

    Authorities selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Authorities record);

    int updateByPrimaryKey(Authorities record);
}