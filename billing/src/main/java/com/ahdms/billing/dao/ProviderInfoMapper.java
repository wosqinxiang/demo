package com.ahdms.billing.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ahdms.billing.model.ProviderInfo;

public interface ProviderInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(ProviderInfo record);

    int insertSelective(ProviderInfo record);

    ProviderInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProviderInfo record);

    int updateByPrimaryKey(ProviderInfo record);

	List<ProviderInfo> queryProviderInfoPageList(Map map);

	ProviderInfo queryByName(String providerName);

	List<ProviderInfo> getProvider();

	List<ProviderInfo> getProviderByServiceId(@Param("serviceId")String serviceId);
}