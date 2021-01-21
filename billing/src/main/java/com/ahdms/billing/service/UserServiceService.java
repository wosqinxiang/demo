package com.ahdms.billing.service;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.UserService;
import com.ahdms.billing.vo.UserServiceDetailVO;

import org.springframework.stereotype.Service;

import java.util.List;


public interface UserServiceService {

    Result<String> addUserService(UserService userService);

    Result<UserService> getUserServiceById(String serviceId);

    Result<UserService> updateUserServiceById(UserService userService);

    Result<List<UserService>> selectByUserId(String userId);

    Result<List<UserService>> queryServiceDetailByUserId(String userId, String typeId);

    Result<Object> findAll();

	Result<List<UserServiceDetailVO>> queryServiceDetailByUserIdAndType(String userId, String typeId);
}
