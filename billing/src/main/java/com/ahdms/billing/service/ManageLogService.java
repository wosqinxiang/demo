package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ManageLog;
import com.ahdms.billing.model.ManageLogQuery;

public interface ManageLogService {

    Result<Object> addManageLog(ManageLog manageLog);

    Result<Object> findAll(int page, int size, ManageLogQuery manageLogQuery);
}
