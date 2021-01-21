package com.ahdms.billing.dao;

import com.ahdms.billing.model.ServiceType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ServiceTypeMapper {

    ServiceType queryServiceTypeById(String id);

    List<ServiceType> findAll();

}
