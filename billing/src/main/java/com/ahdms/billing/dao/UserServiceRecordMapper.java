package com.ahdms.billing.dao;

import com.ahdms.billing.model.UserServiceRecord;
import com.ahdms.billing.vo.UserServiceRecordVO;

import java.util.List;

public interface UserServiceRecordMapper {

    int deleteByPrimaryKey(String id);

    int insert(UserServiceRecord record);

    int insertSelective(UserServiceRecord record);

    UserServiceRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserServiceRecord record);

    int updateByPrimaryKey(UserServiceRecord record);

    List<UserServiceRecordVO> selectByUserId(String userId);

    List<UserServiceRecord> queryServiceHistoryRecord(String userId);
}