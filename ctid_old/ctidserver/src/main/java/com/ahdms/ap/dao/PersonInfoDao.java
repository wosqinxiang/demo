package com.ahdms.ap.dao;

import com.ahdms.ap.model.PersonInfo;

import java.util.List;

public interface PersonInfoDao {
    int deleteByPrimaryKey(String id);

    int insert(PersonInfo record);

    int insertSelective(PersonInfo record);

    PersonInfo selectByPrimaryKey(String id);

    List<PersonInfo> selectByOpenId(String openid);

    int updateByPrimaryKeySelective(PersonInfo record);

    int updateByPrimaryKeyWithBLOBs(PersonInfo record);

    int updateByPrimaryKey(PersonInfo record);

	PersonInfo selectByIdcard(String iDcard);

	PersonInfo selectByOpenID(String openID);
}