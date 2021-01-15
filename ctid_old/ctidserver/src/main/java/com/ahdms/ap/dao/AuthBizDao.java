package com.ahdms.ap.dao;

import java.util.Date;
import java.util.List;

import com.ahdms.ap.model.AuthBiz;

public interface AuthBizDao {
    int deleteByPrimaryKey(String id);

    int insert(AuthBiz record);

    int insertSelective(AuthBiz record);

    AuthBiz selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuthBiz record);

    int updateByPrimaryKey(AuthBiz record);

	AuthBiz queryBySerial(String serialNum);

	List<AuthBiz> queryOverDue(Date d);
}