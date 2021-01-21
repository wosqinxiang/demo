package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.UserServiceRecord;
import com.ahdms.billing.vo.UserServiceRecordVO;

import java.util.List;

public interface UserServiceRecordService {

    int addUserServiceRecord(UserServiceRecord userServiceRecord);

    Result<List<UserServiceRecordVO>> selectByUserId(String userId);

    Result<List<UserServiceRecord>> queryServiceHistoryRecord(String userId);


}
