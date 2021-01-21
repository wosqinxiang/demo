package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.ServiceLogQuery;

import java.util.Date;

public interface ServiceLogService {

    Result<Object> addServiceLog(ServiceLog serviceLog);

    Result<Object> findAll(int page, int size, ServiceLogQuery serviceLogQuery);
}
