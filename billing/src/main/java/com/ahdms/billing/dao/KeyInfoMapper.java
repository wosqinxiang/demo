package com.ahdms.billing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ahdms.billing.model.KeyInfo;

public interface KeyInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(KeyInfo record);

    int insertSelective(KeyInfo record);

    KeyInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(KeyInfo record);

    int updateByPrimaryKey(KeyInfo record);
    
    KeyInfo selectByAppId(String appId);

	KeyInfo selectByAppIdAndAppPackage(@Param(value = "appId")String appId, @Param(value = "appPackage")String appPackage);

	List<KeyInfo> selectAll();
}