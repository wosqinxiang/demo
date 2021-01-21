package com.ahdms.billing.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ahdms.billing.model.ServiceInfo;

@Mapper
public interface ServiceInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(ServiceInfo record);

    int insertSelective(ServiceInfo record);

    ServiceInfo selectByPrimaryKey(String id);

    List<ServiceInfo> selectByServiceCode(@Param("service_encode")String service_encode);
    ServiceInfo queryByServiceName(@Param("serviceName")String serviceName);

    ServiceInfo queryByServiceEncode(@Param("serviceEncode")String serviceEncode);

    int updateByPrimaryKeySelective(ServiceInfo record);

    int updateByPrimaryKey(ServiceInfo record);

    List<ServiceInfo> selectLikeServiceName(@Param("serviceType")String serviceType,@Param("serviceName") String serviceName);

    List<ServiceInfo> selectLikeServiceEncode(@Param("serviceType")String serviceType,@Param("serviceEncode") String serviceEncode);

    List<ServiceInfo> findAll();

    List<ServiceInfo>  selectByServiceType();

    List<ServiceInfo> selectServiceByType(@Param("service_type")String service_type);

//	List<ServiceInfo> findByTypeId(@Param("serviceType")String typeId);
    
}