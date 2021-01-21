package com.ahdms.billing.dao;

import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.model.ManageLogQuery;

import java.util.List;

public interface ManageLogMapper {

    int deleteByPrimaryKey(String id);

    int insert(ManageLog record);

    int insertSelective(ManageLog record);

    ManageLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ManageLog record);

    int updateByPrimaryKey(ManageLog record);

    List<ManageLog> findAll(ManageLogQuery manageLogQuery);
}