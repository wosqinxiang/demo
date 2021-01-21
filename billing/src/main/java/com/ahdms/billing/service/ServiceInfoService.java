package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ServiceInfo;

import java.util.List;

public interface ServiceInfoService {

    Result<Object> addService(ServiceInfo serviceInfo);

    Result<ServiceInfo> queryServiceById(String id);

    Result<ServiceInfo> queryByServiceName(String serviceName);

    Result<ServiceInfo> queryByServiceEncode(String serviceEncode);

    Result<ServiceInfo> updateService(ServiceInfo serviceInfo);

    Result<Object> deleteServiceById(String id);

    Result<Object> selectByServiceCode(String code);

    Result<Object> queryLikeServiceName(int page, int size, String serviceType, String serviceName);

    Result<Object> selectByServiceType();

    Result<Object> queryLikeServiceEncode(int page, int size, String serviceType, String serviceEncode);

    Result<Object> findAll(int page, int size);

}
