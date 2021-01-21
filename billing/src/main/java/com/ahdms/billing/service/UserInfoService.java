package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserInfoForService;

import java.util.List;

public interface UserInfoService {
    Result<String> addUser(UserInfo userInfo,String whiteIp);

    Result<UserInfo> getUserByUserId(String userId);

    Result<UserInfo> updateUserInfoById(UserInfo userInfo,String whiteIp);

    Result<UserInfo> getUserByUsername(String username);

    Result<UserInfo> getUserByUsernameAndPassword(String username, String password);

    Result<List<UserInfo>> findAll();

    List<UserInfoForService> queryReportAll();

    Result<Object> selectLikeUserName(int page, int size, String userName);

    Result<UserInfo> resetUserById(String userId);

    Result<UserInfo> updateUserInfoById(String userId, int status);

    void updateUserInfoById(UserInfo userInfo);
}
