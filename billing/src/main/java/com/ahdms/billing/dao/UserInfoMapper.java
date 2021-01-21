package com.ahdms.billing.dao;

import java.util.Map;

import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserInfoForService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInfoMapper {

    int deleteByPrimaryKey(String id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

    List<UserInfo> findAll();

    List<UserInfoForService> queryAll();

    UserInfo selectByUsername(@Param("username")String username);

    UserInfo selectByUsernameAndPassword(@Param("username") String username,
                                         @Param("password") String password);

    String selectUSerId(Map<String, String> map);

    List<UserInfoForService> selectLikeUserName(@Param("username")String username);

	UserInfo selectByUsernameOrUserAccountOrServiceAccount(@Param("username")String username, @Param("userAccount")String userAccount, @Param("serviceAccount")String serviceAccount);

	UserInfo selectByServiceAccount(@Param("serviceAccount")String serviceAccount);

    UserInfo selectByServiceInfo(@Param("serviceAccount")String serviceAccount,@Param("servicePwd")String servicePwd);
}